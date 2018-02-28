package cn.mdni.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description: 从SpringIOC中获取bean
 * @Company: 美得你智装科技有限公司
 * @Author Paul
 * @Date: 16:28 2017/10/30.
 */
@Component
@Lazy(false)
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context = null;

    @PostConstruct
    public void init() {}

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    public synchronized static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public synchronized static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

}
