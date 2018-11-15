package com.dao;

import com.exception.BadRequestException;
import com.exception.InternalServerError;
import com.model.File;
import com.model.Storage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

import static com.dao.StorageDAO.sizeActions.*;

@Repository
public class FileDAOImpl extends GeneralDAOImpl<File> implements FileDAO{

    private static final String SQL_UPDATE_BY_STORAGE_ID = "UPDATE FILES SET STORAGE_ID = :storageTo WHERE STORAGE_ID = :storageFrom";
    private static final String SQL_GET_FILES_BY_STORAGE_ID = "SELECT * FROM FILES WHERE STORAGE_ID = :storageId";

    private StorageDAO storageDAO;

    @Autowired
    public FileDAOImpl(StorageDAOImpl storageDAO) {
        this.storageDAO = storageDAO;
        setClazz(File.class);
    }

    @Override
    public File put(Storage storage, File file) throws InternalServerError {
        Transaction transaction = null;
        try (Session session = createSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            session.save(file);
            storageDAO.changeSize(storage.getId(), file.getSize(), session, DECREASE);

            session.getTransaction().commit();
            return file;
        } catch (HibernateException e) {
            if (transaction != null)
                transaction.rollback();
            throw new InternalServerError(getClass().getName()+"-put. File id:"+file.getId()+" was not saved to Storage id: "+storage.getId()+". "+e.getMessage());
        }
    }

    @Override
    public File delete(Storage storage, File file) throws InternalServerError{
        Transaction transaction = null;
        try (Session session = createSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            session.delete(file);
            storageDAO.changeSize(storage.getId(), file.getSize(), session, INCREASE);

            session.getTransaction().commit();
            return file;
        } catch (HibernateException e) {
            if (transaction != null)
                transaction.rollback();
            throw new InternalServerError(getClass().getName()+"-delete. File id:"+file.getId()+" was not delete from Storage id: "+storage.getId()+". "+e.getMessage());
        }
    }

    @Override
    public void transferFiles(Storage storageFrom, Storage storageTo, long filesSize) throws InternalServerError{
        Transaction transaction = null;
        try (Session session = createSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            Query query = session.createSQLQuery(SQL_UPDATE_BY_STORAGE_ID);
            query.setParameter("storageTo", storageTo.getId());
            query.setParameter("storageFrom", storageFrom.getId());

            if(query.executeUpdate() == 0)
                throw new InternalServerError(getClass().getName()+"-transferFiles. Transfer fail from storage id:"+storageFrom.getId()+" to id:"+storageTo.getId());
            storageDAO.changeSize(storageFrom.getId(), filesSize, session, INCREASE);
            storageDAO.changeSize(storageTo.getId(), filesSize, session, DECREASE);

            session.getTransaction().commit();
        }catch (HibernateException e){
            if (transaction != null)
                transaction.rollback();
            throw new InternalServerError(getClass().getName()+"-transferFiles. Transfer fail from storage id:"+storageFrom.getId()+" to id:"+storageTo.getId()+". "+e.getMessage());
        }
    }

    @Override
    public void transferFile(Storage storageFrom, Storage storageTo, File file) throws InternalServerError{
        Transaction transaction = null;
        try (Session session = createSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            session.update(file);
            storageDAO.changeSize(storageFrom.getId(), file.getSize(), session, INCREASE);
            storageDAO.changeSize(storageTo.getId(), file.getSize(), session, DECREASE);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (transaction != null)
                transaction.rollback();
            throw new InternalServerError(getClass().getName()+"-transferFile. Transfer File id:"+file.getId()+" from storage id:"+storageFrom.getId()+" to id:"+storageTo.getId()+" failed.");
        }
    }

    @Override
    public List<File> getFilesByStorageId(Long id) throws InternalServerError {
        try (Session session = createSessionFactory().openSession()) {
            return (List<File>) session.createSQLQuery(SQL_GET_FILES_BY_STORAGE_ID)
                    .setParameter("storageId", id)
                    .addEntity(File.class).list();
        } catch (HibernateException e) {
            throw new InternalServerError(getClass().getName() + "-getFilesByStorageId. " + e.getMessage());
        }
    }
}
