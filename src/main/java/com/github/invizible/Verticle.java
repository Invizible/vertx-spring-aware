package com.github.invizible;

import io.vertx.core.DeploymentOptions;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark all {@link io.vertx.core.Verticle} with this annotation.
 *
 * @author Alex Korobko
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Verticle {
  /**
   * Specify a bean name of type {@link DeploymentOptions} which will be used as a Verticle deployment configuration.
   * Empty - use nothing
   */
  String deploymentOptionsBeanName() default "commonVerticleDeploymentOptions";
}