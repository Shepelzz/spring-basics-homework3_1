package com.dao;

import com.exception.InternalServerError;
import com.model.File;

public interface FileDAO {

    public void put(File file) throws InternalServerError;

    public void update(File file) throws InternalServerError;

    public void delete(File file) throws InternalServerError;

    public File findById(long id) throws InternalServerError;

}
