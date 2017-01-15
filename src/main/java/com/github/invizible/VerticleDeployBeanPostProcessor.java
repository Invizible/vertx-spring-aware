package com.github.invizible;

import io.vertx.core.Vertx;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author Alex
 */
@CommonsLog
public class VerticleDeployBeanPostProcessor implements BeanPostProcessor {

  @Autowired
  private Vertx vertx;

  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    Class<?> clazz = bean.getClass();
    if (clazz.isAnnotationPresent(Verticle.class)) {
      if (bean instanceof io.vertx.core.Verticle) {
        log.info(String.format("Deploying Verticle: %s", clazz.getName()));
        vertx.deployVerticle((io.vertx.core.Verticle) bean, result -> {
          if (result.succeeded()) {
            log.info(String.format("Verticle %s has been deployed successfully", clazz.getName()));
          } else {
            log.error(String.format("Verticle %s has not been deployed!", clazz.getName()), result.cause());
          }
        });
      } else {
        log.error(String.format("Incorrect Verticle class %s. Class marked with @Verticle annotation should implement io.vertx.core.Verticle", clazz.getName()));
      }
    }

    return bean;
  }

  public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
    return o;
  }
}
