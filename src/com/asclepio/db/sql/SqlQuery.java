package com.asclepio.db.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import com.asclepio.model.ProductoCompra;
import com.asclepio.model.Usuario;
import com.asclepio.db.AccesoDB;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.asclepio.model.Producto;

public class SqlQuery {
	public static final String NOMBRE_TABLA = "PRODUCTO";
	public static final String COLUMN_IDPRODUCTO = "NOMBRE";
	public static final String COLUMN_PRECIO = "PRECIO";
	public static final String COLUMN_STOCK = "STOCK";

	private AccesoDB acceso;

	public SqlQuery() {

		acceso = new AccesoDB();
	}

	public String consultarPwdxUser(int idUsuario) {
		String pwd = null;

		String query = "SELECT " + UserContract.COLUMN_PASSWD + " FROM " + UserContract.NOMBRE_TABLA + " WHERE "
				+ UserContract.COLUMN_IDUSUARIO + " = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rslt = null;

		try {
			con = acceso.getConnection();
			pstmt = con.prepareStatement(query);

			pstmt.setInt(1, idUsuario);

			rslt = pstmt.executeQuery();

			if (rslt.next()) {

				pwd = rslt.getString(1);
			}

		} catch (ClassNotFoundException e) {
			System.out.println("El driver indicado no es correcto");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error, sentencia incorrecta");
			e.printStackTrace();
		} finally {

			try {
				if (rslt != null)
					rslt.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

		return pwd;
	}

	public ArrayList<Producto> getSearchedProd(String nombre) {
		ArrayList<Producto> listaProdBusq = new ArrayList<Producto>();

		String query = "SELECT * FROM " + ProductContract.NOMBRE_TABLA + " WHERE UPPER ( " + ProductContract.COLUMN_NOM
				+ ") LIKE ? ";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rslt = null;

		try {
			con = acceso.getConnection();

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, "%" + nombre.toUpperCase() + "%");
			rslt = pstmt.executeQuery();

			Producto prod;
			String id;
			String nom;
			String tipo;
			double precio;
			int stock;

			while (rslt.next()) {
				id = rslt.getString(1);
				nom = rslt.getString(2);
				tipo = rslt.getString(3);
				precio = rslt.getDouble(4);
				stock = rslt.getInt(5);

				prod = new Producto(id, nom, tipo, precio, stock);
				listaProdBusq.add(prod);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rslt != null)
					rslt.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return listaProdBusq;

	}

	public ArrayList<Producto> getProducts() {
		ArrayList<Producto> listaProductos = new ArrayList<Producto>();

		String query = "SELECT * FROM " + ProductContract.NOMBRE_TABLA;

		Connection con = null;
		Statement stmt = null;
		ResultSet rslt = null;

		try {
			con = acceso.getConnection();
			stmt = con.createStatement();
			rslt = stmt.executeQuery(query);

			Producto prod;
			String id;
			String nombre;
			String tipo;
			double precio;
			int stock;

			while (rslt.next()) {
				id = rslt.getString(ProductContract.COLUMN_IDPRODUCTO);
				nombre = rslt.getString(ProductContract.COLUMN_NOM);
				tipo = rslt.getString(ProductContract.COLUMN_TIPO);
				precio = rslt.getDouble(ProductContract.COLUMN_PRECIO);
				stock = rslt.getInt(ProductContract.COLUMN_STOCK);

				prod = new Producto(id, nombre, tipo, precio, stock);
				listaProductos.add(prod);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rslt != null)
					rslt.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return listaProductos;
	}

	public ArrayList<ProductoCompra> consultarProductos(String fecha) {
		ArrayList<ProductoCompra> productos = new ArrayList<ProductoCompra>();

		String query = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rslt = null;

		try {
			con = this.acceso.getConnection();

			if (fecha == null || fecha.equals("")) {
				query = "SELECT P.NOMBRE, P.PRECIO, SUM(C.CANTIDAD) AS CANTIDAD_TOTAL, C.FECHA_COMPRA, P.ID_PRODUCTO, P.TIPO"
						+ " FROM PRODUCTO P, COMPRA C WHERE P.ID_PRODUCTO = C.ID_STOCK"
						+ " GROUP BY P.NOMBRE, P.TIPO, P.PRECIO, C.FECHA_COMPRA, P.ID_PRODUCTO"
						+ " ORDER BY C.FECHA_COMPRA";

				pstmt = con.prepareStatement(query);
			} else {
				query = "SELECT P.NOMBRE, P.PRECIO, SUM(C.CANTIDAD) AS CANTIDAD_TOTAL, C.FECHA_COMPRA, P.ID_PRODUCTO, P.TIPO"
						+ " FROM PRODUCTO P, COMPRA C" + " WHERE P.ID_PRODUCTO = C.ID_STOCK" + " AND FECHA_COMPRA = ?"
						+ " GROUP BY P.NOMBRE, P.TIPO, P.PRECIO, C.FECHA_COMPRA, P.ID_PRODUCTO";

				pstmt = con.prepareStatement(query);
				pstmt.setString(1, fecha);
			}

			rslt = pstmt.executeQuery();

			while (rslt.next()) {
				String id = rslt.getString(ProductContract.COLUMN_IDPRODUCTO);
				String nombre = rslt.getString("NOMBRE");
				String tipo = rslt.getString("TIPO");
				double precio = rslt.getDouble("PRECIO");
				int stock = rslt.getInt("ID_PRODUCTO");
				int cantidad = rslt.getInt("CANTIDAD_TOTAL");
				String fechaCompra = rslt.getString("FECHA_COMPRA");

				Producto p = new Producto(id, nombre, tipo, precio, stock);
				ProductoCompra producto = new ProductoCompra(p, cantidad, fechaCompra);
				productos.add(producto);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rslt != null)
					rslt.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return productos;

	}

	public int updateStockRest(int cant, String id) {
		int result = 0;
		String query = "UPDATE " + ProductContract.NOMBRE_TABLA + " SET " + ProductContract.COLUMN_STOCK + " =  "
				+ ProductContract.COLUMN_STOCK + " - ? " + " WHERE " + ProductContract.COLUMN_IDPRODUCTO + " = ?; ";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = acceso.getConnection();
			pstmt = con.prepareStatement(query);

			pstmt.setInt(1, cant);
			pstmt.setString(2, id);

			result = pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			System.out.println("El driver indicado no es correcto");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error en la base de datos: sentencia incorrecta");
			e.printStackTrace();
		} finally {

			try {

				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

		return result;

	}

	public void reponerStock(String idStock, int cantidad) {

		// UPDATE PRODUCTO SET STOCK = STOCK + cantidad WHERE ID_PRODUCTO = idStock

		int result = 0;
		String query = "UPDATE " + ProductContract.NOMBRE_TABLA + " SET " + ProductContract.COLUMN_STOCK + " = "
				+ ProductContract.COLUMN_STOCK + " + ? " + " WHERE " + ProductContract.COLUMN_IDPRODUCTO + " = ?";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = acceso.getConnection();
			pstmt = con.prepareStatement(query);

			pstmt.setInt(1, cantidad);
			pstmt.setString(2, idStock);

			result = pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	public int updateStockSum(String nom, String tipo, int cant) {
		int resultado = 0;

		String query = "UPDATE " + ProductContract.NOMBRE_TABLA + " SET " + ProductContract.COLUMN_STOCK + " = "
				+ ProductContract.COLUMN_STOCK + " + ? " + " WHERE UPPER( " + ProductContract.COLUMN_NOM
				+ " ) LIKE ? AND UPPER(" + ProductContract.COLUMN_TIPO + ") LIKE ?;";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = acceso.getConnection();

			pstmt = con.prepareStatement(query);

			pstmt.setInt(1, cant);
			pstmt.setString(2, "%" + nom.toUpperCase() + "%");
			pstmt.setString(3, "%" + tipo.toUpperCase() + "%");

			resultado = pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			System.out.println("El driver indicado no es correcto");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error en la base de datos: sentencia incorrecta");
			e.printStackTrace();
		} finally {

			try {

				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

		return resultado;
	}

	public void insertTabla(List<ProductoCompra> compra, Usuario usuario, String fechaCompra) {
		int[] result;

		String query = "INSERT INTO " + CompraContract.NOMBRE_TABLA + "( "
				+ CompraContract.COLUMN_FEC + ", " + CompraContract.COLUMN_IMP + ", " + CompraContract.COLUMN_IDUSER
				+ ", " + CompraContract.COLUMN_IDSTOCK + ", " + CompraContract.COLUMN_CANT + ")"
				+ " VALUES (?, ?, ?, ?, ?);";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = acceso.getConnection();
			pstmt = con.prepareStatement(query);

			for (ProductoCompra p : compra) {

				pstmt.setString(1, fechaCompra);
				pstmt.setDouble(2, p.getProducto().getPrecio());
				pstmt.setInt(3, usuario.getIdUsuario());
				pstmt.setString(4, p.getProducto().getIdProducto());
				pstmt.setInt(5, p.getCantidad());

				pstmt.addBatch();

			}

			result = pstmt.executeBatch();

			for (int i = 0; i < result.length; i++) {
				if (result[i] == Statement.SUCCESS_NO_INFO) {
					System.out.println("Execution " + i + ": unknown number of rows updated");
				} else {
					System.out.println("Execution " + i + "successful: " + result[i] + " rows updated");
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
