package ioc.context;

import ioc.context.exp.BeanCreateException;

import java.util.List;

public interface BeanCreator {
    /**
     * 使用无参构造创造bean实例，不设置任何属性
     */
    public Object createBeanUseDefaultConstruct(String className) throws BeanCreateException;

    /**
     * 使用有参数构造创建bean实例，不设置任何属性
     */
    Object createBeanUseDefineConstruct(String className, List<Object> args) throws BeanCreateException;
}
