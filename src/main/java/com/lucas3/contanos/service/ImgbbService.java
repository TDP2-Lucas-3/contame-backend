package com.lucas3.contanos.service;

import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.response.imgbb.UploadImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ImgbbService implements IImgbbService {

    @Value("${contame.app.imgbb.client}")
    private String clientIdImgbb;

    @Override
    public String uploadImgToImgbb(String image) throws FailedToLoadImageException {
        RestTemplate restTemplate = new RestTemplate();
        String url= "https://api.imgbb.com/1/upload?key="+ clientIdImgbb;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("image", image);
        map.add("key", clientIdImgbb);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<UploadImageResponse> result = restTemplate.postForEntity(url, request, UploadImageResponse.class);
        if (result.getBody().isSuccess()) {
            return result.getBody().getData().getUrl();
        } else {
            throw new FailedToLoadImageException();
        }
    }
}
