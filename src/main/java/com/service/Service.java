package com.service;

import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;

public interface Service {

    File put(Storage storage, File file) throws InternalServerError;
    File delete(Storage storage, File file) throws InternalServerError;
    void transferAll(Storage storageFrom, Storage storageTo) throws InternalServerError;
    void transferFile(Storage storageFrom, Storage storageTo, Long id) throws InternalServerError;
    public Storage findById(Long id) throws InternalServerError;

}
