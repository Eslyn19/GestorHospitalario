package cr.ac.una;

import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.presentation_layer.Controller.DoctorController;
import cr.ac.una.presentation_layer.Model.DoctorTableModel;
import cr.ac.una.presentation_layer.Views.DoctorView;
//import cr.ac.una.presentation_layer.Views.Registro;
import cr.ac.una.presentation_layer.Views.PanelPrincipal;
import cr.ac.una.service_layer.DoctorService;
import cr.ac.una.service_layer.IService;
import cr.ac.una.utilities.FileManagement;

import javax.swing.*;
import java.util.Dictionary;
import java.util.Hashtable;

/*

This links goes in the README.md

Credits for the resources provided for the project
<a href="https://www.flaticon.com/free-icons/reset-password" title="reset password icons">Reset password icons created by Freepik - Flaticon</a>
*/

public class Main {
    public static void main(String[] args) {
//        Registro registro = new Registro();
//        registro.setVisible(true);
        PanelPrincipal panelPrincipal = new PanelPrincipal();
        panelPrincipal.setVisible(true);
    }
}