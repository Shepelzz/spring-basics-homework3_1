package com.model;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "FILES")
public class File {
    @Id
    @SequenceGenerator(name = "FILE_SEQ", sequenceName = "FILE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_SEQ")
    @Column(name = "ID")
    private long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "FORMAT")
    private String format;
    @Column(name = "FILE_SIZE")
    private long size;
    @ManyToOne
    @JoinColumn(name="STORAGE_ID", nullable = false)
    private Storage storage;

    public File() {}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public long getSize() {
        return size;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return Objects.equals(name, file.name) &&
                Objects.equals(format, file.format) &&
                Objects.equals(storage, file.storage);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, format, storage);
    }
}
