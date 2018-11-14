package com.dao;

import com.exception.BadRequestException;
import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileDAOImpl extends GeneralDAOImpl implements FileDAO{
    private static final String SQL_SAVE = "INSERT INTO FILES VALUES(?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE FILES SET NAME = ?, FORMAT = ?, FILE_SIZE = ?, STORAGE_ID = ? WHERE ID = ?";
    private static final String SQL_UPDATE_BY_STORAGE_ID = "UPDATE FILES SET STORAGE_ID = ? WHERE STORAGE_ID = ?";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM FILES WHERE ID = ?";
    private static final String SQL_DELETE = "DELETE FROM FILES WHERE ID = ?";
    private static final String SQL_GET_ID = "SELECT FILE_ID_SEQ.NEXTVAL FROM DUAL";
    private static final String SQL_GET_FILES_BY_STORAGE_ID = "SELECT * FROM FILES WHERE STORAGE_ID = ?";

    @Override
    public File put(Storage storage, File file) throws InternalServerError {
        try(Connection conn = getConnection()){
            return putFileIntoStorage(storage, file, conn);
        }catch (SQLException e){
            throw new InternalServerError(getClass().getName()+"-put. File id:"+file.getId()+" was not saved to Storage id: "+storage.getId()+". "+e.getMessage());
        }
    }

    @Override
    public void delete(Storage storage, File file) throws InternalServerError{
        try(Connection conn = getConnection()){
            deleteFileFromStorage(storage, file, conn);
        }catch (SQLException e){
            throw new InternalServerError(getClass().getName()+"-delete. File id:"+file.getId()+" was not delete from Storage id: "+storage.getId()+". "+e.getMessage());
        }
    }

    @Override
    public void transferFiles(Storage storageFrom, Storage storageTo, long filesSize) throws InternalServerError{
        try(Connection conn = getConnection()){
            updateFilesByStorageId(storageFrom, storageTo, filesSize, conn);
        }catch (SQLException e){
            throw new InternalServerError(getClass().getName()+"-transferFiles. Transfer fail from storage id:"+storageFrom.getId()+" to id:"+storageTo.getId()+". "+e.getMessage());
        }
    }

    @Override
    public void transferFile(Storage storageFrom, Storage storageTo, File file) throws InternalServerError{
        try(Connection conn = getConnection()){
            updateFile(storageFrom, storageTo, file, conn);
        }catch (SQLException e){
            throw new InternalServerError(getClass().getName()+"-transferFile. Transfer File id:"+file.getId()+" from storage id:"+storageFrom.getId()+" to id:"+storageTo.getId()+" failed. "+e.getMessage());
        }
    }

    @Override
    public File findById(long id) throws InternalServerError{
        try(Connection conn = getConnection(); PreparedStatement prpStmt = conn.prepareStatement(SQL_FIND_BY_ID)){
            prpStmt.setLong(1, id);

            ResultSet rs = prpStmt.executeQuery();
            if(rs.next()) {
                return getFileFromResultSet(rs);
            }
            throw new BadRequestException(getClass().getName()+"-findById. There is no file with id "+id);
        }catch (SQLException e){
            throw new InternalServerError(getClass().getName()+"-findById. "+e.getMessage());
        }
    }

    @Override
    public List<File> getFilesByStorageId(long id) throws InternalServerError{
        try(Connection conn = getConnection(); PreparedStatement prStmt = conn.prepareStatement(SQL_GET_FILES_BY_STORAGE_ID)){
            prStmt.setLong(1, id);
            ResultSet rs = prStmt.executeQuery();
            List<File> files = new ArrayList<>();
            while(rs.next()){
                files.add(getFileFromResultSet(rs));
            }
            return files;
        }catch (SQLException e){
            throw new InternalServerError(getClass().getName()+"-getFilesByStorageId. "+e.getMessage());
        }
    }


    private File putFileIntoStorage(Storage storage, File file, Connection conn) throws InternalServerError, SQLException{
        try(PreparedStatement prpStmt = conn.prepareStatement(SQL_SAVE)){
            conn.setAutoCommit(false);

            file.setId(getNewEntityId(SQL_GET_ID));

            prpStmt.setLong(1, file.getId());
            prpStmt.setString(2, file.getName());
            prpStmt.setString(3, file.getFormat());
            prpStmt.setLong(4, file.getSize());
            prpStmt.setLong(5, file.getStorage().getId());

            if(prpStmt.executeUpdate() == 0)
                throw new InternalServerError(getClass().getName()+"-save. File with id "+file.getId()+" was not saved");

            StorageDAOImpl storageDAO = new StorageDAOImpl();
            storageDAO.decreaseSize(storage.getId(), file.getSize(), conn);

            conn.commit();
            return file;
        }catch (SQLException e){
            conn.rollback();
            throw e;
        }
    }

    private void deleteFileFromStorage(Storage storage, File file, Connection conn) throws InternalServerError, SQLException{
        try(PreparedStatement prpStmt = conn.prepareStatement(SQL_DELETE)){
            conn.setAutoCommit(false);

            prpStmt.setLong(1, file.getId());
            if(prpStmt.executeUpdate() == 0)
                throw new InternalServerError(getClass().getName()+"-delete. File id:"+file.getId()+" was not deleted from Storage id:"+storage.getId());

            StorageDAOImpl storageDAO = new StorageDAOImpl();
            storageDAO.increaseSize(storage.getId(), file.getSize(), conn);

            conn.commit();
        }catch (SQLException e){
            conn.rollback();
            throw e;
        }
    }

    private void updateFilesByStorageId(Storage storageFrom, Storage storageTo, long filesSize, Connection conn) throws InternalServerError, SQLException{
        try(PreparedStatement prpStmt = conn.prepareStatement(SQL_UPDATE_BY_STORAGE_ID)){
            conn.setAutoCommit(false);

            prpStmt.setLong(1, storageTo.getId());
            prpStmt.setLong(2, storageFrom.getId());

            if(prpStmt.executeUpdate() == 0)
                throw new InternalServerError(getClass().getName()+"-transferFiles. Transfer fail from storage id:"+storageFrom.getId()+" to id:"+storageTo.getId());

            StorageDAOImpl storageDAO = new StorageDAOImpl();
            storageDAO.increaseSize(storageFrom.getId(), filesSize, conn);
            storageDAO.decreaseSize(storageTo.getId(), filesSize, conn);

            conn.commit();
        }catch (SQLException e){
            conn.rollback();
            throw e;
        }
    }

    private void updateFile(Storage storageFrom, Storage storageTo, File file, Connection conn) throws InternalServerError, SQLException{
        try(PreparedStatement prpStmt = conn.prepareStatement(SQL_UPDATE)){
            conn.setAutoCommit(false);

            prpStmt.setString(1, file.getName());
            prpStmt.setString(2, file.getFormat());
            prpStmt.setLong(3, file.getSize());
            prpStmt.setLong(4, file.getStorage().getId());
            prpStmt.setLong(5, file.getId());

            if(prpStmt.executeUpdate() == 0)
                throw new InternalServerError(getClass().getName()+"-transferFile. Transfer File id:"+file.getId()+" from storage id:"+storageFrom.getId()+" to id:"+storageTo.getId()+" failed.");

            StorageDAOImpl storageDAO = new StorageDAOImpl();
            storageDAO.increaseSize(storageFrom.getId(), file.getSize(), conn);
            storageDAO.decreaseSize(storageTo.getId(), -file.getSize(), conn);

            conn.commit();
        }catch (SQLException e){
            conn.rollback();
            throw e;
        }
    }


    private File getFileFromResultSet(ResultSet rs) throws SQLException{
        File file = new File();
            file.setId(rs.getLong(1));
            file.setName(rs.getString(2));
            file.setFormat(rs.getString(3));
            file.setSize(rs.getLong(4));
            file.setStorage(new StorageDAOImpl().findById(rs.getLong(5)));
        return file;
    }
}
