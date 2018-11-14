package com.service;

import com.dao.FileDAO;
import com.dao.FileDAOImpl;
import com.dao.StorageDAO;
import com.dao.StorageDAOImpl;
import com.exception.BadRequestException;
import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;
import org.springframework.beans.factory.annotation.Autowired;

public class ServiceImpl implements Service{
    private FileDAO fileDAO;
    private StorageDAO storageDAO;

    @Autowired
    public ServiceImpl(FileDAOImpl fileDAO, StorageDAOImpl storageDAO) {
        this.fileDAO = fileDAO;
        this.storageDAO = storageDAO;
    }

    @Override
    public File put(Storage storage, File file) throws InternalServerError {
        file.setStorage(storage);
        validateFile(storage, file);

        return fileDAO.put(storage, file);
    }

    @Override
    public void delete(Storage storage, File file) throws InternalServerError {
        fileDAO.delete(storage, file);
    }

    @Override
    public void transferAll(Storage storageFrom, Storage storageTo) throws InternalServerError{
        //check if exists
        storageDAO.checkStorageOnExistingFiles(storageFrom, storageTo);
        //check size and format
        long filesSize = 0;
        long storageSize = storageTo.getStorageSize();
        for (File file : fileDAO.getFilesByStorageId(storageFrom.getId())) {
            filesSize += file.getSize();
            storageSize -= file.getSize();

            checkFileFormat(storageTo, file);
            if (storageSize < 0)
                throw new BadRequestException(getClass().getName() + "-transferAll. Storage is full. storage id:" + storageTo.getId() + (file.getId() == 0 ? "" : " file id:" + file.getId()));
        }

        fileDAO.transferFiles(storageFrom, storageTo, filesSize);
    }

    @Override
    public void transferFile(Storage storageFrom, Storage storageTo, long id) throws InternalServerError{
        File file = fileDAO.findById(id);
        file.setStorage(storageTo);

        validateFile(storageTo, file);

        fileDAO.transferFile(storageFrom, storageTo, file);
    }

    private void validateFile(Storage storage, File file) throws InternalServerError{
        //check if exists
        storageDAO.checkStorageOnExistingFiles(storage, file);
        //check size
        if(storage.getStorageSize() < file.getSize())
            throw new BadRequestException(getClass().getName()+"-checkInputFileSize. There is no enough free space in storage id:"+storage.getId()+" file id:"+file.getId());
        //check format
        checkFileFormat(storage, file);
    }

    private void checkFileFormat(Storage storage, File file){
        for(String format : storage.getFormatsSupported())
            if(format.equals(file.getFormat()))
                return;
        throw new BadRequestException(getClass().getName()+"-checkInputFileFormat. File format is not accepted. storage id:"+storage.getId()+" file id:"+file.getId());
    }
}
