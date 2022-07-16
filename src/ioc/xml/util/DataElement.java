package ioc.xml.util;

public interface DataElement {
    /**
     * 返回的数据类型(ref/value)
     */
    public String getType();

    /**
     * 返回数据的值
     */
    public Object getValue();
}
