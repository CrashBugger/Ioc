package ioc.xml.util.impl;

import ioc.xml.util.DataElement;

public class ValueElement implements DataElement {
    private final Object value;

    public ValueElement(Object value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "value";
    }

    @Override
    public Object getValue() {
        return this.value;
    }
}
