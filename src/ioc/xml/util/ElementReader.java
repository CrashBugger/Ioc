package ioc.xml.util;

import ioc.xml.util.impl.PropertyElement;
import org.dom4j.Element;

import java.util.List;

public interface ElementReader {
    /**
     * @return 判断一个bean的元素是否需要延迟加载
     */
    boolean isLazy(Element element);

    /**
     * @param element bean元素
     * @return 获得一个bean元素下的constructor-arg
     */
    List<Element> getConstructorElements(Element element);

    /**
     * @return 得到元素属性name的值
     */
    String getAttribute(Element element, String name);

    /**
     * @return 判断element 是否为单态
     */
    boolean isSingleton(Element element);

    /**
     * @return 获得一个bean元素下的property元素
     */
    List<Element> getPropertyElements(Element element);

    /**
     * @return 返回对应的Autowire对象
     */
    Autowire getAutowire(Element element);

    /**
     * @return 获取bean元素下所有的constructor-arg的值(包括value和ref)
     */
    List<DataElement> getConstructorValue(Element element);

    /**
     * @return 获取bean元素下所有property元素的值(value和ref)
     */
    List<PropertyElement> getPropertyValue(Element element);
}
