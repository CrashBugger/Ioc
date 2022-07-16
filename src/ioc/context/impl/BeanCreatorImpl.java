package ioc.context.impl;

import ioc.context.BeanCreator;
import ioc.context.exp.BeanCreateException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class BeanCreatorImpl implements BeanCreator {
    @Override
    public Object createBeanUseDefaultConstruct(String className) throws BeanCreateException {
        try {
            //使用class.forName通过类名创建class对象
            Class<?> clazz = Class.forName(className);
            //使用newInstance方法创建类的实例
            return clazz.getConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new BeanCreateException("class not found " + e.getMessage());
        } catch (Exception e) {
            throw new BeanCreateException(e.getMessage());
        }
    }

    @Override
    public Object createBeanUseDefineConstruct(String className, List<Object> args) throws BeanCreateException {
        Class[] argsClasses = getArgsClasses(args);
        try {
            Class clazz = Class.forName(className);
            Constructor constructor = findConstructor(clazz, argsClasses);
            return constructor.newInstance(args.toArray());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new BeanCreateException("class not found" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeanCreateException(e.getMessage());
        }
    }

    /**
     * 获得构造器，如果没有找到，不抛出NoSuchMethodException，返回null
     */
    private Constructor getConstructor(Class clazz, Class[] argsClasses) {
        try {
            return clazz.getConstructor(argsClasses);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * 根据类型和参数类型查找构造器
     */
    private Constructor findConstructor(Class clazz, Class[] argsClasses) throws NoSuchMethodException {
        Constructor constructor = getConstructor(clazz, argsClasses);
        //可能传入的构造参数是一个接口，而传入参数是其实现
        if (constructor == null) {
            for (Constructor c : clazz.getConstructors()) {
                Class[] constructorArgsClass = c.getParameterTypes();
                //参数数量与构造器参数数量相同
                if (constructorArgsClass.length == argsClasses.length && isSameArgs(argsClasses, constructorArgsClass)) {
                    return c;
                }
            }
        } else {
            return constructor;
        }
        throw new NoSuchMethodException("could not find any constructor");
    }

    /**
     * 判断两个class数组的类型是否一致
     */
    private boolean isSameArgs(Class[] argsClasses, Class[] constructorArgsClass) {
        for (int i = 0; i < argsClasses.length; i++) {
            try {
                //将参数类型与构造器中的参数强制转换
                //用是否出异常来判断是否参数相同
                argsClasses[i].asSubclass(constructorArgsClass[i]);
            } catch (Exception e) {
                //有一个参数类型不符合，跳出该循环
                return false;
            }
        }
        return true;
    }

    private Class[] getArgsClasses(List<Object> args) {
        List<Class> result = new ArrayList<>();
        for (Object arg : args) {
            result.add(getClass(arg));
        }
        Class[] a = new Class[result.size()];
        return result.toArray(a);
    }

    private Class getClass(Object obj) {
        if (obj instanceof Integer) {
            return Integer.TYPE;
        } else if (obj instanceof Boolean) {
            return Boolean.TYPE;
        } else if (obj instanceof Long) {
            return Long.TYPE;
        } else if (obj instanceof Short) {
            return Short.TYPE;
        } else if (obj instanceof Float) {
            return Float.TYPE;
        } else if (obj instanceof Double) {
            return Double.TYPE;
        } else if (obj instanceof Byte) {
            return Byte.TYPE;
        } else if (obj instanceof Character) {
            return Character.TYPE;
        }
        return obj.getClass();
    }
}
