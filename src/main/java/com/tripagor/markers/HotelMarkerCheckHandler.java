package com.tripagor.markers;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.google.maps.model.LatLng;
import com.tripagor.markers.model.Approval;
import com.tripagor.markers.model.ApprovalStatus;
import com.tripagor.markers.model.HotelMarker;
import com.tripagor.markers.model.HotelMarkerCheck;
import com.tripagor.markers.model.ProcessingStatus;

@Component
@RepositoryEventHandler(HotelMarkerCheck.class)
public class HotelMarkerCheckHandler {
	private final com.tripagor.cli.exporter.HotelMarkerCheckWorker hotelMarkerCheck;
	private ExecutorService executor = Executors.newFixedThreadPool(10);
	private final HotelMarkerCheckRepository hotelMarkerCheckRepository;

	@Autowired
	public HotelMarkerCheckHandler(com.tripagor.cli.exporter.HotelMarkerCheckWorker hotelMarkerCheck,
			HotelMarkerCheckRepository hotelMarkerCheckRepository) {
		super();
		this.hotelMarkerCheck = hotelMarkerCheck;
		this.hotelMarkerCheckRepository = hotelMarkerCheckRepository;
	}

	@HandleBeforeCreate
	public void handleBeforeCreate(final HotelMarkerCheck markercheck) {
		markercheck.setStatus(ProcessingStatus.CREATED);
	}

	@HandleAfterCreate
	public void handleAfterCreate(final HotelMarkerCheck markercheck) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				Collection<HotelMarker> changed = hotelMarkerCheck.doCheck();
				for (HotelMarker hotelMarker : changed) {
					Approval approval = new Approval();
					markercheck.getApprovals().add(approval);
					approval.setHotelId(hotelMarker.getId());
					approval.setHotelName(hotelMarker.getName());
					approval.setFormattedAddress(hotelMarker.getAddress());
					approval.setUrl(hotelMarker.getWebsite());
					approval.setPlaceId(hotelMarker.getPlaceId());
					approval.setLatLng(
							new LatLng(hotelMarker.getLocation().getLat(), hotelMarker.getLocation().getLng()));
					if (hotelMarker.getIsOwned()) {
						approval.setStatus(ApprovalStatus.APPROVED);
					} else {
						approval.setStatus(ApprovalStatus.REJECTED);
					}
				}
				markercheck.setStatus(ProcessingStatus.PROCESSED);
				hotelMarkerCheckRepository.save(markercheck);
			}
		});

	}
}