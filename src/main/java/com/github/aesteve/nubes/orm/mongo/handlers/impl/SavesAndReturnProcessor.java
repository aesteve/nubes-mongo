package com.github.aesteve.nubes.orm.mongo.handlers.impl;

import java.util.function.Function;

import io.vertx.ext.web.RoutingContext;

import com.github.aesteve.nubes.orm.annotations.Create;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.nubes.orm.queries.UpdateBy;
import com.github.aesteve.vertx.nubes.handlers.AnnotationProcessor;
import com.github.aesteve.vertx.nubes.handlers.impl.NoopAfterAllProcessor;
import com.github.aesteve.vertx.nubes.marshallers.Payload;

public class SavesAndReturnProcessor extends NoopAfterAllProcessor implements AnnotationProcessor<Create> {

	private static Function<?, ?> defaultTransform = someObj -> someObj;

	private MongoService mongo;

	public SavesAndReturnProcessor(MongoService mongo) {
		this.mongo = mongo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void postHandle(RoutingContext context) {
		Payload<?> payload = context.get(Payload.DATA_ATTR);
		Object payloadAsObject = payload.get();
		Object createdObject = payloadAsObject;
		UpdateBy updateBy = null;
		if (payloadAsObject instanceof UpdateBy) {
			updateBy = (UpdateBy) payloadAsObject;
			createdObject = updateBy.updated;
		}
		final Function transform = updateBy == null ? defaultTransform : updateBy::transform;
		mongo.create(createdObject, res -> {
			if (res.failed()) {
				context.fail(res.cause());
			} else {
				Payload<Object> newPayload = new Payload<>();
				newPayload.set(transform.apply(res.result()));
				context.put(Payload.DATA_ATTR, newPayload);
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
