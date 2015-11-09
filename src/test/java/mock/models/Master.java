package mock.models;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class Master {
	public String _id;
	public String name;

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
}
