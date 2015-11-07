package com.github.aesteve.nubes.orm.mongo.factories;

import com.github.aesteve.nubes.orm.annotations.Update;
import com.github.aesteve.nubes.orm.mongo.handlers.impl.UpdateAndReturnProcessor;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.vertx.nubes.handlers.AnnotationProcessor;
import com.github.aesteve.vertx.nubes.reflections.factories.AnnotationProcessorFactory;

public class UpdateAndReturnProcessorFactory implements AnnotationProcessorFactory<Update> {

	private MongoService mongo;
	
	public UpdateAndReturnProcessorFactory(MongoService hibernate) {
		this.mongo = hibernate;
	}
	
	@Override
	public AnnotationProcessor<Update> create(Update update) {
		return new UpdateAndReturnProcessor(mongo);
	}
	
}
