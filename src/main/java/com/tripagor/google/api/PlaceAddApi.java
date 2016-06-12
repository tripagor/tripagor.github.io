package com.tripagor.google.api;

import com.tripagor.google.api.model.PlaceAddRequest;
import com.tripagor.google.api.model.PlaceAddResponse;

public interface PlaceAddApi {
	PlaceAddResponse add(PlaceAddRequest place);
}
