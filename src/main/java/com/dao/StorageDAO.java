package com.dao;

import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;
import org.hibernate.Session;

public interface StorageDAO extends GeneralDAO<Storage>{
    enum sizeActions {INCREASE, DECREASE}

    void changeSize(Long id, Long size, Session session, sizeActions act) throws InternalServerError;
    int checkStorageOnExistingFiles(File file) throws InternalServerError;
    int checkStorageOnExistingFiles(Storage storageFrom, Storage storageTo) throws InternalServerError;

}
