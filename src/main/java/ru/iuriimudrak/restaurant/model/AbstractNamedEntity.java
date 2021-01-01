package ru.iuriimudrak.restaurant.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
@NoArgsConstructor
public class AbstractNamedEntity extends AbstractBaseEntity {

	@NotBlank
	@Size(min = 2, max = 100)
	@Column(name = "name", nullable = false)
	String name;

	protected AbstractNamedEntity(Integer id, String name) {
		super(id);
		this.name = name;
	}

	@Override
	public String toString() {
		return super.toString() + '(' + name + ')';
	}
}
