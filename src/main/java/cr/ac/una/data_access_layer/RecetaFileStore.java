package cr.ac.una.data_access_layer;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

public class RecetaFileStore {
    private final File xmlFile;
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public RecetaFileStore(File xmlFile) {
        this.xmlFile = xmlFile;
        ensureFile();
    }

    private void ensureFile() {
        try {
            File parent = xmlFile.getParentFile();
            if (parent != null) parent.mkdirs();
            if (!xmlFile.exists()) {
                xmlFile.createNewFile();
                try (java.io.FileOutputStream out = new java.io.FileOutputStream(xmlFile)) {
                    out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><recetas></recetas>".getBytes("UTF-8"));
                }
            }
        } catch (Exception ignored) {}
    }

    // Lee el XML y retorna una lista simple de nodos recetas (no mapea a objetos porque para el dashboard basta DOM)
    private Document getDocument() throws Exception {
        if (xmlFile.length() == 0) {
            // archivo vacío: crear raíz
            try (java.io.FileOutputStream out = new java.io.FileOutputStream(xmlFile)) {
                out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><recetas></recetas>".getBytes("UTF-8"));
            }
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(xmlFile);
    }

    // Lista de códigos únicos de medicamentos que aparecen en recetas
    public List<String> getMedicamentosDisponibles() {
        Set<String> set = new TreeSet<>();
        try {
            Document doc = getDocument();
            NodeList meds = doc.getElementsByTagName("medicamento");
            for (int i = 0; i < meds.getLength(); i++) {
                Element el = (Element) meds.item(i);
                String code = el.getAttribute("codigo");
                if (code != null && !code.trim().isEmpty()) set.add(code);
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        List<String> out = new ArrayList<>(set);
        out.add(0, "Todos");
        return out;
    }

    // Cantidad (sumada) por mes (YearMonth) para un código de medicamento o "Todos"
    public Map<YearMonth, Integer> getPrescripcionesPorMes(String medicamentoCodigo, YearMonth from, YearMonth to) {
        Map<YearMonth, Integer> counts = new TreeMap<>();
        YearMonth cur = from;
        while (!cur.isAfter(to)) { counts.put(cur, 0); cur = cur.plusMonths(1); }

        try {
            Document doc = getDocument();
            NodeList recetas = doc.getElementsByTagName("receta");
            for (int i = 0; i < recetas.getLength(); i++) {
                Element receta = (Element) recetas.item(i);
                String fechaStr = receta.getAttribute("fecha");
                if (fechaStr == null || fechaStr.trim().isEmpty()) continue;
                LocalDate fecha = LocalDate.parse(fechaStr, df);
                YearMonth ym = YearMonth.from(fecha);
                if (ym.isBefore(from) || ym.isAfter(to)) continue;

                NodeList medicamentos = receta.getElementsByTagName("medicamento");
                for (int j = 0; j < medicamentos.getLength(); j++) {
                    Element m = (Element) medicamentos.item(j);
                    String codigo = m.getAttribute("codigo");
                    String cantS = m.getAttribute("cantidad");
                    int cantidad = 1;
                    try { if (cantS != null && !cantS.isEmpty()) cantidad = Integer.parseInt(cantS); } catch (Exception ex){}
                    if ("Todos".equals(medicamentoCodigo) || medicamentoCodigo == null || medicamentoCodigo.equals("Todos") || medicamentoCodigo.equals(codigo)) {
                        counts.put(ym, counts.getOrDefault(ym, 0) + cantidad);
                    }
                }
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        return counts;
    }

    // Cuenta recetas por estado (p.ej. confeccionada, pendiente, anulada)
    public Map<String, Integer> getRecetasPorEstado() {
        Map<String, Integer> map = new TreeMap<>();
        try {
            Document doc = getDocument();
            NodeList recetas = doc.getElementsByTagName("receta");
            for (int i = 0; i < recetas.getLength(); i++) {
                Element r = (Element) recetas.item(i);
                String estado = r.getAttribute("estado");
                if (estado == null || estado.trim().isEmpty()) estado = "desconocido";
                map.put(estado, map.getOrDefault(estado, 0) + 1);
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        return map;
    }
}
