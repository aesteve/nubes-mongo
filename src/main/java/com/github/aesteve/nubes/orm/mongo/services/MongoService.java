package com.github.aesteve.nubes.orm.mongo.services;

import java.util.ArrayList;
import java.util.List;

import org.boon.json.JsonFactory;
import org.boon.json.JsonSerializer;
import org.boon.json.JsonSerializerFactory;
import org.boon.json.ObjectMapper;

import com.github.aesteve.nubes.orm.mongo.queries.MongoCriteriaBuilder;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.nubes.orm.queries.ListAndCount;
import com.github.aesteve.vertx.nubes.services.Service;
import com.github.aesteve.vertx.nubes.utils.async.MultipleFutures;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MongoService implements Service {

	private MongoClient mongo;
	private JsonSerializer serializer;
	private ObjectMapper mapper;
	private Vertx vertx;
	private JsonObject config;
	
	@Override
	public void init(Vertx vertx, JsonObject config) {
		this.vertx = vertx;
		this.config = config;
	}

	@Override
	public void start(Future<Void> future) {
		mongo = MongoClient.createShared(vertx, config);
		this.serializer = new JsonSerializerFactory().create();
		this.mapper = JsonFactory.create();
		future.complete();
	}

	@Override
	public void stop(Future<Void> future) {
		future.complete();
	}
	
	public<T> void findBy(FindBy<T> findBy, Handler<AsyncResult<T>> handler) {
		mongo.findOne(findBy.getType().getName(), new MongoCriteriaBuilder<>(findBy).toJson(), null, res -> {
			if (res.failed()) {
				handler.handle(Future.failedFuture(res.cause()));
			} else {
				JsonObject result = res.result();
				if (result == null) {
					handler.handle(Future.succeededFuture(null));
					return;
				}
				try {
					T mapped = mapper.fromJson(result.toString(), findBy.getType());
					handler.handle(Future.succeededFuture(mapped));
				} catch(Exception e) {
					handler.handle(Future.failedFuture(e));
				}
			}
		});
	}
	
	public<T> void listAndCount(FindBy<T> findBy, Integer firstItem, Integer lastItem, Handler<AsyncResult<ListAndCount<T>>> handler) {
		mongo.find(findBy.getType().getName(), new MongoCriteriaBuilder<>(findBy).toJson(), res -> {
			if (res.failed()) {
				handler.handle(Future.failedFuture(res.cause()));
			} else {
				List<JsonObject> complete = res.result();
				if (complete == null) {
					complete = new ArrayList<>(0);
				}
				ListAndCount<T> result = new ListAndCount<>();
				result.count = Long.valueOf(complete.size());
				try {
					Integer lastItemInRange = Math.min(lastItem, complete.size());
					complete.subList(firstItem, lastItemInRange).forEach(item -> {
						result.list.add(mapper.fromJson(item.toString(), findBy.getType()));
					});
					handler.handle(Future.succeededFuture(result));
				} catch(Exception e) {
					handler.handle(Future.failedFuture(e));
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public<T> void create(T object, Handler<AsyncResult<T>> handler) {
		JsonObject json = new JsonObject(serializer.serialize(object).toString());
		mongo.insert(object.getClass().getName(), json, res -> {
			if (res.failed()) {
				handler.handle(Future.failedFuture(res.cause()));
			} else {
				try {
					T mapped = (T)mapper.fromJson(json.toString(), object.getClass());
					handler.handle(Future.succeededFuture(mapped));
				} catch(Exception e) {
					handler.handle(Future.failedFuture(e));
				}
			}
		});
	}
	
	public<T> void createMany(List<T> objects, Handler<AsyncResult<List<T>>> handler) {
		if (objects == null || objects.isEmpty()) {
			handler.handle(Future.succeededFuture(new ArrayList<T>()));
			return;
		}
		MultipleFutures<T> futures = new MultipleFutures<>();
		List<T> createdObjects = new ArrayList<>();
		futures.setHandler(res -> {
			if (res.failed()) {
				handler.handle(Future.failedFuture(res.cause()));
			} else {
				handler.handle(Future.succeededFuture(createdObjects));
			}
		});
		objects.forEach(object -> {
			futures.add(future -> {
				create(object, res -> {
					if (res.failed()) {
						future.fail(res.cause());
					} else {
						createdObjects.add(res.result());
						future.complete(res.result());
					}
				});
			});
		});
		futures.start();
	}
	
	public<T> void update(T object, FindBy<T> findBy, Handler<AsyncResult<T>> handler) {
		JsonObject json = new JsonObject(serializer.serialize(object).toString());
		JsonObject query = new JsonObject().put("$set", json);
		mongo.update(object.getClass().getName(), new MongoCriteriaBuilder<>(findBy).toJson(), query, res -> {
			if (res.failed()) {
				handler.handle(Future.failedFuture(res.cause()));
			} else {
				handler.handle(Future.succeededFuture(object));
			}
		});
	}
	
	public<T> void delete(FindBy<T> findBy, Handler<AsyncResult<Void>> handler) {
		mongo.removeOne(findBy.getType().getName(), new MongoCriteriaBuilder<>(findBy).toJson(), handler);
	}
	
	public<T> void deleteAll(FindBy<T> findBy, Handler<AsyncResult<Void>> handler) {
		mongo.remove(findBy.getType().getName(), new MongoCriteriaBuilder<>(findBy).toJson(), handler);
	}
	
}
