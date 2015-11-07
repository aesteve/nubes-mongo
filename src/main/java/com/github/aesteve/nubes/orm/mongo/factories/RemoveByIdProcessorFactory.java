package com.github.aesteve.nubes.orm.mongo.factories;

import com.github.aesteve.nubes.orm.annotations.RemoveById;
import com.github.aesteve.nubes.orm.mongo.handlers.impl.RemoveByIdProcessor;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.vertx.nubes.handlers.AnnotationProcessor;
import com.github.aesteve.vertx.nubes.reflections.factories.AnnotationProcessorFactory;

public class RemoveByIdProcessorFactory implements AnnotationProcessorFactory<RemoveById> {

	private MongoService mongo;
	
	public RemoveByIdProcessorFactory(MongoService hibernate) {
		this.mongo = hibernate;
	}
	
	@Override
	public AnnotationProcessor<RemoveById> create(RemoveById annnot) {
		return new RemoveByIdProcessor(mongo);
	}
	
}
