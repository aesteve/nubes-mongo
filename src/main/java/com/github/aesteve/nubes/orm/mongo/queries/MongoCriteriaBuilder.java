package com.github.aesteve.nubes.orm.mongo.queries;

import com.github.aesteve.nubes.orm.queries.FindBy;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class MongoCriteriaBuilder<T> {
	
	private FindBy<T> findBy;
	
	
	public MongoCriteriaBuilder(FindBy<T> findBy) {
		this.findBy = findBy;
	}
	
	public JsonObject toJson() {
		JsonObject crit = new JsonObject();
		findBy.getStrictRestrictions().forEach((name, value) -> {
			crit.put(name, value);
		});
		findBy.getInRestrictions().forEach((name, values) -> {
			crit.put(name, new JsonObject().put("$in", new JsonArray(values)));
		});
		return crit;
	}
}
