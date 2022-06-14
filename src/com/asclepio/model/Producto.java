package com.asclepio.model;

public class Producto {
	private String idProducto;
	private String nombre;
	private String tipo;
	private double precio;
	private int stock;

	public Producto(String idProducto, String nombre, String tipo, double precio, int stock) {
		this.idProducto = idProducto;
		this.nombre = nombre;
		this.tipo = tipo;
		this.precio = precio;
		this.stock = stock;
	}

	@Override
	public String toString() {

		return nombre + " - " + tipo;
		/*
		 * return "Medicamento [idProducto=" + idProducto + ", nombre=" + nombre +
		 * ", tipo=" + tipo + ", precio=" + precio + ", stock=" + stock + "]";
		 */
	}

	public String getIdProducto() {
		return idProducto;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public double getPrecio() {
		return precio;
	}

	public int getStock() {
		return stock;
	}
}
