package ioc.context.impl;

public class XmlApplicationContext extends AbstractApplicationContext {
    public XmlApplicationContext(String[] xmlPath) {
        //初始化文档元素
        super.setUpElements(xmlPath);
        //初始化容器
        super.createBeans();
    }
}
