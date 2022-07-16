package ioc.context.impl;

import ioc.context.PropertyHandler;
import ioc.context.exp.BeanCreateException;
import ioc.context.exp.PropertyException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class PropertyHandlerImpl implements PropertyHandler {
    @Override
    public Object setProperties(Object obj, Map<String, Object> properties) throws PropertyException {
        Class clazz = obj.getClass();
        try {
            for (String key : properties.keySet()) {
                String setterName = getSetterMethodName(key);
                Class argClass = getClass(properties.get(key));
                Method setterMethod = getSetterMethod(clazz, setterName, argClass);
                setterMethod.invoke(obj, properties.get(key));
            }
            return obj;
        } catch (NoSuchMethodException e) {
            throw new PropertyException("setter method not found " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new PropertyException("wrong argument not found " + e.getMessage());
        } catch (InvocationTargetException e) {
            throw new PropertyException(e.getMessage());
        }
    }

    @Override
    public void executeMethod(Object object, Object argBean, Method method) throws BeanCreateException {
        try {
            //获取方法参数类型
            Class[] parameterTypes = method.getParameterTypes();
            //如果参数不为1,则不执行该方法
            if (parameterTypes.length == 1 && isMethodArgs(method, parameterTypes[0])) {
                //调用isMethodArgs方法判断参数类型是否一致
                method.invoke(object, argBean);
            }
        } catch (Exception e) {
            throw new BeanCreateException("autowire exception " + e.getMessage());
        }
    }

    @Override
    public Map<String, Method> getSetterMethodsMap(Object obj) {
        List<Method> methods = getSetterMethodsList(obj);
        Map<String, Method> result = new HashMap<>();
        for (Method m : methods) {
            String propertyName = getMethodNameWithoutSet(m.getName());
            result.put(propertyName, m);
        }
        return result;
    }

    /**
     * setter方法还原
     */
    private String getMethodNameWithoutSet(String name) {
        String propertyName = name.replaceFirst("set", "");
        String first = propertyName.substring(0, 1);
        String newFirst = first.toLowerCase(Locale.ROOT);
        return propertyName.replaceFirst(first, newFirst);
    }

    /**
     * 获取所有的set方法
     */
    private List<Method> getSetterMethodsList(Object obj) {
        Method[] methods = obj.getClass().getMethods();
        List<Method> result = new ArrayList<>();
        for (Method m : methods) {
            if (m.getName().startsWith("set")) {
                result.add(m);
            }
        }
        return result;
    }


    private Method getSetterMethod(Class objClass, String methodName, Class argClass) throws NoSuchMethodException {
        //使用原类型获得方法，如果没有找到该方法，则得到null
        Method argClassMethod = getMethod(objClass, methodName, argClass);
        //如果找不到该方法，则找该类型所实现的接口
        if (argClassMethod == null) {
            List<Method> methods = getMethods(objClass, methodName);
            Method method = findMethod(argClass, methods);
            if (method == null) {
                throw new NoSuchMethodException(methodName);
            }
            return method;
        } else return argClassMethod;
    }

    /**
     * 遍历所有方法，判断方法中的参数与argClass是否为同一类型
     */
    private Method findMethod(Class argClass, List<Method> methods) {
        for (Method m : methods) {
            if (isMethodArgs(m, argClass)) {
                return m;
            }
        }
        return null;
    }

    /**
     * 判断一个方法的参数类型是否与argClass一样，有可能argClass是该方法的实现类
     */
    private boolean isMethodArgs(Method m, Class argClass) {
        Class<?>[] c = m.getParameterTypes();
        if (c.length == 1) {
            try {
                argClass.asSubclass(c[0]);
                return true;
            } catch (ClassCastException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 得到所有methodName并且只有一个参数的方法
     */
    private List<Method> getMethods(Class objClass, String methodName) {
        List<Method> result = new ArrayList<>();
        for (Method m : objClass.getMethods()) {
            if (m.getName().equals(methodName) && m.getParameterTypes().length == 1) result.add(m);
        }
        return result;
    }

    private Method getMethod(Class clazz, String setterName, Class argClass) {
        try {
            return clazz.getMethod(setterName, argClass);
        } catch (NoSuchMethodException e) {
            return null;
        }
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

    private String getSetterMethodName(String propertyName) {
        return "set" + upperCaseFirstWord(propertyName);
    }

    private String upperCaseFirstWord(String s) {
        String firstWord = s.substring(0, 1);
        String upperCaseWord = firstWord.toUpperCase();
        return s.replaceFirst(firstWord, upperCaseWord);
    }
}
