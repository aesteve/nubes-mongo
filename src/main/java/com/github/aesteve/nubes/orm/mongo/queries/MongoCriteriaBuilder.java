package com.github.aesteve.nubes.orm.mongo.queries;

import com.github.aesteve.nubes.orm.queries.FindBy;

import io.vertx.core.json.JsonObject;

public class MongoCriteriaBuilder<T> {
	
	private FindBy<T> findBy;
	
	
	public MongoCriteriaBuilder(FindBy<T> findBy) {
		this.findBy = findBy;
	}
	
	public JsonObject toJson() {
		JsonObject crit = new JsonObject();
		findBy.getRestrictions().forEach((name, value) -> {
			crit.put(name, value);
		});
		return crit;
	}
}
