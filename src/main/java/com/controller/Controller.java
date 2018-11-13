package com.controller;

import jdbc.lesson4.homework4_1.exception.InternalServerError;
import jdbc.lesson4.homework4_1.model.File;
import jdbc.lesson4.homework4_1.model.Storage;
import jdbc.lesson4.homework4_1.service.Service;

public class Controller {
    private Service service = new Service();

    public File put(Storage storage, File file) throws InternalServerError {
        return service.put(storage, file);
    }

    public void delete(Storage storage, File file) throws InternalServerError {
        service.delete(storage, file);
    }

    public void transferAll(Storage storageFrom, Storage storageTo) throws InternalServerError {
        service.transferAll(storageFrom, storageTo);
    }

    public void transferFile(Storage storageFrom, Storage storageTo, long id) throws InternalServerError {
        service.transferFile(storageFrom, storageTo, id);
    }
}
