package com.github.aesteve.nubes.orm.mongo;

import com.github.aesteve.vertx.nubes.NubesServer;

import io.vertx.core.Context;
import io.vertx.core.Vertx;

public class MongoNubesServer extends NubesServer {
	@Override
	public void init(Vertx vertx, Context context) {
		super.init(vertx, context);
		nubes = new MongoNubes(vertx, context.config());
	}

}
