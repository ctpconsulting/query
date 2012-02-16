package com.ctp.cdi.query.meta.unit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

class MappingFile {
    
    public static final String RESOURCE_PATH = "META-INF/orm.xml";
    
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    
    private final List<EntityMapping> classes;
    
    private MappingFile(List<EntityMapping> classes) {
        this.classes = classes;
    }

    public static MappingFile readFromFile(URL fileUrl) throws IOException {
        InputStream stream = fileUrl.openStream();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(stream));
            return extractMapping(doc);
        } catch (Exception e) {
            throw new RuntimeException("Failed reading XML document", e);
        } finally {
            stream.close();
        }
    }
    
    public static MappingFile defaultOrm(String baseUrl) throws IOException {
        File file = new File(baseUrl + RESOURCE_PATH);
        if (file.exists())
            return readFromFile(file.toURI().toURL());
        return null;
    }

    private static MappingFile extractMapping(Document doc) {
        List<EntityMapping> mappings = EntityMapping.readFromDocument(doc);
        return new MappingFile(mappings);
    }

    public EntityMapping find(Class<?> entityClass) {
        for (EntityMapping entity : classes) {
            if (entity.is(entityClass))
                return entity;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MappingFile [classes=").append(classes).append("]");
        return builder.toString();
    }

    
}
