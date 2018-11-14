package com.dao;

import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;

import java.sql.Connection;
import java.sql.SQLException;

public interface StorageDAO {

    void increaseSize(long id, long size, Connection conn) throws InternalServerError;
    void decreaseSize(long id, long size, Connection conn) throws InternalServerError;
    void checkStorageOnExistingFiles(Storage storageTo, File file) throws InternalServerError;
    void checkStorageOnExistingFiles(Storage storageFrom, Storage storageTo) throws InternalServerError;
    Storage findById(long id) throws SQLException;

}
