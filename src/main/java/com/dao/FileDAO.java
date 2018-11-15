package com.dao;

import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDAO {

    File put(Storage storage, File file) throws InternalServerError;
    File delete(Storage storage, File file) throws InternalServerError;
    void transferFiles(Storage storageFrom, Storage storageTo, long filesSize) throws InternalServerError;
    void transferFile(Storage storageFrom, Storage storageTo, File file) throws InternalServerError;
    File findById(Long id) throws InternalServerError;
    List<File> getFilesByStorageId(Long id) throws InternalServerError;

}
