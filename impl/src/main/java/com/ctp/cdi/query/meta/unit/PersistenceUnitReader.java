package com.ctp.cdi.query.meta.unit;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PersistenceUnitReader extends DescriptorReader {
    
    public List<PersistenceUnit> readAll() throws IOException {
        List<PersistenceUnit> result = new LinkedList<PersistenceUnit>();
        List<Descriptor> persistenceXmls = readAllFromClassPath(PersistenceUnit.RESOURCE_PATH);
        for (Descriptor desc : persistenceXmls) {
            result.addAll(lookupUnits(desc));
        }
        return Collections.unmodifiableList(result);
    }

    private List<PersistenceUnit> lookupUnits(Descriptor descriptor) {
        List<PersistenceUnit> result = new LinkedList<PersistenceUnit>();
        NodeList list = descriptor.getDocument().getDocumentElement().getElementsByTagName("persistence-unit");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            String unitName = extractUnitName(node);
            String baseUrl = extractBaseUrl(descriptor.getUrl(), PersistenceUnit.RESOURCE_PATH);
            List<EntityMapping> entities = extractMappings((Element) node, baseUrl);
            result.add(new PersistenceUnit(unitName, entities));
        }
        return result;
    }

    private List<EntityMapping> extractMappings(Element element, String baseUrl) {
        try {
            EntityDescriptorReader reader = new EntityDescriptorReader();
            List<EntityMapping> result = new LinkedList<EntityMapping>();
            NodeList list = element.getElementsByTagName("mapping-file");
            for (int i = 0; i < list.getLength(); i++) {
                result.addAll(reader.readAll(baseUrl, list.item(i).getTextContent()));
            }
            result.addAll(reader.readDefaultOrm(baseUrl));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed initializing mapping files", e);
        }
    }

    private String extractUnitName(Node node) {
        return node.getAttributes().getNamedItem("name").getTextContent();
    }

}
