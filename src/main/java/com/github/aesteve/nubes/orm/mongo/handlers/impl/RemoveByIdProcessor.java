package com.github.aesteve.nubes.orm.mongo.handlers.impl;

import io.vertx.ext.web.RoutingContext;

import com.github.aesteve.nubes.orm.annotations.RemoveById;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.vertx.nubes.handlers.AnnotationProcessor;
import com.github.aesteve.vertx.nubes.handlers.impl.NoopAfterAllProcessor;
import com.github.aesteve.vertx.nubes.marshallers.Payload;

public class RemoveByIdProcessor extends NoopAfterAllProcessor implements AnnotationProcessor<RemoveById> {

	private MongoService mongo;

	public RemoveByIdProcessor(MongoService mongo) {
		this.mongo = mongo;
	}

	@Override
	public void postHandle(RoutingContext context) {
		Payload<FindBy<?>> payload = context.get(Payload.DATA_ATTR);
		mongo.delete(payload.get(), res -> {
			context.put(Payload.DATA_ATTR, null);
			if (res.failed()) {
				context.fail(res.cause());
			} else {
				context.next();
			}
		});
	}

	@Override
	public void preHandle(RoutingContext context) {
		context.next();
	}

}
