package com.asclepio.model;

public class ProductoCompra {
	
	private Producto producto;
	private int cantidad;
	private String fechaCompra;
	
	public ProductoCompra(Producto producto, int cantidad) {
		this.producto= producto;
		this.cantidad = cantidad;
	}
	
	public ProductoCompra(Producto producto, int cantidad, String fechaCompra) {
		this.producto= producto;
		this.cantidad = cantidad;
		this.fechaCompra = fechaCompra;
	}

	public Producto getProducto() {
		return this.producto;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public String getFechaCompra() {
		return fechaCompra;
	}

	@Override
	public String toString() {
		return "ProductoCompra [producto " + this.producto.toString() + ", cantidad=" + cantidad + "]";
	}
}
