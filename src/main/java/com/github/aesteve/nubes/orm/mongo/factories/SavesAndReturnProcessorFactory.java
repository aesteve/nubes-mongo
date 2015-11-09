package com.github.aesteve.nubes.orm.mongo.factories;

import com.github.aesteve.nubes.orm.annotations.Create;
import com.github.aesteve.nubes.orm.mongo.handlers.impl.SavesAndReturnProcessor;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.vertx.nubes.handlers.AnnotationProcessor;
import com.github.aesteve.vertx.nubes.reflections.factories.AnnotationProcessorFactory;

public class SavesAndReturnProcessorFactory implements AnnotationProcessorFactory<Create> {

	private MongoService mongo;

	public SavesAndReturnProcessorFactory(MongoService hibernate) {
		this.mongo = hibernate;
	}

	@Override
	public AnnotationProcessor<Create> create(Create arg0) {
		return new SavesAndReturnProcessor(mongo);
	}

}
