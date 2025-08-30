package cr.ac.una;

import cr.ac.una.data_access_layer.DoctorFileStore;
import cr.ac.una.presentation_layer.Views.Registro;
import cr.ac.una.service_layer.DoctorService;

import javax.swing.*;
import java.io.File;

/*

This links goes in the README.md

Credits for the resources provided for the project
<a href="https://www.flaticon.com/free-icons/reset-password" title="reset password icons">Reset password icons created by Freepik - Flaticon</a>
*/

public class Main {
    public static void main(String[] args) {

        // Initialize data storage
        File xmlFile = new File("data/doctores.xml");
        DoctorFileStore fileStore = new DoctorFileStore(xmlFile);
        DoctorService doctorService = new DoctorService(fileStore);
        
        // Start the application with the registration window
        SwingUtilities.invokeLater(() -> {
            new Registro();
        });
    }
}