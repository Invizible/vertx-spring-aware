# vertx-spring-aware
Allows you to write verticles as a spring components.

You can find examples [here](https://github.com/Invizible/vertx-spring-aware-examples).

# How to use
Import one of Spring Configuration classes (VertxConfiguration or ClusteredVertxConfiguration) in your Context

```java
@Import(VertxConfiguration.class) // 1. Import Vertx configuration
@SpringBootApplication
public class SpringVertxApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringVertxApplication.class, args); // 2. Run app (will create Spring context)
  }

}
```

Put @Verticle annotation on your Verticles:

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

That's it. Now all your Verticles will be deployed automatically by VertxConfiguration and dependency injection will be available.

See examples project for more use cases.