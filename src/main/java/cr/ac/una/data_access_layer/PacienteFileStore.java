package cr.ac.una.data_access_layer;

import cr.ac.una.domain_layer.Paciente;
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

public class PacienteFileStore implements IFileStore<Paciente> {

    private final File xmlFile;

    public PacienteFileStore(File xmlFile) {
        this.xmlFile = xmlFile;
        ensureFile();
    }

    @Override
    public List<Paciente> readAll() {
        List<Paciente> out = new ArrayList<>();
        if (xmlFile.length() == 0) return out;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            JAXBContext ctx = JAXBContext.newInstance(Paciente.class);
            Unmarshaller u = ctx.createUnmarshaller();
            NodeList pacienteNodos = doc.getElementsByTagName("paciente");

            for (int i = 0; i < pacienteNodos.getLength(); i++) {
                Node pacienteNodo = pacienteNodos.item(i);
                if (pacienteNodo.getNodeType() == Node.ELEMENT_NODE) {
                    Paciente p = (Paciente) u.unmarshal(pacienteNodo);
                    out.add(p);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return out;
    }

    @Override
    public void writeAll(List<Paciente> data) {
        try (FileOutputStream out = new FileOutputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(Paciente.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xw = xof.createXMLStreamWriter(out, "UTF-8");

            xw.writeStartDocument("UTF-8", "1.0");
            xw.writeStartElement("pacientes");

            if (data != null) {
                for (Paciente p : data) {
                    m.marshal(p, xw);
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
