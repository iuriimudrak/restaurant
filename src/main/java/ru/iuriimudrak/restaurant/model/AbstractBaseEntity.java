package ru.iuriimudrak.restaurant.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import ru.iuriimudrak.restaurant.HasId;

import javax.persistence.*;

@Data
@MappedSuperclass
// https://stackoverflow.com/a/61980073
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Access(AccessType.FIELD)
public abstract class AbstractBaseEntity implements HasId {

	private static final int START_SEQ = 100000;

	@Id
	@SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = START_SEQ)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
	protected Integer id;

	protected AbstractBaseEntity(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !getClass().equals(Hibernate.getClass(o))) {
			return false;
		}
		AbstractBaseEntity that = (AbstractBaseEntity) o;
		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id;
	}
}
