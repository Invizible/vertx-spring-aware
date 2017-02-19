package com.github.invizible.vertx.springaware;

import io.vertx.core.Vertx;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Configuration;

@Configuration
@CommonsLog
public class VertxConfiguration extends AbstractVertxConfiguration {

  @Override
  protected Vertx createVertx() {
    log.info("Creating Vert.x...");
    return Vertx.vertx(vertxOptions());
  }
}
