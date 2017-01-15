package com.github.invizible;

import io.vertx.core.Vertx;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author Alex
 */
public class VerticleDeployBeanPostProcessor implements BeanPostProcessor {

  @Autowired
  private Vertx vertx;

  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    Class<?> clazz = bean.getClass();
    if (clazz.isAnnotationPresent(Verticle.class)) {
      if (bean instanceof io.vertx.core.Verticle) {
        vertx.deployVerticle((io.vertx.core.Verticle) bean);
      } else {
        vertx.deployVerticle(clazz.getName());
      }
    }

    return bean;
  }

  public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
    return o;
  }
}
