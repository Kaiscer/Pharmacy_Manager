package com.asclepio.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.asclepio.control.AppControl;
import com.asclepio.model.Producto;
import com.asclepio.model.ProductoCompra;
import javax.swing.SpinnerNumberModel;

//import asclepio.view.PCompra;

public class PCompra extends JPanel {

	public static final int ALTO = 650;
	public static final int ANCHO = 950;

	public static final String BTN_COMPRAR = "Comprar";
	public static final String BTN_ELIMINAR = "Eliminar";
	public static final String BTN_BUSQ = "Busqueda";
	public static final String BTN_CARRITO = "Carrito";

	private JScrollPane scrpCarrito;
	private JTable tbCarrito;
	private DefaultTableModel tModel;
	private JButton btnComprar;
	private JButton btnEliminar;
	private JTextField txtBusq;
	private JList<Producto> list;
	private DefaultListModel<Producto> dlm;
	private JButton btnBusq;
	private JButton btnCarrito;
	private JSpinner spinner;
	private JScrollPane scrpLista;
	private TextPrompt placeholder;

	private List<ProductoCompra> carrito;

	public PCompra() {
		this.carrito = new ArrayList<ProductoCompra>();
		setForeground(new Color(0, 0, 0));
		setBackground(Color.WHITE);
		init();
	}

	private void init() {
		setLayout(null);
		setSize(ANCHO, ALTO);

		JLabel lblCarrito = new JLabel("C A R R I T O");
		lblCarrito.setHorizontalAlignment(SwingConstants.CENTER);
		lblCarrito.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblCarrito.setBounds(557, 68, 335, 35);
		add(lblCarrito);

		scrpCarrito = new JScrollPane();
		scrpCarrito.setBounds(557, 114, 335, 381);
		add(scrpCarrito);

		tbCarrito = new JTable();
		tbCarrito.setBackground(Color.WHITE);
		tbCarrito.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrpCarrito.setViewportView(tbCarrito);

		// LOS BOTONES EST�N EN LA MISMA POSICI�N, PARA QUE CUANDO
		// UN PRODUCTO SEA SELECCIONADO, EL BOT�N COMPRAR SE CAMBIAR� AL
		// DE ELIMINAR (PONIENDO VISIBLE UNO E INVISIBLE OTRO)
		btnComprar = new JButton("");
		btnComprar.setBackground(Color.WHITE);
		btnComprar.setBorder(null);
		btnComprar.setIcon(new ImageIcon(PCompra.class.getResource("/img/tick (1).png")));
		btnComprar.setToolTipText("Comprar");
		btnComprar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnComprar.setBounds(616, 556, 77, 48);
		add(btnComprar);

		btnEliminar = new JButton("");
		btnEliminar.setBackground(Color.WHITE);
		btnEliminar.setBorder(null);
		btnEliminar.setIcon(new ImageIcon(PCompra.class.getResource("/img/delete (1).png")));
		btnEliminar.setToolTipText("Eliminar");
		btnEliminar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnEliminar.setBounds(771, 556, 77, 48);
		add(btnEliminar);


		scrpLista = new JScrollPane();
		scrpLista.setBounds(88, 114, 335, 381);
		add(scrpLista);

		list = new JList<Producto>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrpLista.setViewportView(list);
		dlm = new DefaultListModel<Producto>();

		JLabel lblBusq = new JLabel("B Ú S Q U E D A");
		lblBusq.setHorizontalAlignment(SwingConstants.CENTER);
		lblBusq.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblBusq.setBounds(88, 68, 335, 35);
		add(lblBusq);

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
		spinner.setBounds(140, 562, 64, 40);
		add(spinner);

		btnCarrito = new JButton("");
		btnCarrito.setBackground(Color.WHITE);
		btnCarrito.setBorder(null);
		btnCarrito.setIcon(new ImageIcon(PCompra.class.getResource("/img/carrito-de-compras (1).png")));
		btnCarrito.setToolTipText("Carrito");
		btnCarrito.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCarrito.setBounds(243, 556, 56, 48);
		add(btnCarrito);
		// btnCarrito.setIcon(new
		// ImageIcon(PCompra.class.getResource("/img/carrito.jpg")));

		txtBusq = new JTextField();
		placeholder = new TextPrompt("Búsqueda", txtBusq);
		txtBusq.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtBusq.setBounds(331, 22, 287, 35);
		add(txtBusq);
		txtBusq.setColumns(10);
		txtBusq.setToolTipText("B�squeda");

		btnBusq = new JButton("");
		btnBusq.setToolTipText("Busqueda");
		btnBusq.setBorder(null);
		btnBusq.setBackground(Color.WHITE);
		btnBusq.setIcon(new ImageIcon(PCompra.class.getResource("/img/Search.jpeg")));
		btnBusq.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBusq.setBounds(640, 23, 38, 35);
		add(btnBusq);
		// btnBusq.setIcon(new ImageIcon(PCompra.class.getResource("/img/lupita.jpg")));

		// centrarVentana();
		configurarTabla();

	}

	public String getTxtBusq() {
		return txtBusq.getText();
	}

	private void configurarTabla() {
		tModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tModel = (DefaultTableModel) tbCarrito.getModel();

		tModel.addColumn("CANTIDAD");
		tModel.addColumn("PRODUCTO");
		tModel.addColumn("PRECIO");

		tbCarrito.getColumn("CANTIDAD").setPreferredWidth(50);
		tbCarrito.getColumn("PRODUCTO").setPreferredWidth(150);
		tbCarrito.getColumn("PRECIO").setPreferredWidth(50);

	}

	public void rellenarTabla(Producto prod, int cant) {

		Object[] fila = new Object[3];
		ProductoCompra p;

		fila[0] = cant;
		fila[1] = prod.getNombre() + "-" + prod.getTipo();
		fila[2] = prod.getPrecio();

		p = new ProductoCompra(prod, cant);
		
		carrito.add(p);
		tModel.addRow(fila);
	}

	public List<ProductoCompra> getDatosCompra() {
		return carrito;
	}

	public void showList(List<Producto> lista) {
		dlm.removeAllElements();
		list.setModel(dlm);
		dlm.addAll(lista);
	}

	private void centrarVentana() {
		setPreferredSize(new Dimension(ANCHO, ALTO));
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ventana = this.getPreferredSize();
		setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
	}

	public void hacerVisible() {
		setVisible(true);
	}

	public JList<Producto> getList() {
		return list;
	}

	public JTable getTb() {
		return tbCarrito;
	}

	public int getSpn() {
		return (int) spinner.getValue();
	}

	public String getDate() {
		long miliseconds = System.currentTimeMillis();
		Date date = new Date(miliseconds);
		
		return date.toString();
	}

	public Producto getSelectedProd() {
		Producto item = list.getSelectedValue();
		// String id = item.getIdProducto();
		System.out.println(list.getSelectedValue());
		return item;

	}

	public ProductoCompra getSelectedP() {
		ProductoCompra p;
		int row = tbCarrito.getSelectedRow();
		p = carrito.get(row);
		return p;
	}

	public void setControlador(AppControl control) {
		btnBusq.addActionListener(control);
		btnCarrito.addActionListener(control);
		btnComprar.addActionListener(control);
		btnEliminar.addActionListener(control);
	}

	public void setError(String error) {
		JOptionPane.showMessageDialog(this, error, "Error en datos", JOptionPane.ERROR_MESSAGE);

	}

	public void borrarFila(int fila) {
		tModel.removeRow(fila);

		ProductoCompra p = carrito.get(fila);

		carrito.remove(p);

	}

	public void removeElements() {
		carrito.removeAll(carrito);
		int rows = tModel.getRowCount()-1;
		
        for (int i = rows; i >= 0; i--) {          
        tModel.removeRow(tModel.getRowCount()-1);
        }
        
        System.out.println(carrito);
        
	
		
	}

}
