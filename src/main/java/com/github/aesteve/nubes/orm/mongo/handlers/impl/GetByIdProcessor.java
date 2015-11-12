package com.github.aesteve.nubes.orm.mongo.handlers.impl;

import io.vertx.ext.web.RoutingContext;

import com.github.aesteve.nubes.orm.annotations.RetrieveById;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.vertx.nubes.handlers.AnnotationProcessor;
import com.github.aesteve.vertx.nubes.handlers.impl.NoopAfterAllProcessor;
import com.github.aesteve.vertx.nubes.marshallers.Payload;

public class GetByIdProcessor extends NoopAfterAllProcessor implements AnnotationProcessor<RetrieveById> {

	private MongoService mongo;

	public GetByIdProcessor(MongoService mongo) {
		this.mongo = mongo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void postHandle(RoutingContext context) {
		Payload<FindBy> payload = context.get(Payload.DATA_ATTR);
		FindBy findBy = payload.get();
		mongo.findBy(findBy, res -> {
			if (res.failed()) {
				context.fail(res.cause());
			} else {
				Payload newPayload = new Payload<>();
				newPayload.set(findBy.transform(res.result()));
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
	public Class<? extends RetrieveById> getAnnotationType() {
		return RetrieveById.class;
	}

}
