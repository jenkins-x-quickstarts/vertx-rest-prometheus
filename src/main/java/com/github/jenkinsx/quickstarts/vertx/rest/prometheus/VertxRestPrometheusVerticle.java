package com.github.jenkinsx.quickstarts.vertx.rest.prometheus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.hotspot.DefaultExports;
import io.prometheus.client.vertx.MetricsHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import static io.vertx.core.Vertx.vertx;

public class VertxRestPrometheusVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);

        exposeHelloWorldEndpoint(router);
        exposeMetricsEndpoint(router);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
        super.start(startFuture);
    }

    private void exposeHelloWorldEndpoint(Router router) {
        router.route("/hello").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");
            response.end(new JsonObject().put("hello", "world").toBuffer());
        });
    }

    private void exposeMetricsEndpoint(Router router) {
        CollectorRegistry registry = CollectorRegistry.defaultRegistry;
        DefaultExports.initialize();
        router.route("/metrics").handler(new MetricsHandler(registry));

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

        Gauge.build("a", "a help").register(registry);
        Gauge.build("b", "b help").register(registry);
        Gauge.build("c", "c help").register(registry);
    }

    public static void main(String[] args) {
        vertx().deployVerticle(new VertxRestPrometheusVerticle());
    }

}