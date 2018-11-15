package com.dao;

import com.exception.InternalServerError;
import com.model.Model;
import org.hibernate.SessionFactory;

public interface GeneralDAO<T extends Model> {

    T findById(Long id) throws InternalServerError;
    SessionFactory createSessionFactory();

}
