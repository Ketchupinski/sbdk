package com.labdb.labdb.entity;

import java.io.Serializable;
import java.util.Objects;

public class Doctor implements Serializable {
    private int id;
    private String name;

    private String specialization;

    private Doctor() {}

    public static Doctor create(String name, String specialization) {
        Doctor doctor = new Doctor();
        doctor.setName(name);
        doctor.setSpecialization(specialization);
        return doctor;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return id == doctor.id && Objects.equals(name, doctor.name) && Objects.equals(specialization, doctor.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specialization);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}
