package com.lucas3.contanos.service;

import com.lucas3.contanos.model.exception.FailedReverseGeocodeException;
import com.lucas3.contanos.model.response.geocoding.LocationResponse;

public interface IGeocodingService {

    LocationResponse getLocationFromCoordinates(Double latitude, Double longitude) throws FailedReverseGeocodeException;
}
