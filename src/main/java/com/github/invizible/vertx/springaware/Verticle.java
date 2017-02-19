package com.github.invizible.vertx.springaware;

import io.vertx.core.DeploymentOptions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * Mark all {@link io.vertx.core.Verticle} with this annotation.
 *
 * @author Alex Korobko
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
// Prototype scope is needed as multiple instances of Verticle can be deployed
@Scope(SCOPE_PROTOTYPE)
public @interface Verticle {
  /**
   * Specify a bean name of type {@link DeploymentOptions} which will be used as a Verticle deployment configuration.
   * Empty - use nothing
   */
  String deploymentOptionsBeanName() default "commonVerticleDeploymentOptions";
}