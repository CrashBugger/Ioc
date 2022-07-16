package ioc.test;

public class TestObject1 {
    private String name;
    private String value;

    public TestObject1(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public TestObject1() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
