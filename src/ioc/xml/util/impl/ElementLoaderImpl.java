package ioc.xml.util.impl;

import ioc.xml.util.ElementLoader;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementLoaderImpl implements ElementLoader {
    //提供一个Map保存,Map的key为bean元素的id,value为bean对应的Element
    private Map<String, Element> elements = new HashMap<>();

    @Override
    public void addElements(Document doc) {
        //读取根节点beans,在得到所有bean节点
        List<Element> eles = doc.getRootElement().elements();
        for (Element ele : eles) {
            //得到bean的id属性
            String id = ele.attributeValue("id");
            //添加到map中
            elements.put(id, ele);
        }
    }

    @Override
    public Element getElement(String id) {
        return elements.get(id);
    }

    @Override
    public Collection<Element> getElements() {
        return this.elements.values();
    }
}
