package com.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "FILES")
public class File implements Serializable {
    @Id
    @SequenceGenerator(name = "FILE_SEQ", sequenceName = "FILE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "FORMAT")
    private String format;
    @Column(name = "FILE_SIZE")
    private Long size;
    @ManyToOne
    @JoinColumn(name="STORAGE_ID", nullable = false)
    private Storage storage;

    public File() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public Long getSize() {
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
                Objects.equals(size, file.size) &&
                Objects.equals(storage, file.storage);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, format, size, storage);
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", format='" + format + '\'' +
                ", size=" + size +
                ", storage=" + (storage == null ? "" : storage.getId()) +
                '}';
    }
}
