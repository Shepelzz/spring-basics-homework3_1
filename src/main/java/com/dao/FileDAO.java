package com.dao;

import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;

import java.util.List;

public interface FileDAO extends GeneralDAO<File>{

    File put(Storage storage, File file) throws InternalServerError;
    File delete(Storage storage, File file) throws InternalServerError;
    void transferFiles(Storage storageFrom, Storage storageTo, long filesSize) throws InternalServerError;
    void transferFile(Storage storageFrom, Storage storageTo, File file) throws InternalServerError;
    List<File> getFilesByStorageId(Long id) throws InternalServerError;

}
