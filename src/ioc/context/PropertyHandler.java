package ioc.context;

import ioc.context.exp.BeanCreateException;
import ioc.context.exp.PropertyException;

import java.lang.reflect.Method;
import java.util.Map;

public interface PropertyHandler {
    /**
     * 为对象设置属性
     */
    public Object setProperties(Object obj, Map<String, Object> properties) throws PropertyException;

    /**
     * @param object  需要执行方法的对象
     * @param argBean 参数的bean
     * @param method  method  方法对象
     */
    public void executeMethod(Object object, Object argBean, Method method) throws BeanCreateException;

    /**
     * @return 返回一个对象的所有setter方法，封装成map,key为setter方法名不要set
     */
    public Map<String, Method> getSetterMethodsMap(Object obj);
}
