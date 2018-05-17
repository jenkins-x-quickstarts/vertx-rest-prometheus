package com.github.hekonsek.vertx.rest.prometheus;

import io.vertx.core.Vertx;

import static io.vertx.core.Vertx.vertx;

public class VertxRestPrometheusApp {

    public static void main(String[] args) {
        Vertx vertx = vertx();
        vertx.deployVerticle(new HelloWorldVerticle());
    }

}
