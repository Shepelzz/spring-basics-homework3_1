package com.dao;

import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageDAO {
    enum sizeActions {INCREASE, DECREASE}

    void changeSize(Long id, Long size, Session session, sizeActions act) throws InternalServerError;
    int checkStorageOnExistingFiles(File file) throws InternalServerError;
    int checkStorageOnExistingFiles(Storage storageFrom, Storage storageTo) throws InternalServerError;
    Storage findById(Long id) throws InternalServerError;

}
