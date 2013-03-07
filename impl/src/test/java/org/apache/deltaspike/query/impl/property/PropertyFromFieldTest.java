package org.apache.deltaspike.query.impl.property;

import java.lang.reflect.Field;

import org.apache.deltaspike.query.impl.property.FieldProperty;
import org.apache.deltaspike.query.impl.property.Properties;
import org.junit.Test;

/**
 * Verify that only valid properties are permitted, as per the JavaBean specification.
 *
 * @author Vivian Steller
 */
public class PropertyFromFieldTest {

    @Test
    public void testAccessingPrimitiveTypedFieldProperty() throws Exception {
        final Field field = ClassToIntrospect.class.getField("primitiveProperty");

        FieldProperty<Object> propertyUT = Properties.createProperty(field);
        propertyUT.getValue(new ClassToIntrospect());
    }

}
