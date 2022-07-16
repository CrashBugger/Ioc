package ioc.xml.util.impl;

import ioc.xml.util.DocumentHolder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XmlDocumentHolder implements DocumentHolder {
    private Map<String, Document> docs = new HashMap<>();

    @Override
    public Document getDocument(String filePath) {
        this.docs.computeIfAbsent(filePath, key -> {
            try {
                return readDocument(key);
            } catch (DocumentException e) {
                e.printStackTrace();
                return null;
            }
        });
        return this.docs.get(filePath);
    }

    /**
     * 根据文件路径读取document
     */
    private Document readDocument(String filePath) throws DocumentException {
        try {
            //读取xml文件,用saxReader
            SAXReader reader = new SAXReader(true);
            //使用自己的EntityResolver
            reader.setEntityResolver(new IoCEntityResolver());
            File xmlFile = new File(filePath);
            //读取文件并返回xml对象
            return reader.read(xmlFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DocumentException(e.getMessage());
        }
    }
}
