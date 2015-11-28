package com.github.aesteve.nubes.orm.mongo.handlers.impl;

import io.vertx.ext.web.RoutingContext;

import com.github.aesteve.nubes.orm.annotations.Update;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.nubes.orm.queries.UpdateBy;
import com.github.aesteve.vertx.nubes.handlers.AnnotationProcessor;
import com.github.aesteve.vertx.nubes.handlers.impl.NoopAfterAllProcessor;
import com.github.aesteve.vertx.nubes.marshallers.Payload;

public class UpdateAndReturnProcessor extends NoopAfterAllProcessor implements AnnotationProcessor<Update> {

	private MongoService mongo;

	public UpdateAndReturnProcessor(MongoService mongo) {
		this.mongo = mongo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void postHandle(RoutingContext context) {
		Payload<UpdateBy> payload = context.get(Payload.DATA_ATTR);
		UpdateBy updateBy = payload.get();
		mongo.update(updateBy.updated, updateBy.findBy, res -> {
			if (res.failed()) {
				context.fail(res.cause());
			} else {
				Payload newPayload = new Payload<>();
				newPayload.set(updateBy.transform(res.result()));
				context.put(Payload.DATA_ATTR, payload);
				context.next();
			}
		});
	}

	@Override
	public void preHandle(RoutingContext context) {
		context.next();
	}

}
