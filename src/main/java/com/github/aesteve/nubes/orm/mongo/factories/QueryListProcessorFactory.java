package com.github.aesteve.nubes.orm.mongo.factories;

import com.github.aesteve.nubes.orm.annotations.RetrieveByQuery;
import com.github.aesteve.nubes.orm.mongo.handlers.impl.QueryListProcessor;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.vertx.nubes.handlers.AnnotationProcessor;
import com.github.aesteve.vertx.nubes.reflections.factories.AnnotationProcessorFactory;

public class QueryListProcessorFactory implements AnnotationProcessorFactory<RetrieveByQuery> {

	private MongoService mongo;

	public QueryListProcessorFactory(MongoService hibernate) {
		this.mongo = hibernate;
	}

	@Override
	public AnnotationProcessor<RetrieveByQuery> create(RetrieveByQuery annnot) {
		return new QueryListProcessor(mongo);
	}

}
