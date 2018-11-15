package com.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "STORAGE")
public class Storage extends Model {
    @Id
    @SequenceGenerator(name = "STORAGE_SEQ", sequenceName = "STORAGE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STORAGE_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "FORMATS_SUPPORTED")
    private String formatsSupported;
    @Column(name = "STORAGE_COUNTRY")
    private String storageCountry;
    @Column(name = "STORAGE_SIZE")
    private Long storageSize;

    public Storage(){}

    @Override
    public Long getId() {
        return id;
    }

    public String getFormatsSupported() {
        return formatsSupported;
    }

    public String getStorageCountry() {
        return storageCountry;
    }

    public Long getStorageSize() {
        return storageSize;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFormatsSupported(String formatsSupported) {
        this.formatsSupported = formatsSupported;
    }

    public void setStorageCountry(String storageCountry) {
        this.storageCountry = storageCountry;
    }

    public void setStorageSize(long storageSize) {
        this.storageSize = storageSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Storage storage = (Storage) o;
        return Objects.equals(id, storage.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}