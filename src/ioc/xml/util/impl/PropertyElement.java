package ioc.xml.util.impl;

import ioc.xml.util.DataElement;

public class PropertyElement {
    //Property元素的name值
    private String name;
    //Property元素下的ref或者value对象
    private DataElement dataElement;

    public PropertyElement(String name, DataElement dataElement) {
        this.name = name;
        this.dataElement = dataElement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataElement getDataElement() {
        return dataElement;
    }

    public void setDataElement(DataElement dataElement) {
        this.dataElement = dataElement;
    }
}
