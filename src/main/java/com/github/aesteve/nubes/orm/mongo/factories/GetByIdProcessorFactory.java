package com.github.aesteve.nubes.orm.mongo.factories;

import com.github.aesteve.nubes.orm.annotations.RetrieveById;
import com.github.aesteve.nubes.orm.mongo.handlers.impl.GetByIdProcessor;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.vertx.nubes.handlers.AnnotationProcessor;
import com.github.aesteve.vertx.nubes.reflections.factories.AnnotationProcessorFactory;

public class GetByIdProcessorFactory implements AnnotationProcessorFactory<RetrieveById> {

	private MongoService mongo;

	public GetByIdProcessorFactory(MongoService hibernate) {
		this.mongo = hibernate;
	}

	@Override
	public AnnotationProcessor<RetrieveById> create(RetrieveById annnot) {
		return new GetByIdProcessor(mongo);
	}

}
