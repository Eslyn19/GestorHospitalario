package cr.ac.una.data_access_layer;

import cr.ac.una.domain_layer.Receta;
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

public class RecetaFileStore implements IFileStore<Receta> {

    private final File xmlFile;

    public RecetaFileStore(File xmlFile) {
        this.xmlFile = xmlFile;
        ensureFile();
    }

    @Override
    public List<Receta> readAll() {
        List<Receta> out = new ArrayList<>();
        if (xmlFile.length() == 0) return out;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            JAXBContext ctx = JAXBContext.newInstance(Receta.class);
            Unmarshaller u = ctx.createUnmarshaller();
            NodeList recetaNodos = doc.getElementsByTagName("receta");

            for (int i = 0; i < recetaNodos.getLength(); i++) {
                Node recetaNodo = recetaNodos.item(i);
                if (recetaNodo.getNodeType() == Node.ELEMENT_NODE) {
                    Receta r = (Receta) u.unmarshal(recetaNodo);
                    out.add(r);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return out;
    }

    @Override
    public void writeAll(List<Receta> data) {
        try (FileOutputStream out = new FileOutputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(Receta.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xw = xof.createXMLStreamWriter(out, "UTF-8");

            xw.writeStartDocument("UTF-8", "1.0");
            xw.writeStartElement("recetas");

            if (data != null) {
                for (Receta receta : data) {
                    m.marshal(receta, xw);
                }
            }

            xw.writeEndElement();
            xw.writeEndDocument();
            xw.flush();
            xw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                xmlFile.delete();
                ensureFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
