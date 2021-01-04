package ru.iuriimudrak.restaurant.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
// https://stackoverflow.com/a/61980073
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractNamedEntity extends AbstractBaseEntity {

	@NotBlank
	@Size(min = 2, max = 100)
	@Column(name = "name", nullable = false)
	protected String name;

	protected AbstractNamedEntity(Integer id, String name) {
		super(id);
		this.name = name;
	}

	@Override
	public String toString() {
		return super.toString() + '(' + name + ')';
	}
}
