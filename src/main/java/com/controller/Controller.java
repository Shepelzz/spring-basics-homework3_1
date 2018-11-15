package com.controller;

import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;
import com.service.Service;
import com.service.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;


@org.springframework.stereotype.Controller
public class Controller {
    private Service service;

    @Autowired
    public Controller(ServiceImpl service) {
        this.service = service;
    }

    public void put(Storage storage, File file) throws InternalServerError {
        service.put(storage, file);
        System.out.println("put done");
    }

    public void delete(Storage storage, File file) throws InternalServerError {
        service.delete(storage, file);
        System.out.println("delete done");
    }

    public void transferAll(Storage storageFrom, Storage storageTo) throws InternalServerError {
        service.transferAll(storageFrom, storageTo);
        System.out.println("transferAll done");
    }

    public void transferFile(Storage storageFrom, Storage storageTo, Long id) throws InternalServerError {
        service.transferFile(storageFrom, storageTo, id);
        System.out.println("transferFile done");
    }

    public Storage findById(Long id) throws InternalServerError{
        return service.findById(id);
    }
}
