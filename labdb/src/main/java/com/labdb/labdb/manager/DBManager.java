package com.labdb.labdb.manager;

import com.labdb.labdb.entity.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBManager {
    private static DBManager instance;

    private DBManager() {}

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public List<Doctor> getDoctors(Connection connection) {
        String query = "SELECT * FROM doctors";
        List<Doctor> doctors = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                Doctor doctor = Doctor.create(resultSet.getString("name"),
                        resultSet.getString("specialization"));
                doctor.setId(resultSet.getInt("id"));
                doctors.add(doctor);
            }
            return doctors;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public boolean createDoctor(Doctor doctor, Connection connection) {
        String query = "INSERT INTO doctors (name, specialization) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, doctor.getName());
            statement.setString(2, doctor.getSpecialization());
            int result = statement.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    doctor.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Doctor> getDoctorBySpec(String specialization, Connection connection) {
        String query = "SELECT * FROM doctors WHERE specialization = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, specialization);
            ResultSet resultSet = statement.executeQuery();
            List<Doctor> doctors = new ArrayList<>();
            while (resultSet.next()) {
                Doctor doctor = Doctor.create(resultSet.getString("name"),
                        resultSet.getString("specialization"));
                doctor.setId(resultSet.getInt("id"));
                doctors.add(doctor);
            }
            return doctors;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public boolean editDoctorParams(Doctor doctor, Doctor newDoctor, Connection connection) {
        String query = "UPDATE doctors SET name = ?, specialization = ? WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, newDoctor.getName());
            preparedStatement.setString(2, newDoctor.getSpecialization());
            preparedStatement.setString(3, doctor.getName());
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    doctor.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Doctor getDoctorById(int id, Connection connection) {
        String query = "SELECT * FROM doctors WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Doctor doctor = Doctor.create(resultSet.getString("name"),
                        resultSet.getString("specialization"));
                doctor.setId(resultSet.getInt("id"));
                return doctor;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean removeDoctor(Doctor doctor, Connection connection) {
        String query = "DELETE FROM doctors WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, doctor.getId());
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getDoctorId(Doctor doctor, Connection connection) {
        String query = "SELECT id FROM doctors WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, doctor.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                doctor.setId(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
