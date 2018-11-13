package com.dao;

import com.exception.BadRequestException;
import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StorageDAOImpl extends GeneralDAO{
    private static final String SQL_FIND_BY_ID = "SELECT * FROM STORAGE WHERE ID = ?";
    private static final String SQL_INCREASE_SIZE = "UPDATE STORAGE SET STORAGE_SIZE = STORAGE_SIZE+? WHERE ID = ?";
    private static final String SQL_DECREASE_SIZE = "UPDATE STORAGE SET STORAGE_SIZE = STORAGE_SIZE-? WHERE ID = ?";
    private static final String SQL_CHECK_IF_EXISTS_BY_FILE_ID =
            "SELECT COUNT(*) FOUND FROM FILES WHERE STORAGE_ID = ? AND EXISTS (SELECT * FROM FILES Checked WHERE Checked.ID = ? AND Checked.NAME = FILES.NAME AND Checked.FORMAT = FILES.FORMAT AND Checked.FILE_SIZE = FILES.FILE_SIZE)";
    private static final String SQL_CHECK_IF_EXISTS_BY_STORAGE_ID =
            "SELECT COUNT(*) FOUND FROM FILES WHERE STORAGE_ID = ? AND EXISTS (SELECT * FROM FILES Checked WHERE Checked.STORAGE_ID = ? AND Checked.NAME = FILES.NAME AND Checked.FORMAT = FILES.FORMAT AND Checked.FILE_SIZE = FILES.FILE_SIZE)";


    void increaseSize(long id, long size, Connection conn) throws InternalServerError {
        try(PreparedStatement prpStmt = conn.prepareStatement(SQL_INCREASE_SIZE)){
            prpStmt.setLong(1, size);
            prpStmt.setLong(2, id);

            if(prpStmt.executeUpdate() == 0)
                throw new InternalServerError(getClass().getName()+"-increaseSize. Storage size with id "+id+" was not updated");
        }catch (SQLException e){
            throw new InternalServerError(getClass().getName()+"-increaseSize. Storage size with id "+id+" was not updated. "+e.getMessage());
        }
    }

    void decreaseSize(long id, long size, Connection conn) throws InternalServerError{
        try(PreparedStatement prpStmt = conn.prepareStatement(SQL_DECREASE_SIZE)){
            prpStmt.setLong(1, size);
            prpStmt.setLong(2, id);

            if(prpStmt.executeUpdate() == 0)
                throw new InternalServerError(getClass().getName()+"-decreaseSize. Storage size with id "+id+" was not updated");
        }catch (SQLException e){
            throw new InternalServerError(getClass().getName()+"-decreaseSize. Storage size with id "+id+" was not updated. "+e.getMessage());
        }
    }

    public void checkStorageOnExistingFiles(Storage storageTo, File file) throws InternalServerError{
        try(Connection conn = getConnection(); PreparedStatement prpStmt = conn.prepareStatement(SQL_CHECK_IF_EXISTS_BY_FILE_ID)){

            prpStmt.setLong(1, storageTo.getId());
            prpStmt.setLong(2, file.getId());

            ResultSet rs = prpStmt.executeQuery();
            rs.next();
            if(rs.getInt(1) > 0)
                throw new BadRequestException(getClass().getName()+"-checkStorageOnExistingFiles. There is existing file id:"+file.getId()+" in Storage id:"+storageTo.getId());

        }catch (SQLException e){
            throw new InternalServerError(getClass().getName()+"-checkStorageOnExistingFiles. "+e.getMessage());
        }
    }

    public void checkStorageOnExistingFiles(Storage storageFrom, Storage storageTo) throws InternalServerError{
        try(Connection conn = getConnection(); PreparedStatement prpStmt = conn.prepareStatement(SQL_CHECK_IF_EXISTS_BY_STORAGE_ID)){

            prpStmt.setLong(1, storageTo.getId());
            prpStmt.setLong(2, storageFrom.getId());

            ResultSet rs = prpStmt.executeQuery();
            rs.next();
            if(rs.getInt(1) > 0)
                throw new BadRequestException(getClass().getName()+"-checkStorageOnExistingFiles. There is existing file from Storage id:"+storageFrom.getId()+" in Storage id:"+storageTo.getId());

        }catch (SQLException e){
            throw new InternalServerError(getClass().getName()+"-checkStorageOnExistingFiles. "+e.getMessage());
        }
    }

    public Storage findById(long id) throws SQLException {
        try(Connection conn = getConnection(); PreparedStatement prpStmt = conn.prepareStatement(SQL_FIND_BY_ID)){
            prpStmt.setLong(1, id);

            ResultSet rs = prpStmt.executeQuery();
            if(rs.next()) {
                Storage storage = new Storage();
                    storage.setId(rs.getLong(1));
                    storage.setFormatsSupported(rs.getString(2).split(","));
                    storage.setStorageCountry(rs.getString(3));
                    storage.setStorageSize(rs.getLong(4));
                return storage;
            }
            throw new BadRequestException(getClass().getName()+"-findById. There is no Storage with id "+id);
        }catch (SQLException e){
            throw e;
        }
    }
}
