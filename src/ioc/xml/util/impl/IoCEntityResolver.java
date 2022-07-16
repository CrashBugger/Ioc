package ioc.xml.util.impl;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public class IoCEntityResolver implements EntityResolver {
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        //先从本地寻找DTD
        if ("http://www.crazyit.org/beans.dtd".equals(systemId)) {
            InputStream inputStream = IoCEntityResolver.class.getResourceAsStream("beans.dtd");
            return new InputSource(inputStream);
        } else {
            return null;
        }
    }
}
