package ioc.xml.util.impl;

import ioc.xml.util.Autowire;
import ioc.xml.util.DataElement;
import ioc.xml.util.ElementLoader;
import ioc.xml.util.ElementReader;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class ElementReaderImpl implements ElementReader {

    @Override
    public boolean isLazy(Element element) {
        //获得isLazy的值
        String lazy = getAttribute(element, "lazy-init");
        //获得父节点的beans的lazy-init的值
        Element parent = element.getParent();
        Boolean parentLazy = new Boolean(getAttribute(parent, "lazy-init"));
        if (parentLazy) {
            //bean节点需要延迟加载
            return !"false".equals(lazy);
        } else {
            //根节点不需要延迟加载
            return "true".equals(lazy);
        }
    }

    @Override
    public List<Element> getConstructorElements(Element element) {
        //得到bean节点下的所有节点
        List<Element> children = element.elements();
        ArrayList<Element> result = new ArrayList<>();
        for (Element e : children) {
            //如果是constructor-arg的节点
            if ("constructor-arg".equals(e.getName())) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public String getAttribute(Element element, String name) {
        return element.attributeValue(name);
    }

    @Override
    public boolean isSingleton(Element element) {
        return Boolean.parseBoolean(getAttribute(element, "singleton"));
    }

    @Override
    public List<Element> getPropertyElements(Element element) {
        List<Element> children = element.elements();
        ArrayList<Element> result = new ArrayList<>();
        for (Element e : children) {
            if ("property".equals(e.getName())) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public Autowire getAutowire(Element element) {
        String value = this.getAttribute(element, "autowire");
        String parentValue = this.getAttribute(element.getParent(), "default-auto-wire");
        if ("no".equals(parentValue)) {
            //根节点不需要自动装配
            if ("byName".equals(value))
                return new ByNameAutowire(value);
            return new NoAutowire(value);
        } else if ("byName".equals(parentValue)) {
            if ("no".equals(value))
                return new NoAutowire(value);
            return new ByNameAutowire(value);
        }
        return new NoAutowire(value);
    }

    @Override
    public List<DataElement> getConstructorValue(Element element) {
        //调用本类的getConstructorElements方法取得全部constructor-arg元素
        List<Element> cons = getConstructorElements(element);
        List<DataElement> result = new ArrayList<>();
        for (Element e : cons) {
            //获得constructor-arg下ref或者value元素(仅一个)
            List<Element> els = e.elements();
            //将element封装成DataElement对象
            DataElement dataElement = this.getDataElement(els.get(0));
            result.add(dataElement);
        }
        return result;
    }

    /**
     * @param dataElement ref或Element节点
     * @return 判断元素类型是ref还是value, 并将其封装为DataElement
     */
    private DataElement getDataElement(Element dataElement) {
        String name = dataElement.getName();
        if ("value".equals(name)) {
            String classTypeName = dataElement.attributeValue("type");
            String data = dataElement.getText();
            return new ValueElement(this.getValue(classTypeName, data));
        } else if ("ref".equals(name)) {
            return new RefElement(this.getAttribute(dataElement, "bean"));
        }
        return null;
    }

    private Object getValue(String classTypeName, String data) {
        if (isType(classTypeName, "Integer")) {
            return Integer.parseInt(data);
        } else if (isType(classTypeName, "Boolean")) {
            return Boolean.parseBoolean(data);
        } else if (isType(classTypeName, "Long")) {
            return Long.parseLong(data);
        } else {
            return data;
        }
    }

    private boolean isType(String classTypeName, String type) {
        return classTypeName.contains(type);
    }

    @Override
    public List<PropertyElement> getPropertyValue(Element element) {
        //调用本类的getPropertyElements方法取得所有property节点
        List<Element> properties = getPropertyElements(element);
        ArrayList<PropertyElement> result = new ArrayList<>();
        for (Element e : properties) {
            //获得property下的ref或value元素(只有一个)
            List<Element> elements = e.elements();
            //将节点封装
            DataElement dataElement = getDataElement(elements.get(0));
            //得到property节点的name属性
            String propertyNameAt = this.getAttribute(e, "name");
            //将数据值和property元素的name属性封装成PropertyElement对象
            PropertyElement pE = new PropertyElement(propertyNameAt, dataElement);
            result.add(pE);
        }
        return result;
    }
}
