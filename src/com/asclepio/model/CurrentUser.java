package com.asclepio.model;

public class CurrentUser {

	private static Usuario user;
	
	public static void setUsuario(Usuario _user) {
		user = _user;
	}
	
	public static Usuario getUsuario() {
		return user;
	}
}
