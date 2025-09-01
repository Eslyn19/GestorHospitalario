package cr.ac.una.data_access_layer;

import cr.ac.una.domain_layer.Admin;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AdminFileStore implements IFileStore<Admin> {
    private final File xmlFile;

    public AdminFileStore(File xmlFile) {
        this.xmlFile = xmlFile;
        ensureFile();
    }

    @Override
    public List<Admin> readAll() {
        List<Admin> out = new ArrayList<>();
        if (xmlFile.length() == 0) return out;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            JAXBContext ctx = JAXBContext.newInstance(Admin.class);
            Unmarshaller u = ctx.createUnmarshaller();
            NodeList adminNodos = doc.getElementsByTagName("admin");

            for (int i = 0; i < adminNodos.getLength(); i++) {
                Node adminNodo = adminNodos.item(i);
                if (adminNodo.getNodeType() == Node.ELEMENT_NODE) {
                    Admin a = (Admin) u.unmarshal(adminNodo);
                    out.add(a);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return out;
    }

    @Override
    public void writeAll(List<Admin> data) {
        try (FileOutputStream out = new FileOutputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(Admin.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xw = xof.createXMLStreamWriter(out, "UTF-8");

            xw.writeStartDocument("UTF-8", "1.0");
            xw.writeStartElement("admins");

            if (data != null) {
                for (Admin admin : data) {
                    m.marshal(admin, xw);
                }
            }

            xw.writeEndElement();
            xw.writeEndDocument();
            xw.flush();
            xw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ensureFile() {
        try {
            File parent = xmlFile.getParentFile();

            if (parent != null) {
                parent.mkdirs();
            }

            if (!xmlFile.exists()) {
                xmlFile.createNewFile();
                writeAll(new ArrayList<>());
            }
        } catch (Exception ignored) {}
    }
}
