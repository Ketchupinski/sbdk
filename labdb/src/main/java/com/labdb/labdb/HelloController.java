package com.labdb.labdb;

import com.labdb.labdb.dbservice.DBService;
import com.labdb.labdb.entity.Doctor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class HelloController {

    public TextField editName;
    public TextField editSpec;
    @FXML
    private TextField createName;

    @FXML
    private TextField createSpec;

    @FXML
    private TextField findSpec;

    @FXML
    private ListView<String> doctorsList;

    @FXML
    public void getDoctors(ActionEvent actionEvent) {
        if (!doctorsList.getItems().isEmpty()) {
            doctorsList.getItems().clear();
        }
        List<Doctor> doctors = DBService.getDoctors();
        doctors.forEach(doctor -> doctorsList.getItems().add(doctor.getName() + " " + doctor.getSpecialization()));
    }

    @FXML
    public void createDoctor(ActionEvent actionEvent) {
        if (createName.getText().isEmpty() || createSpec.getText().isEmpty()) {
            return;
        }
        boolean result = DBService.createDoctor(createName.getText(), createSpec.getText());
        if (result) {
            doctorsList.getItems().add(createName.getText() + " " + createSpec.getText());
            createName.clear();
            createSpec.clear();
        } else {
            doctorsList.getItems().add("Error creating doctor");
        }
    }

    @FXML
    public void findDoctor(ActionEvent actionEvent) {
        if (findSpec.getText().isEmpty()) {
            return;
        }
        List<Doctor> doctors = DBService.getDoctorBySpec(findSpec.getText());
        if (!doctorsList.getItems().isEmpty()) {
            doctorsList.getItems().clear();
        }
        if (doctors != null && !doctors.isEmpty()) {
            doctors.forEach(doctor -> doctorsList.getItems().add(doctor.getName() + " " + doctor.getSpecialization()));
        } else {
            doctorsList.getItems().add("No doctors with such specialization");
        }

    }

    @FXML
    public void editDoctor(ActionEvent actionEvent) {
        if (doctorsList.getSelectionModel().getSelectedItem() == null
                || editName.getText() == null
                || editSpec.getText() == null) {
            return;
        }
        String[] doctorInfo = doctorsList.getSelectionModel().getSelectedItem().split(" ");
        Doctor doctor = Doctor.create(doctorInfo[0], doctorInfo[1]);
        Doctor newDoctor = Doctor.create(editName.getText(), editSpec.getText());
        Doctor editedDoctor = DBService.editDoctorParams(doctor, newDoctor);
        if (editedDoctor != null) {
            doctorsList.getItems().clear();
            doctorsList.getItems().add(editedDoctor.getName() + " " + editedDoctor.getSpecialization());
            editSpec.clear();
            editName.clear();
        }
    }

    @FXML
    public void removeDoctor(ActionEvent actionEvent) {
        if (doctorsList.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        String[] doctorInfo = doctorsList.getSelectionModel().getSelectedItem().split(" ");
        Doctor doctor = Doctor.create(doctorInfo[0], doctorInfo[1]);
        boolean result = DBService.removeDoctor(doctor);
        if (result) {
            doctorsList.getItems().remove(doctorsList.getSelectionModel().getSelectedItem());
        }
    }
}