package com.github.aesteve.nubes.orm.mongo;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import com.github.aesteve.nubes.orm.annotations.Create;
import com.github.aesteve.nubes.orm.annotations.RemoveById;
import com.github.aesteve.nubes.orm.annotations.RetrieveById;
import com.github.aesteve.nubes.orm.annotations.RetrieveByQuery;
import com.github.aesteve.nubes.orm.annotations.Update;
import com.github.aesteve.nubes.orm.mongo.factories.GetByIdProcessorFactory;
import com.github.aesteve.nubes.orm.mongo.factories.QueryListProcessorFactory;
import com.github.aesteve.nubes.orm.mongo.factories.RemoveByIdProcessorFactory;
import com.github.aesteve.nubes.orm.mongo.factories.SavesAndReturnProcessorFactory;
import com.github.aesteve.nubes.orm.mongo.factories.UpdateAndReturnProcessorFactory;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.vertx.nubes.VertxNubes;

public class MongoNubes extends VertxNubes {

	public static final String MONGO_SERVICE_NAME = "mongo";

	protected MongoService mongo;

	private JsonObject jsonConfig;

	public MongoNubes(Vertx vertx, JsonObject config) {
		super(vertx, config);
		this.jsonConfig = config.getJsonObject("mongo");
		mongo = new MongoService();
		registerService(MONGO_SERVICE_NAME, mongo);
	}

	@Override
	public void bootstrap(Handler<AsyncResult<Router>> handler) {
		mongo.init(vertx, jsonConfig);
		registerAnnotationProcessor(Create.class, new SavesAndReturnProcessorFactory(mongo));
		registerAnnotationProcessor(Update.class, new UpdateAndReturnProcessorFactory(mongo));
		registerAnnotationProcessor(RetrieveById.class, new GetByIdProcessorFactory(mongo));
		registerAnnotationProcessor(RetrieveByQuery.class, new QueryListProcessorFactory(mongo));
		registerAnnotationProcessor(RemoveById.class, new RemoveByIdProcessorFactory(mongo));
		super.bootstrap(handler);
	}

}
