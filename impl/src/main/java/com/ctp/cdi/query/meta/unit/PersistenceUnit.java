package com.ctp.cdi.query.meta.unit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

class PersistenceUnit {
    
    public static final String RESOURCE_PATH = "META-INF/persistence.xml";
    
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    
    private final String unitName;
    private final List<MappingFile> mappingFiles;

    private PersistenceUnit(String unitName, List<MappingFile> mappingFiles) {
        this.unitName = unitName;
        this.mappingFiles = mappingFiles;
    }

    public static List<PersistenceUnit> readFromFile(URL fileUrl) throws IOException {
        InputStream stream = fileUrl.openStream();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(stream));
            String baseUrl = extractBaseUrl(fileUrl);
            return lookupUnits(doc, baseUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed reading XML document", e);
        } finally {
            stream.close();
        }
    }
    
    private static String extractBaseUrl(URL fileUrl) {
        String file = fileUrl.getFile();
        return fileUrl.getProtocol() + "://" + file.substring(0, file.length() - RESOURCE_PATH.length());
    }

    private static List<PersistenceUnit> lookupUnits(Document doc, String baseUrl) {
        List<PersistenceUnit> result = new LinkedList<PersistenceUnit>();        
        NodeList list = doc.getDocumentElement().getElementsByTagName("persistence-unit");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            String unitName = extractUnitName(node);
            List<MappingFile> mappingFiles = extractMappings((Element) node, baseUrl);
            result.add(new PersistenceUnit(unitName, mappingFiles));
        }
        return result;
    }

    private static List<MappingFile> extractMappings(Element element, String baseUrl) {
        try {
            List<MappingFile> result = new LinkedList<MappingFile>();
            NodeList list = element.getElementsByTagName("mapping-file");
            for (int i = 0; i < list.getLength(); i++) {
                result.add(MappingFile.readFromFile(new URL(baseUrl + list.item(i).getTextContent())));
            }
            MappingFile defaultOrm = MappingFile.defaultOrm(baseUrl);
            if (defaultOrm != null)
                result.add(defaultOrm);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed initializing mapping files", e);
        }
    }

    private static String extractUnitName(Node node) {
        return node.getAttributes().getNamedItem("name").getTextContent();
    }
    
    public EntityMapping find(Class<?> entityClass) {
        for (MappingFile file : mappingFiles) {
            EntityMapping mapping = file.find(entityClass);
            if (mapping != null)
                return mapping;
        }
        return null;
    }

    public String getUnitName() {
        return unitName;
    }

    public List<MappingFile> getMappingFiles() {
        return mappingFiles;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PersistenceUnit [unitName=").append(unitName)
               .append(", mappingFiles=").append(mappingFiles).append("]");
        return builder.toString();
    }

}
