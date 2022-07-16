package ioc.xml.util;

import org.dom4j.Document;
import org.dom4j.Element;

import java.util.Collection;

public interface ElementLoader {
    //加入一份doc所有的Element
    public void addElements(Document doc);

    //根据元素id获得Element对象
    public Element getElement(String id);

    //返回全部的Element
    public Collection<Element> getElements();
}
