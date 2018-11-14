package com.dao;

import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;

import java.util.List;

public interface FileDAO {

    File put(Storage storage, File file) throws InternalServerError;
    void delete(Storage storage, File file) throws InternalServerError;
    void transferFiles(Storage storageFrom, Storage storageTo, long filesSize) throws InternalServerError;
    void transferFile(Storage storageFrom, Storage storageTo, File file) throws InternalServerError;
    File findById(long id) throws InternalServerError;
    List<File> getFilesByStorageId(long id) throws InternalServerError;

}
