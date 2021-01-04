package ru.iuriimudrak.restaurant.to;

import lombok.NoArgsConstructor;
import ru.iuriimudrak.restaurant.HasId;

@NoArgsConstructor
public abstract class BaseTo implements HasId {
	protected Integer id;

	public BaseTo(Integer id) {
		this.id = id;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
}
