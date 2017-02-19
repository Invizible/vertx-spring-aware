package com.github.invizible.vertx.springaware;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Alex Korobko
 */
@CommonsLog
public abstract class AbstractVertxConfiguration {
  private Vertx vertx;

  @PostConstruct
  public void init() throws ExecutionException, InterruptedException {
    vertx = createVertx();

    // We will deploy our verticles using this factory which will create spring aware verticles
    vertx.registerVerticleFactory(springVerticleFactory());
  }

  protected abstract Vertx createVertx();

  /**
   * Exposes the clustered Vert.x instance.
   * We must disable destroy method inference, otherwise Spring will call the {@link Vertx#close()} automatically.
   */
  @Bean(destroyMethod = "")
  public Vertx vertx() {
    return vertx;
  }

  @PreDestroy
  public void close() throws ExecutionException, InterruptedException {
    CompletableFuture<Void> future = new CompletableFuture<>();
    log.info("Closing Vertx...");
    vertx.close(ar -> {
      log.info("Vertx was closed.");
      future.complete(null);
    });
    future.get();
  }

  @Bean
  public VertxOptions vertxOptions() {
    return new VertxOptions();
  }

  @Bean
  public SpringVerticleFactory springVerticleFactory() {
    return new SpringVerticleFactory();
  }

  @Bean
  public VerticleDeployer verticleDeployer() {
    return new VerticleDeployer();
  }
}
