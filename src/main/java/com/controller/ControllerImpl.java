package com.controller;

import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;
import com.service.Service;
import com.service.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Controller
public class ControllerImpl implements Controller{
    private Service service;

    @Autowired
    public ControllerImpl(ServiceImpl service) {
        this.service = service;
    }

    @Override
    public File put(Storage storage, File file) throws InternalServerError {
        return service.put(storage, file);
    }

    @Override
    public void delete(Storage storage, File file) throws InternalServerError {
        service.delete(storage, file);
    }

    @Override
    public void transferAll(Storage storageFrom, Storage storageTo) throws InternalServerError {
        service.transferAll(storageFrom, storageTo);
    }

    @Override
    public void transferFile(Storage storageFrom, Storage storageTo, long id) throws InternalServerError {
        service.transferFile(storageFrom, storageTo, id);
    }
}
