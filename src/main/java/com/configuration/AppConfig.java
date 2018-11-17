package com.configuration;

import com.Application;
import com.controller.Controller;
import com.dao.*;
import com.service.Service;
import com.service.ServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com")
public class AppConfig {

    @Bean
    public StorageDAO storageDAO(){
        return new StorageDAOImpl();
    }

    @Bean
    public FileDAO fileDAO(StorageDAO storageDAO){
        return new FileDAOImpl(storageDAO);
    }

    @Bean
    public Service service(FileDAO fileDAO, StorageDAO storageDAO){
        return new ServiceImpl(fileDAO, storageDAO);
    }

    @Bean
    public Controller controller(Service service){
        return new Controller(service);
    }

    @Bean
    public Application application(Controller controller, FileDAO fileDAO, StorageDAO storageDAO){
        return new Application(controller, storageDAO, fileDAO);
    }
}
