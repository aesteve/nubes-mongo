package com.github.aesteve.nubes.orm.mongo.handlers.impl;

import com.github.aesteve.nubes.orm.annotations.RetrieveByQuery;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.vertx.nubes.context.PaginationContext;
import com.github.aesteve.vertx.nubes.handlers.AnnotationProcessor;
import com.github.aesteve.vertx.nubes.handlers.impl.NoopAfterAllProcessor;
import com.github.aesteve.vertx.nubes.marshallers.Payload;

import io.vertx.ext.web.RoutingContext;

public class QueryListProcessor extends NoopAfterAllProcessor implements AnnotationProcessor<RetrieveByQuery> {
	
	private MongoService mongo;
	
	public QueryListProcessor(MongoService mongo) {
		this.mongo = mongo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void postHandle(RoutingContext context) {
		Payload<FindBy<?>> payload = context.get(Payload.DATA_ATTR);
		PaginationContext pageContext = context.get(PaginationContext.DATA_ATTR);
		mongo.listAndCount(payload.get(), pageContext.firstItemInPage(), pageContext.lastItemInPage(), res -> {
			if (res.failed()) {
				context.put(Payload.DATA_ATTR, null);
				context.fail(res.cause());
			} else {
				Payload newPayload = new Payload<>();
				newPayload.set(res.result().list);
				pageContext.setNbItems(res.result().count);
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
	public Class<? extends RetrieveByQuery> getAnnotationType() {
		return RetrieveByQuery.class;
	}

}
