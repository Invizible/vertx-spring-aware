package com.github.invizible.vertx.springaware;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * This class will be deploying {@link Verticle}'s using {@link SpringVerticleFactory} so that all Verticles will get into Spring Context.
 * It should take all @Verticle classes from scanning packages.
 *
 * @author Alex Korobko
 */
@CommonsLog
public class VerticleDeployer {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private Vertx vertx;

  @Autowired
  private SpringVerticleFactory springVerticleFactory;

  @PostConstruct
  public void deployVerticles() {
    log.info("Starting Verticles deployment...");
    log.info("Getting Verticle bean names...");

    String[] verticleBeanNames = applicationContext.getBeanNamesForAnnotation(Verticle.class);

    log.info(String.format("Found verticle names: %s", Arrays.toString(verticleBeanNames)));
    log.info("Starting deploying found verticles...");

    for (String verticleBeanName : verticleBeanNames) {
      log.info(String.format("Deploying %s...", verticleBeanName));
      
      vertx.deployVerticle(springVerticleFactory.prefix() + ":" + verticleBeanName, getDeploymentOptions(verticleBeanName), result -> {
        if (result.succeeded()) {
          log.info(String.format("Verticle '%s' has been deployed successfully", verticleBeanName));
        } else {
          log.error(String.format("Verticle '%s' has not been deployed!", verticleBeanName), result.cause());
          vertx.close(v -> log.error("Vert.x has been closed!"));
        }
      });
    }
  }

  private DeploymentOptions getDeploymentOptions(String beanName) {
    log.info(String.format("Getting deployment options for bean '%s'", beanName));

    Verticle annotation = applicationContext.findAnnotationOnBean(beanName, Verticle.class);

    String deploymentOptionsBeanName = annotation.deploymentOptionsBeanName();

    if (StringUtils.hasText(deploymentOptionsBeanName) && applicationContext.containsBean(deploymentOptionsBeanName)) {
      DeploymentOptions deploymentOptions = applicationContext.getBean(deploymentOptionsBeanName, DeploymentOptions.class);
      log.info(String.format("Using %s bean as a deployment options (%s) for Verticle %s",
        deploymentOptionsBeanName,
        deploymentOptions.toJson().toString(),
        beanName));
      return deploymentOptions;
    }

    log.info(String.format("Using empty deployment options for Verticle '%s'", beanName));
    return new DeploymentOptions();
  }
}
