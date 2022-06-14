package com.asclepio.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.asclepio.db.sql.SqlQuery;
import com.asclepio.gui.PStock;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.asclepio.gui.PCompra;
import com.asclepio.gui.PHistorial;
import com.asclepio.gui.VLogin;
import com.asclepio.gui.VPrincipal;
import com.asclepio.model.Usuario;
import com.asclepio.model.CurrentUser;
import com.asclepio.model.Producto;
import com.asclepio.model.ProductoCompra;

public class AppControl implements ActionListener {

	private static final int TOTAL_INTENTOS = 3;
	private static ArrayList<Producto> listaProd;
	VPrincipal vp;
	VLogin vl;
	PCompra pc;
	private int contAcces;
	SqlQuery sql;
	PStock ps;
	PHistorial ph;

	public AppControl(VPrincipal vp, VLogin vl, PCompra pc, PStock ps, PHistorial pHist) {
		this.vp = vp;
		this.vl = vl;
		this.pc = pc;
		this.ps = ps;
		this.sql = new SqlQuery();
		this.ph = pHist;
		contAcces = 0;
		listaProd = new ArrayList<Producto>();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			JButton button = (JButton)e.getSource();
			String tooltipText = tooltipTextNotNull(button);
			
			if(tooltipText.equals(PCompra.BTN_BUSQ)) {
				searchProd();
			} else if (tooltipText.equals(PHistorial.BTN_CONSULTAR)) {
				this.consultarProductos();
			} else if (tooltipText.equals(PCompra.BTN_CARRITO)) {
				addCarrito();
			} else if (tooltipText.equals(PCompra.BTN_COMPRAR)) {
				buyProducts();
			} else if (tooltipText.equals(PCompra.BTN_ELIMINAR)) {
				deleteProd();
			} else if (e.getActionCommand().equals(PStock.BTN_BUSQUEDA_PSTOCK)) {
				String palabra = PStock.obtenerTexto();
				listaProd = sql.getSearchedProd(palabra);
				ps.filtrarTabla(listaProd);
				ps.habComponents(true);
				
			} else if (e.getActionCommand().equals(PStock.BTN_REPONER_PSTOCK)) {
				String idStock = ps.productoSeleccionado();
				if (idStock != null) {
					int cantidad = ps.cantidadReponer();
					sql.reponerStock(idStock, cantidad);

				}
				
				
			}else if (e.getActionCommand().equals(VLogin.BTN_LOGIN)) {
		
				obtenerUsuario();
				
				
			}else if (e.getActionCommand().equals(VPrincipal.BTN_SEE_STOCK)) {
				
				vp.uploadPanel(ps);
				ps.habComponents(false);
				vp.hacerVisible(true);
				
				
				
			}else if (e.getActionCommand().equals(VPrincipal.BTN_REGISTRAR_C)) {
				
				vp.uploadPanel(pc);
				vp.hacerVisible(true);
				
			}else if (e.getActionCommand().equals(VPrincipal.BTN_HISTORIAL_C)) {
			
				vp.uploadPanel(ph);
				vp.hacerVisible(true);

			}
			
		}else if(e.getSource() instanceof JTextField){
			if (e.getSource().equals(vl.getTxtPwd()) || e.getSource().equals(vl.getTxtUsuario())) {
				
				obtenerUsuario();
				
			} else if(e.getSource().equals(ph.getTxtFecha())) {
				consultarProductos();
			}
			
		}else if(e.getSource() instanceof JMenuItem){
			
			if (e.getActionCommand().equals(VPrincipal.ITEM_MENU_LOGOUT)) {
				backLogin();
				
				
			}else if (e.getActionCommand().equals(VPrincipal.ITEM_MENU_EXIT)) {
				
				int option = JOptionPane.showConfirmDialog(vp, "¿Estas seguro que deseas salir?", "Confirmar Salida", 
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (option == JOptionPane.YES_OPTION) {
					System.exit(0);
				}

			}
		}

	}

	private String tooltipTextNotNull(JButton button) {
		String tooltipText = button.getToolTipText();
		if (tooltipText == null) {
			tooltipText = "";
		}
		return tooltipText;
	}

	private void deleteProd() {

		if (pc.getTb().getSelectedRow() == -1) {
			pc.setError("Debe seleccionar el elemento a eliminar");
		} else {
			ProductoCompra p = pc.getSelectedP();

			String nom = p.getProducto().getNombre();
			String tipo = p.getProducto().getTipo();

			int cant = p.getCantidad();

			int result = sql.updateStockSum(nom, tipo, cant);

			if (result <= 0) {
				pc.setError("Error en la base de datos");
			} else {
				JOptionPane.showMessageDialog(pc, "Los datos se actualizaron correctamente", "Resultado de Operaci�n",
						JOptionPane.INFORMATION_MESSAGE);
			}

			pc.borrarFila(pc.getTb().getSelectedRow());

		}

	}

	private void buyProducts() {
		List<ProductoCompra> compra = pc.getDatosCompra();

		Usuario usuario = CurrentUser.getUsuario();
		String fechaCompra = pc.getDate();
		System.out.println(fechaCompra);

		for (ProductoCompra p : compra) {
			System.out.println(p.getCantidad());
		}
		sql.insertTabla(compra, usuario, fechaCompra);
		
		
		pc.removeElements();
		
	}

	private void backLogin() {
		vp.dispose();
		vl.cleanData();
		vl.hacerVisible();
	}



	private void addCarrito() {

		if (pc.getList().getSelectedIndex() == -1) {
			pc.setError("Debe seleccionar el elemento a a�adir");
		} else {

			Producto prod = pc.getSelectedProd();

			int cant = pc.getSpn();
			System.out.println(cant);
			// String error = "";

			if (cant <= 0 || cant > prod.getStock()) {
				pc.setError("La cantidad debe ser mayor que 0 y menor que la de Stock");
			} else {
				int result = sql.updateStockRest(cant, prod.getIdProducto());

				if (result <= 0) {
					pc.setError("Error en la base de datos");
				} else {
					pc.rellenarTabla(prod, cant);

				}

			}

		}

	}

	private void searchProd() {

		if (pc.getTxtBusq().isEmpty()) {
			pc.showList(sql.getProducts());
		} else {
			String busq = pc.getTxtBusq();
			ArrayList<Producto> lista = new ArrayList<>();
			lista = sql.getSearchedProd(busq);
			if (lista.isEmpty()) {
				pc.setError("No hay resultados para su b�squeda");
			} else {
				pc.showList(sql.getSearchedProd(busq));
			}

		}

	}

	private void obtenerUsuario() {
		boolean acceso = true;
		Usuario user = vl.comprobarDatos();

		if (user != null) {
			contAcces++;

			String pwd = sql.consultarPwdxUser(user.getIdUsuario());
			String error = "";

			if (pwd == null) {
				error = "El id usuario no existe";

			} else if (!pwd.equals(user.getPwd())) {
				error = "La password introducida no es correcta.";

			} else {
				CurrentUser.setUsuario(user);

				acceso = false;
				vl.dispose();
				vp.showVPrincipal();
				contAcces = 0;
			}

			if (acceso) {
				if (contAcces < 3) {
					error += "\nTe qudan " + (TOTAL_INTENTOS - contAcces) + " intentos";
					vl.setError(error);

				} else {
					error += "\nSe han agotado los tres intentos la aplicación se va a cerrar";
					vl.setError(error);
					System.exit(0);
				}

			}

		}

	}
	
	public void consultarProductos() {
		
		String fecha = ph.getFecha();
		
		SqlQuery productoContract = new SqlQuery();
		ArrayList<ProductoCompra> productos = productoContract.consultarProductos(fecha);
		ph.rellenarTabla(productos);	
		
	}
}
