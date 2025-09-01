package cr.ac.una;

import cr.ac.una.presentation_layer.Views.Registro;
import cr.ac.una.data_access_layer.AdminFileStore;
import cr.ac.una.service_layer.AdminService;
import cr.ac.una.presentation_layer.Controller.AdminController;
import cr.ac.una.utilities.FileManagement;

public class Main {
    public static void main(String[] args) {
        Registro registro = new Registro();

        AdminFileStore adminFileStore = FileManagement.getAdminFileStore("admins.xml");
        AdminService adminService = new AdminService(adminFileStore);
        AdminController adminController = new AdminController(adminService);
        registro.setAdminController(adminController);

        registro.setVisible(true);
    }
}