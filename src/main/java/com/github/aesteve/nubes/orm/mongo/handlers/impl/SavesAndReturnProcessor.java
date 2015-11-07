package com.github.aesteve.nubes.orm.mongo.handlers.impl;

import com.github.aesteve.nubes.orm.annotations.Create;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.vertx.nubes.handlers.AnnotationProcessor;
import com.github.aesteve.vertx.nubes.handlers.impl.NoopAfterAllProcessor;
import com.github.aesteve.vertx.nubes.marshallers.Payload;

import io.vertx.ext.web.RoutingContext;

public class SavesAndReturnProcessor extends NoopAfterAllProcessor implements AnnotationProcessor<Create> {
	
	private MongoService mongo;
	
	public SavesAndReturnProcessor(MongoService mongo) {
		this.mongo = mongo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void postHandle(RoutingContext context) {
		Payload payload = context.get(Payload.DATA_ATTR);
		mongo.create(payload.get(), res -> {
			if (res.failed()) {
				context.fail(res.cause());
			} else {
				payload.set(res.result());
				context.put(Payload.DATA_ATTR, payload);
				context.next();
			}
		});
	}

	@Override
	public void preHandle(RoutingContext context) {
		context.next();
	}
	
	@Override
	public Class<? extends Create> getAnnotationType() {
		return Create.class;
	}
}
