package ioc.xml.util;

import org.dom4j.Document;

public interface DocumentHolder {
    /**
     * @param filePath 根据文件路径
     * @return 返回文档对象
     */
    Document getDocument(String filePath);
}
