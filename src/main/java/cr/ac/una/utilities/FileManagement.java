package cr.ac.una.utilities;

import com.sun.istack.NotNull;
import cr.ac.una.data_access_layer.*;
import cr.ac.una.domain_layer.*;

import java.io.File;

public class FileManagement {
    static File basDir = new File(System.getProperty("user.dir"));

    @NotNull
    public static IFileStore<Doctor> getDoctoresFileStore(String filename){
        File doctoresXml = new File(basDir, filename);
        return new DoctorFileStore(doctoresXml);
    }

    @NotNull
    public static IFileStore<Farmaceuta> getFarmaceutasFileStore(String filename){
        File farmaceutasXml = new File(basDir, filename);
        return new FarmaceutaFileStore(farmaceutasXml);
    }

    @NotNull
    public static IFileStore<Paciente> getPacientesFileStore(String filename){
        File pacientesXml = new File(basDir, filename);
        return new PacienteFileStore(pacientesXml);
    }

    @NotNull
    public static IFileStore<Medicamento> getMedicamentoFileStore(String filename){
        File medicamentosXml = new File(basDir, filename);
        return new MedicamentoFileStore(medicamentosXml);
    }
    
    @NotNull
    public static AdminFileStore getAdminFileStore(String filename){
        File adminXml = new File(basDir, filename);
        return new AdminFileStore(adminXml);
    }

    @NotNull
    public static IFileStore<Receta> getRecetaFileStore(String filename){
        File recetasXml = new File(basDir, filename);
        return new RecetaFileStore(recetasXml);
    }
}
