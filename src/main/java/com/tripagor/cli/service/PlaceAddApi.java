package com.tripagor.cli.service;

import com.tripagor.model.PlaceAddRequest;
import com.tripagor.model.PlaceAddResponse;

public interface PlaceAddApi {
	PlaceAddResponse add(PlaceAddRequest place);
}
