package com.ctp.cdi.query.meta.unit;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.jboss.solder.properties.query.NamedPropertyCriteria;
import org.jboss.solder.properties.query.PropertyQueries;
import org.jboss.solder.properties.query.PropertyQuery;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class EntityMapping {

    private final String name;
    private final String packageName;
    private final String className;
    private final String idClass;
    private final String id;
    
    public static List<EntityMapping> readFromDocument(Document doc) {
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
    
    private static String extractNodeAttribute(Element element, String childName, String attribute) {
        NodeList list = element.getElementsByTagName(childName);
        if (list.getLength() == 0)
            return null;
        return extractAttribute(list.item(0), attribute);
    }

    private static String extractAttribute(Node item, String name) {
        Node node = item.getAttributes().getNamedItem(name);
        if (node != null)
            return node.getTextContent();
        return null;
    }

    private static String extractNodeContent(Element element, String name) {
        NodeList list = element.getElementsByTagName(name);
        if (list.getLength() == 0)
            return null;
        return list.item(0).getTextContent();
    }
    
    private EntityMapping(String name, String packageName, String className, String idClass, String id) {
        this.name = name;
        this.packageName = packageName;
        this.className = className;
        this.idClass = idClass;
        this.id = id;
    }
    
    public boolean is(Class<?> entityClass) {
        return className().equals(entityClass.getName());
    }
    
    public Class<?> entityClass() {
        try {
            String name = className();
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Can't create class " + className(), e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public Class<? extends Serializable> idClass() {
        try {
            return (Class<? extends Serializable>) (idClass != null ? Class.forName(idClass) : lookupIdClass());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Failed to get ID class", e);
        }
    }

    public String getIdClass() {
        return idClass;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EntityMapping [packageName=").append(packageName)
               .append(", name=").append(name)
               .append(", className=").append(className)
               .append(", idClass=").append(idClass)
               .append(", id=").append(id).append("]");
        return builder.toString();
    }
    
    private Class<?> lookupIdClass() {
        PropertyQuery<Serializable> query = PropertyQueries.<Serializable>createQuery(entityClass())
                .addCriteria(new NamedPropertyCriteria(id));
        return query.getFirstResult().getJavaClass();
    }
    
    private String className() {
        return packageName != null ? packageName + "." + className : className;
    }
    
}
