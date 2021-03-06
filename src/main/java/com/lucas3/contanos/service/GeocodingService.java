package com.lucas3.contanos.service;

import com.lucas3.contanos.model.exception.FailedReverseGeocodeException;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.response.geocoding.GeocodingAddress;
import com.lucas3.contanos.model.response.geocoding.LocationResponse;
import com.lucas3.contanos.model.response.geocoding.ReverseGeocodingResponse;
import com.lucas3.contanos.model.response.imgbb.UploadImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodingService implements  IGeocodingService{

    @Value("${contame.app.geocoding.client}")
    private String clientIdGeocoding;

    @Override
    public LocationResponse getLocationFromCoordinates(Double latitude, Double longitude) throws FailedReverseGeocodeException, NullPointerException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://us1.locationiq.com/v1/reverse.php?key="+clientIdGeocoding+
                    "&format=json"+
                    "&accept-language=es"+
                    "&normalizeaddress=1"+
                    "&lat="+latitude.toString()+"&lon="+longitude.toString();

        ResponseEntity<ReverseGeocodingResponse> result = restTemplate.getForEntity(url,ReverseGeocodingResponse.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            return convertAddress(result.getBody().getAddress());
        } else {
            throw new FailedReverseGeocodeException();
        }
    }


    private LocationResponse convertAddress(GeocodingAddress address){
        LocationResponse location = new LocationResponse();
        if(address.getName() == null || address.getName().isEmpty()){
            location.setAddress(address.getRoad()+" "+ address.getHouse_number()+", "+ address.getSuburb());
        }else{
            location.setAddress(address.getName()+ ", " + address.getSuburb());
        }
        location.setHood(address.getSuburb());
        return location;
    }
}
