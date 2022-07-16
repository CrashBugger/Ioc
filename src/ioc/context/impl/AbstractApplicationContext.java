package ioc.context.impl;

import ioc.context.ApplicationContext;
import ioc.context.BeanCreator;
import ioc.context.PropertyHandler;
import ioc.context.exp.BeanCreateException;
import ioc.context.exp.PropertyException;
import ioc.xml.util.*;
import ioc.xml.util.impl.*;
import org.dom4j.Document;
import org.dom4j.Element;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 抽象类，不可被实例化
 */
public abstract class AbstractApplicationContext implements ApplicationContext {
    //元素加载对象
    protected ElementLoader elementLoader = new ElementLoaderImpl();
    //文档持有对象
    protected DocumentHolder documentHolder = new XmlDocumentHolder();
    //缓存的beans
    protected Map<String, Object> beans = new HashMap<>();
    //属性处理类
    protected PropertyHandler propertyHandler = new PropertyHandlerImpl();
    //创建bean对象的接口
    protected BeanCreator beanCreator = new BeanCreatorImpl();
    //element元素读取类
    protected ElementReader elementReader = new ElementReaderImpl();

    /**
     * 读取xml文件,将各个元素缓存
     */
    protected void setUpElements(String[] xmlPaths) {
        URL classPathUrl = AbstractApplicationContext.class.getClassLoader().getResource(".");
        String classPath = URLDecoder.decode(classPathUrl.getPath(), StandardCharsets.UTF_8);
        for (String path : xmlPaths) {
            Document doc = documentHolder.getDocument(classPath + path);
            elementLoader.addElements(doc);
        }
    }

    /**
     * 处理bean,如果bean是单态的，则加到map中，非单态，创建并返回
     */
    protected Object handleSingleton(String id) throws BeanCreateException {
        Object bean = createBean(id);
        if (isSingleton(id)) {
            this.beans.put(id, bean);
        }
        return bean;
    }

    /**
     * 创建一个bean并设置属性，如果找不到该bean对应的配置文件,抛出异常
     */
    protected Object createBean(String id) throws BeanCreateException {
        Element e = elementLoader.getElement(id);
        if (e == null) throw new BeanCreateException("element not found:" + id);
        Object result = instance(e);
        System.out.println("创建bean:" + id);
        System.out.println("该bean对象为:" + result);
        //设置注入,先判断是否自动装配
        Autowire autowire = elementReader.getAutowire(e);
        if (autowire instanceof ByNameAutowire) {
            //使用自动装配
            autowireByName(result);
        } else {
            //设置注入
            setterInject(result, e);
        }
        return result;
    }

    /**
     * 通过property元素设置obj属性
     */
    protected void setterInject(Object obj, Element e) {
        //得到property节点下的值，包括ref的bean属性和value值
        List<PropertyElement> properties = elementReader.getPropertyValue(e);
        //调用本类方法,将properties封装成Map
        Map<String, Object> propertiesMap = getPropertiesArg(properties);
        try {
            //将参数Map对象和bean实例，调用PropertyHandler接口setProperties方法
            propertyHandler.setProperties(obj, propertiesMap);
        } catch (PropertyException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Map<String, Object> getPropertiesArg(List<PropertyElement> properties) {
        HashMap<String, Object> result = new HashMap<>();
        for (PropertyElement p : properties) {
            DataElement dataElement = p.getDataElement();
            if (dataElement instanceof RefElement) {
                result.put(p.getName(), this.getBean((String) dataElement.getValue()));
            } else {
                result.put(p.getName(), dataElement.getValue());
            }
        }
        return result;
    }

    /**
     * 自动装配一个对象，得到该bean的所有setter方法，再从容器中找到对应的bean
     * 例如，setSchool(School) 查找名字为school的bean,再调用setSchool方法设入对象中
     */
    protected void autowireByName(Object obj) {
        Map<String, Method> methods = propertyHandler.getSetterMethodsMap(obj);
        for (String s : methods.keySet()) {
            Object bean = this.getBean(s);
            Method method = methods.get(s);
            try {
                propertyHandler.executeMethod(obj, bean, method);
            } catch (BeanCreateException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 通过bean的id,得到bean的实例
     */
    public Object getBean(String id) {
        Object bean = this.beans.get(id);
        //如果获取不到该bean,则创建
        if (bean == null) {
            //判断处理单态或者非单态的bean
            try {
                bean = handleSingleton(id);
            } catch (BeanCreateException e) {
                throw new RuntimeException(e);
            }
        }
        return bean;
    }

    /**
     * 实例化一个bean,如果该bean有constructor-arg元素，那么使用带参数的构造器
     */
    protected Object instance(Element e) {
        String className = e.attributeValue("class");
        List<Element> constructorElements = elementReader.getConstructorElements(e);
        try {
            if (constructorElements.size() == 0)
                //无constructor-arg元素
                return beanCreator.createBeanUseDefaultConstruct(className);
            else {
                List<Object> args = getConstructorArgs(e);
                return beanCreator.createBeanUseDefineConstruct(className, args);
            }
        } catch (BeanCreateException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected List<Object> getConstructorArgs(Element e) {
        List<DataElement> datas = elementReader.getConstructorValue(e);
        List<Object> result = new ArrayList<>();
        for (DataElement data : datas) {
            if (data instanceof ValueElement) {
                result.add(data.getValue());
            } else {
                //如果是ref节点，直接调用getBean方法，找不到则创建
                result.add(getBean((String) data.getValue()));
            }
        }
        return result;
    }

    @Override
    public boolean containsBean(String id) {
        //调用elementloader对象，根据id得到element对象
        Element e = elementLoader.getElement(id);
        return e != null;
    }

    @Override
    public boolean isSingleton(String id) {
        //使用ElementLoader对象，根据id得到对应的Element对象
        Element e = elementLoader.getElement(id);
        //使用elementreader判断是否为单态
        return elementReader.isSingleton(e);
    }

    @Override
    public Object getBeanIgnoreCreate(String id) {
        return this.beans.get(id);
    }

    /**
     * 创建所有的bean实例，延迟加载的不创建
     */
    protected void createBeans() {
        Collection<Element> elements = elementLoader.getElements();
        for (Element e : elements) {
            boolean lazy = elementReader.isLazy(e);
            if (!lazy && this.getBean(e.attributeValue("id")) == null) {
                //处理单态bean，如果是单态的,加到缓存中，非单态则不创建
                try {
                    handleSingleton(e.attributeValue("id"));
                } catch (BeanCreateException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}

