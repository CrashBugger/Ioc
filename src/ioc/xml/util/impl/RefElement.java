package ioc.xml.util.impl;

import ioc.xml.util.DataElement;

public class RefElement implements DataElement {
    private final Object value;

    public RefElement(Object value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "ref";
    }

    @Override
    public Object getValue() {
        return this.value;
    }
}
