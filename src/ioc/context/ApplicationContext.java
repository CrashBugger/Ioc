package ioc.context;

/**
 * IOC容器
 */
public interface ApplicationContext {
    /**
     * 根据id找到bean
     *
     * @param id 配置文件的bean的id
     * @return 该bean的实例
     */
    Object getBean(String id);

    /**
     * ioc 容器中是否包含id为参数的bean
     *
     * @return true 容器中包含这个bean
     */
    boolean containsBean(String id);

    /**
     * 判断一个bean是否为单态
     *
     * @param id bean节点的id
     * @return true 为单态
     */
    boolean isSingleton(String id);

    /**
     * 获取bean,找不到返回null
     *
     * @param id bean节点的id
     * @return 找到的bean的实例
     */
    Object getBeanIgnoreCreate(String id);
}
