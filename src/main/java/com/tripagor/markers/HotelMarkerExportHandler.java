package com.tripagor.markers;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.tripagor.cli.exporter.HotelMarker;
import com.tripagor.hotels.model.Hotel;
import com.tripagor.markers.model.HotelMarkerExport;

@Component
@RepositoryEventHandler(HotelMarkerExport.class)
public class HotelMarkerExportHandler {
	private final HotelMarker hotelMarker;
	private ExecutorService executor = Executors.newFixedThreadPool(10);
	private final String appendStr;
	private final HotelMarkerExportRepository hotelMarkerExportRepository;

	@Autowired
	public HotelMarkerExportHandler(HotelMarker hotelMarker, HotelMarkerExportRepository hotelMarkerExportRepository,
			@Value("${hotel.url.postfix}") String appendStr) {
		super();
		this.hotelMarker = hotelMarker;
		this.appendStr = appendStr;
		this.hotelMarkerExportRepository = hotelMarkerExportRepository;
	}

	@HandleAfterCreate
	public void handle(final HotelMarkerExport markerExport) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				Collection<Hotel> exported = hotelMarker.doMark(markerExport.getNumberToMark(), appendStr);
				markerExport.setHotels(exported);
				hotelMarkerExportRepository.save(markerExport);
			}
		});

	}
}