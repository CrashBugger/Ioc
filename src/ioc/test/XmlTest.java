package ioc.test;

import ioc.context.*;
import ioc.context.exp.BeanCreateException;
import ioc.context.exp.PropertyException;
import ioc.context.impl.BeanCreatorImpl;
import ioc.context.impl.PropertyHandlerImpl;
import ioc.context.impl.XmlApplicationContext;
import ioc.xml.util.ElementLoader;
import ioc.xml.util.impl.ElementLoaderImpl;
import ioc.xml.util.impl.XmlDocumentHolder;
import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.*;

public class XmlTest {
    @Test
    public void testGetDoc() {
        XmlDocumentHolder holder = new XmlDocumentHolder();
        String filepath = "D:\\Develop\\IdeaProjects\\IoC\\src\\ioc\\resources\\XmlHolder.xml";
        //获得documentHolder对象
        Document doc = holder.getDocument(filepath);
        Element root = doc.getRootElement();
        System.out.println(root.getName());
        //重新获取一次,判断两个document对象是否一致
        Document doc2 = holder.getDocument(filepath);
        System.out.println(doc);
        System.out.println(doc2);
    }

    @Test
    public void testGetElements() {
        XmlDocumentHolder holder = new XmlDocumentHolder();
        String filePath = "D:\\Develop\\IdeaProjects\\IoC\\src\\ioc\\resources\\ElementLoader.xml";
        Document doc = holder.getDocument(filePath);
        ElementLoader elementLoader = new ElementLoaderImpl();
        elementLoader.addElements(doc);
        //得到bean  id为test1的Element对象
        Element e = elementLoader.getElement("test1");
        System.out.println(e.attribute("class"));
    }

    @Test
    public void testGetBean1() {
        //无参数构造实例
        String className = "ioc.test.TestObject1";
        BeanCreator beanCreator = new BeanCreatorImpl();
        try {
            TestObject1 obj = (TestObject1) beanCreator.createBeanUseDefaultConstruct(className);
            System.out.println(obj);
            System.out.println(obj.getName());
            System.out.println(obj.getValue());
        } catch (BeanCreateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetBean2() {
        //有参数构造实例
        String className = "ioc.test.TestObject1";
        List<Object> args = new ArrayList<>();
        args.add("name");
        args.add("value");
        BeanCreator beanCreator = new BeanCreatorImpl();
        try {
            TestObject1 obj = (TestObject1) beanCreator.createBeanUseDefineConstruct(className, args);
            System.out.println(obj.getValue());
            System.out.println(obj.getName());
        } catch (BeanCreateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetter() {
        //测试setter方法
        TestObject2 obj = new TestObject2();
        Map<String, Object> properties = new HashMap<>();
        properties.put("age", new Integer(12));
        properties.put("name", "zhangsan");
        properties.put("testObject1", new TestObject1("name", "value"));
        try {
            Object o = new PropertyHandlerImpl().setProperties(obj, properties);
            TestObject2 newO = (TestObject2) o;
            System.out.println(newO.getAge());
            System.out.println(newO.getName());
            System.out.println(newO.getTestObject1());
        } catch (PropertyException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAutowireGetSetMap() {
        TestObject1 obj = new TestObject1("name", "value");
        Map<String, Method> result = new PropertyHandlerImpl().getSetterMethodsMap(obj);
        System.out.println(result.get("name"));
        System.out.println(result.get("value"));
        try {
            new PropertyHandlerImpl().executeMethod(obj, "newName", result.get("name"));
        } catch (BeanCreateException e) {
            throw new RuntimeException(e);
        }
        System.out.println(obj.getName());
    }

    @Test
    public void test() {
        ApplicationContext applicationContext = new
                XmlApplicationContext(new String[]{"ioc/resources/test.xml"});
        //TestObject1 test1 = (TestObject1) applicationContext.getBean("test1");
        //System.out.println(test1.getName());
        TestObject1 obj1 = (TestObject1) applicationContext.getBean("test3");
        System.out.println(obj1 + "--------------");
        TestObject1 obj2 = (TestObject1) applicationContext.getBean("test3");
        System.out.println(obj2 + "---------------");
    }

    @Test
    //构造注入
    public void test2() {
        ApplicationContext ioc = new XmlApplicationContext(new String[]{"ioc/resources/test.xml"});
        TestObject2 obj2 = (TestObject2) ioc.getBean("test2");
        System.out.println(obj2.getAge());
        System.out.println(obj2.getName());
        System.out.println(obj2.getTestObject1());
        System.out.println(ioc.getBean("test1"));
    }

    @Test
    public void test4() {
        ApplicationContext ioc = new XmlApplicationContext(new String[]{"ioc/resources/test.xml"});
        Object object1 = ioc.getBean("object1");
        System.out.println("--------------------");
        System.out.println(object1);
        TestObject3 test4 = (TestObject3) ioc.getBean("test4");
        System.out.println(test4.getObject1());
    }

    @Test
    public void tem() {
        System.out.println(Boolean.TYPE);
    }
}
