package ioc.xml.util.impl;

import ioc.xml.util.Autowire;

public class NoAutowire implements Autowire {

    private String value;

    public NoAutowire(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
