package ioc.xml.util.impl;

import ioc.xml.util.Autowire;

public class ByNameAutowire implements Autowire {
    private final String value;

    public ByNameAutowire(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
