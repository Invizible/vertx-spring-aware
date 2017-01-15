# vertx-spring-aware
Allows you to write verticles as a spring components.

You can find examples [here](https://github.com/Invizible/vertx-spring-aware-examples).

# How to use
Add Vertx into spring context and BeanPostProcessor like so:

```java
  @Bean
  public Vertx vertx() {
    return Vertx.vertx();
  }

  @Bean
  public VerticleDeployBeanPostProcessor verticleDeployBeanPostProcessor() {
    return new VerticleDeployBeanPostProcessor();
  }
```

Put @Verticle annotation on your Verticle:

```java
@Verticle
public class ServerVerticle extends AbstractVerticle {

  @Autowired
  private TestService testService;

  @Override
  public void start() throws Exception {
    HttpServer httpServer = vertx.createHttpServer()
      .requestHandler(request -> {
        request.response().end(testService.getMessage());
      })
      .listen(8080);
  }
}
```

Now you can easily use spring beans in your verticles. 