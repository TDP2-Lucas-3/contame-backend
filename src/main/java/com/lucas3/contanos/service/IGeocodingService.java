package com.lucas3.contanos.service;

import com.lucas3.contanos.model.exception.FailedReverseGeocodeException;

public interface IGeocodingService {

    String getLocationFromCoordinates(Double latitude, Double longitude) throws FailedReverseGeocodeException;
}
