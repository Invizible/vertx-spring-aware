package com.github.invizible;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.StringUtils;

/**
 * BeanPostProcessor which will deploy all @Verticles.
 *
 * @author Alex Korobko
 */
@CommonsLog
public class VerticleDeployBeanPostProcessor implements BeanPostProcessor {

  @Autowired
  private Vertx vertx;

  @Autowired
  private BeanFactory beanFactory;

  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }

  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    Class<?> clazz = bean.getClass();

    if (!clazz.isAnnotationPresent(Verticle.class)) {
      return bean;
    }

    if (!(bean instanceof io.vertx.core.Verticle)) {
      log.error(String.format(
        "Incorrect Verticle class %s. Class marked with @Verticle annotation should implement io.vertx.core.Verticle",
        clazz.getName()));
      return bean;
    }

    deployVerticle((io.vertx.core.Verticle) bean, clazz);

    return bean;
  }

  private void deployVerticle(io.vertx.core.Verticle bean, Class<?> clazz) {
    log.info(String.format("Deploying Verticle: %s", clazz.getName()));

    vertx.deployVerticle(bean, getDeploymentOptions(clazz), result -> {
      if (result.succeeded()) {
        log.info(String.format("Verticle %s has been deployed successfully", clazz.getName()));
      } else {
        log.error(String.format("Verticle %s has not been deployed!", clazz.getName()), result.cause());
      }
    });
  }

  private DeploymentOptions getDeploymentOptions(Class<?> clazz) {
    Verticle annotation = clazz.getAnnotation(Verticle.class);
    String deploymentOptionsBeanName = annotation.deploymentOptionsBeanName();

    if (StringUtils.hasText(deploymentOptionsBeanName) && beanFactory.containsBean(deploymentOptionsBeanName)) {
      DeploymentOptions deploymentOptions = beanFactory.getBean(deploymentOptionsBeanName, DeploymentOptions.class);
      log.info(String.format("Using %s bean as a deployment options (%s) for Verticle %s",
        deploymentOptionsBeanName,
        deploymentOptions.toJson().toString(),
        clazz.getName()));
      return deploymentOptions;
    }

    log.info(String.format("Using empty deployment options for Verticle %s", clazz.getName()));
    return new DeploymentOptions();
  }
}
