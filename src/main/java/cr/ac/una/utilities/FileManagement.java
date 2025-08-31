package cr.ac.una.utilities;

import com.sun.istack.NotNull;
import cr.ac.una.data_access_layer.DoctorFileStore;
import cr.ac.una.data_access_layer.IFileStore;
import cr.ac.una.domain_layer.Doctor;

import java.io.File;

public class FileManagement {
    static File basDir = new File(System.getProperty("user.dir"));

    @NotNull
    public static IFileStore<Doctor> getDoctoresFileStore(String filename){
        File doctoresXml = new File(basDir, filename);
        return new DoctorFileStore(doctoresXml);
    }
}
