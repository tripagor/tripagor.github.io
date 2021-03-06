package com.tripagor.markers;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.tripagor.cli.exporter.HotelMarkerWorker;
import com.tripagor.markers.model.HotelMarker;
import com.tripagor.markers.model.HotelMarkerExport;
import com.tripagor.markers.model.ProcessingStatus;

@Component
@RepositoryEventHandler(HotelMarkerExport.class)
public class HotelMarkerExportHandler {
	private final HotelMarkerWorker hotelMarkerWorker;
	private ExecutorService executor = Executors.newFixedThreadPool(10);
	private final String appendStr;
	private final HotelMarkerExportRepository hotelMarkerExportRepository;

	@Autowired
	public HotelMarkerExportHandler(HotelMarkerWorker hotelMarkerWorker,
			HotelMarkerExportRepository hotelMarkerExportRepository, @Value("${hotel.url.postfix}") String appendStr) {
		super();
		this.hotelMarkerWorker = hotelMarkerWorker;
		this.appendStr = appendStr;
		this.hotelMarkerExportRepository = hotelMarkerExportRepository;
	}

	@HandleBeforeCreate
	public void handleBeforeCreate(final HotelMarkerExport markerExport) {
		markerExport.setStatus(ProcessingStatus.CREATED);
	}

	@HandleAfterCreate
	public void handleAfterCreate(final HotelMarkerExport markerExport) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				Collection<HotelMarker> exported = hotelMarkerWorker.doHandle(markerExport.getNumberToMark(),
						appendStr);
				markerExport.setHotelsMarkers(exported);
				markerExport.setStatus(ProcessingStatus.PROCESSED);
				hotelMarkerExportRepository.save(markerExport);
			}
		});

	}
}