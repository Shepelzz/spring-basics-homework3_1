package com.controller;

import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;

public interface Controller {

    File put(Storage storage, File file) throws InternalServerError;
    void delete(Storage storage, File file) throws InternalServerError;
    void transferAll(Storage storageFrom, Storage storageTo) throws InternalServerError;
    void transferFile(Storage storageFrom, Storage storageTo, long id) throws InternalServerError;


}
