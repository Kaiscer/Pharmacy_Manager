package com.asclepio.model;

public class Usuario {
	
	private int idUsuario;
	private String pwd;
	
	
	public Usuario(int idUsuario, String pwd) {
		this.idUsuario = idUsuario;
		this.pwd = pwd;
	}


	public int getIdUsuario() {
		return idUsuario;
	}


	public String getPwd() {
		return pwd;
	}


	public static String validarPwd(String pwd) {
		String error = "";
		
		if (pwd.isBlank()) {
			error = "Debe introducir la password";
			
		}else if (pwd.length() < 8 || pwd.length() > 20) {
			error = "La password contiene entre 8 y 20 caracteres";
			
		}else if (!contieneMayus(pwd) || !contieneNum(pwd) || contieneCarEsp(pwd)) {
			error = "La password debe contener al menos un caracter en mayásculas, " 
					+ "un número y no puede contener caracteres especiales salvo &, +, _ y *";
			
		} 
	
		return error;
	}
	
	private static boolean contieneMayus(String pwd) {
		boolean contieneMayus = false;
		String pwdMayus = pwd.toUpperCase();
		for (int i = 0; i < pwd.length() && !contieneMayus; i++) {
			if (pwd.charAt(i) == pwdMayus.charAt(i) && !Character.isDigit(pwd.charAt(i))) {
				contieneMayus = true;
			}
		}
		return contieneMayus;
	}
	
	private static boolean contieneNum(String pwd) {
		boolean contieneNum = false;
		for (int i = 0; i < pwd.length() && !contieneNum; i++) {
			if (Character.isDigit(pwd.charAt(i))) {
				contieneNum = true;
			}
		}
		return contieneNum;
	}
	private static boolean contieneCarEsp(String pwd) {
		boolean contieneCE = false;
		for (int i = 0; i < pwd.length() && !contieneCE; i++) {
			if (!Character.isAlphabetic(pwd.charAt(i)) && !Character.isDigit(pwd.charAt(i)) && pwd.charAt(i) != '&'
					&& pwd.charAt(i) != '+' && pwd.charAt(i) != '_' && pwd.charAt(i) != '*') {
				contieneCE = true;
			}
		}
		return contieneCE;
	}


	

}
