package com.labdb.labdb.dbservice;


import com.labdb.labdb.connection.ConnectionPool;
import com.labdb.labdb.connection.StandardConnectionPool;
import com.labdb.labdb.entity.Doctor;
import com.labdb.labdb.manager.DBManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public final class DBService {

    private DBService() {
    }

    private static ConnectionPool connectionPool;

    static {
        try {
            connectionPool = StandardConnectionPool.create("jdbc:postgresql://localhost:5432/clinic",
                    "javadude",
                    "pass");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Doctor> getDoctors() {
        try {
            Connection connection = connectionPool.getConnection();
            List<Doctor> doctorList = DBManager.getInstance().getDoctors(connection);
            connectionPool.releaseConnection(connection);
            return doctorList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static boolean createDoctor(String name, String specialization) {
        try {
            Connection connection = connectionPool.getConnection();
            boolean result = DBManager.getInstance().createDoctor(Doctor.create(name, specialization), connection);
            connectionPool.releaseConnection(connection);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Doctor> getDoctorBySpec(String specialization) {
        try {
            Connection connection = connectionPool.getConnection();
            List<Doctor> doctors = DBManager.getInstance().getDoctorBySpec(specialization, connection);
            connectionPool.releaseConnection(connection);
            return doctors;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Doctor editDoctorParams(Doctor doctor, Doctor newDoctor) {
        try {
            Connection connection = connectionPool.getConnection();
            Doctor editDoctor = null;
            if (DBManager.getInstance().editDoctorParams(doctor, newDoctor, connection)) {
                editDoctor = DBManager.getInstance().getDoctorById(doctor.getId(), connection);
            }
            connectionPool.releaseConnection(connection);
            return editDoctor;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean removeDoctor(Doctor doctor) {
        try {
            Connection connection = connectionPool.getConnection();
            DBManager.getInstance().getDoctorId(doctor, connection);
            boolean result = DBManager.getInstance().removeDoctor(doctor, connection);
            connectionPool.releaseConnection(connection);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

