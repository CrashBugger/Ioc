package ioc.test;

public class TestObject2 {
    private String name;
    private int age;
    private TestObject1 testObject1;

    public TestObject2(String name, int age, TestObject1 testObject1) {
        this.name = name;
        this.age = age;
        this.testObject1 = testObject1;
    }

    public TestObject1 getTestObject1() {
        return testObject1;
    }

    public void setTestObject1(TestObject1 testObject1) {
        this.testObject1 = testObject1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public TestObject2() {
    }

    public TestObject2(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
