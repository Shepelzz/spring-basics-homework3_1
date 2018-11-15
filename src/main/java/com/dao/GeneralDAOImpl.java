package com.dao;

import com.exception.BadRequestException;
import com.exception.InternalServerError;
import com.model.Model;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.sql.*;

@Repository
public abstract class GeneralDAOImpl<T extends Model> implements GeneralDAO<T>{

    private Class<T> clazz;
    private SessionFactory sessionFactory;

    public final void setClazz( Class<T> clazzToSet ){
        this.clazz = clazzToSet;
    }

    public T findById(Long id) throws InternalServerError{
        try (Session session = createSessionFactory().openSession()) {

            return session.get(clazz, id);

        } catch (HibernateException e) {
            throw new InternalServerError(getClass().getSimpleName()+"-findById: "+id+" failed. "+e.getMessage());
        } catch (NoResultException noe){
            throw new BadRequestException("There is not File with id: "+id+". "+noe.getMessage());
        }
    }

    public SessionFactory createSessionFactory(){
        if(sessionFactory == null)
            sessionFactory = new Configuration().configure().buildSessionFactory();
        return sessionFactory;
    }
}
