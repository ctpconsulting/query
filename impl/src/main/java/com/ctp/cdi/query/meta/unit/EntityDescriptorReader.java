package com.ctp.cdi.query.meta.unit;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EntityDescriptorReader extends DescriptorReader {
    
    public List<EntityMapping> readAll(String baseUrl, String resource) throws IOException {
        return readFromFile(read(baseUrl, resource));
    }

    public List<EntityMapping> readDefaultOrm(String baseUrl) throws IOException {
        try {
            Descriptor desc = read(baseUrl, PersistenceUnit.DEFAULT_ORM_PATH);
            return extractMapping(desc.getDocument());
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    private List<EntityMapping> readFromFile(Descriptor descriptor) throws IOException {
        return extractMapping(descriptor.getDocument());
    }

    private List<EntityMapping> extractMapping(Document doc) {
        return readFromDocument(doc);
    }
    
    public List<EntityMapping> readFromDocument(Document doc) {
        List<EntityMapping> result = new LinkedList<EntityMapping>();
        String packageName = extractNodeContent(doc.getDocumentElement(), "package");
        NodeList mappings = doc.getElementsByTagName("entity");
        for (int i = 0; i < mappings.getLength(); i++) {
            String name = extractAttribute(mappings.item(i), "name");
            String className = extractAttribute(mappings.item(i), "class");
            String idClass = extractNodeAttribute((Element) mappings.item(i), "id-class", "class");
            String id = extractNodeAttribute((Element) mappings.item(i), "id", "name");
            String embeddedId = extractNodeAttribute((Element) mappings.item(i), "embedded-id", "name");
            result.add(new EntityMapping(name, packageName, className, idClass, id != null ? id : embeddedId));
        }
        return result;
    }
    
    private String extractNodeAttribute(Element element, String childName, String attribute) {
        NodeList list = element.getElementsByTagName(childName);
        if (list.getLength() == 0) {
            return null;
        }
        return extractAttribute(list.item(0), attribute);
    }

    private String extractAttribute(Node item, String name) {
        Node node = item.getAttributes().getNamedItem(name);
        if (node != null) {
            return node.getTextContent();
        }
        return null;
    }

    private String extractNodeContent(Element element, String name) {
        NodeList list = element.getElementsByTagName(name);
        if (list.getLength() == 0) {
            return null;
        }
        return list.item(0).getTextContent();
    }
}
