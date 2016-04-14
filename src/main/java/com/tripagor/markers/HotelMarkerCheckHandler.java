package com.tripagor.markers;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.google.maps.model.LatLng;
import com.tripagor.hotels.model.Hotel;
import com.tripagor.markers.model.Approval;
import com.tripagor.markers.model.ApprovalStatus;
import com.tripagor.markers.model.HotelMarkerCheck;
import com.tripagor.markers.model.HotelMarkerExport;

@Component
@RepositoryEventHandler(HotelMarkerCheck.class)
public class HotelMarkerCheckHandler {
	private final com.tripagor.cli.exporter.HotelMarkerCheck hotelMarkerCheck;
	private ExecutorService executor = Executors.newFixedThreadPool(10);

	@Autowired
	public HotelMarkerCheckHandler(com.tripagor.cli.exporter.HotelMarkerCheck hotelMarkerCheck) {
		super();
		this.hotelMarkerCheck = hotelMarkerCheck;
	}

	@HandleAfterCreate
	public void handle(final HotelMarkerExport markerExport) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				Collection<Hotel> exported = hotelMarkerCheck.doCheck();
				for (Hotel hotel : exported) {
					Approval approval = new Approval();
					approval.setHotelId(hotel.getId());
					approval.setHotelName(hotel.getName());
					approval.setFormattedAddress(hotel.getFormattedAddress());
					approval.setLatLng(new LatLng(Double.parseDouble(hotel.getLatitude()),
							Double.parseDouble(hotel.getLongitude())));
					if (hotel.getIsMarkerApproved()) {
						if (hotel.getPlaceId() != null) {
							approval.setStatus(ApprovalStatus.APPROVED);
						} else {
							approval.setStatus(ApprovalStatus.REJECTED);
						}
					} else {
						approval.setStatus(ApprovalStatus.PENDING);
					}
				}
			}
		});

	}
}