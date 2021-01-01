package ru.iuriimudrak.restaurant.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
	USER,
	ADMIN;

	@Override
	public String getAuthority() {
		return "ROLE_" + name();
	}
}
