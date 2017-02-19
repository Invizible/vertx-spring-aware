package com.github.invizible.vertx.springaware;

import io.vertx.core.Vertx;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

@Configuration
@CommonsLog
public class ClusteredVertxConfiguration extends AbstractVertxConfiguration {

  @Override
  protected Vertx createVertx() {
    try {
      CompletableFuture<Vertx> future = new CompletableFuture<>();
      log.info("Creating clustered Vertx...");
      Vertx.clusteredVertx(vertxOptions(), ar -> {
        if (ar.succeeded()) {
          log.info("Vertx creation is completed!");
          future.complete(ar.result());
        } else {
          log.error("Vertx creation failed!", ar.cause());
          future.completeExceptionally(ar.cause());
        }
      });

      return future.get();
    } catch (Exception e) {
      log.error("Clustered Vertx creation failed", e);
      throw new RuntimeException(e);
    }
  }

}
