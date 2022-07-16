package ioc.test;

public class TestObject3 {
    private String name;
    private int age;
    private TestObject1 object1;

    public TestObject3() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public TestObject1 getObject1() {
        return object1;
    }

    public void setObject1(TestObject1 object1) {
        this.object1 = object1;
    }
}
