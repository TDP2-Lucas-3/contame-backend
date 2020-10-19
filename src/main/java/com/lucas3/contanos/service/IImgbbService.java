package com.lucas3.contanos.service;

import com.lucas3.contanos.model.exception.FailedToLoadImageException;

public interface IImgbbService {

    String uploadImgToImgbb(String image) throws FailedToLoadImageException;
}
