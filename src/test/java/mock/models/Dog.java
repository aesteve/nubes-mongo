package mock.models;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class Dog {
	public String name;
	public String breed;
	public String masterId;
	
	public Dog(String name, String breed) {
		this.name = name;
		this.breed = breed;
	}
	
	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
	
	@Override
	public String toString() {
		return "I'm a " + breed + " and my name is: " + name + ". I belong to: " + masterId;
	}
}
