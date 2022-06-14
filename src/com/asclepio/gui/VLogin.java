package com.asclepio.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextField;

import com.asclepio.control.AppControl;
import com.asclepio.model.Usuario;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class VLogin extends JFrame {
	public static final String BTN_LOGIN = "LOGIN";
	private static final int ANCHO = 700;
	private static final int ALTO = 500;
	
	
	private JTextField txtUsuario;
	private JPasswordField txtPwd;
	private JButton btnLogin;
	private TextPrompt placeholder;
	
	public VLogin() {
		initComponents();
	}

	private void initComponents() {
		setTitle("User Login");
		getContentPane().setLayout(null);
		
		txtUsuario = new JTextField();
		placeholder = new TextPrompt("ID Usuario", txtUsuario); 
		txtUsuario.setBounds(195, 214, 157, 35);
		getContentPane().add(txtUsuario);
		txtUsuario.setColumns(10);
		
		txtPwd = new JPasswordField();
		placeholder = new TextPrompt("Password", txtPwd);
		txtPwd.setBounds(195, 292, 157, 35);
		getContentPane().add(txtPwd);
		
		btnLogin = new JButton(BTN_LOGIN);
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnLogin.setBounds(222, 365, 115, 29);
		getContentPane().add(btnLogin);
	
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setIcon(new ImageIcon(VLogin.class.getResource("/img/image.png")));
		lblNewLabel_1.setBounds(518, 0, 235, 472);
		getContentPane().add(lblNewLabel_1);
		
		JPanel panel = new JPanel(null);
		panel.setBackground(new Color(68, 174, 178));
		panel.setBounds(0, 63, 519, 98);
		getContentPane().add(panel);
		
		JLabel lblNewLabel = new JLabel("A S C L E P I O");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 38));
		lblNewLabel.setBounds(171, 31, 299, 37);
		panel.add(lblNewLabel);
		lblNewLabel.setForeground(Color.BLACK);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(22, 0, 115, 90);
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setIcon(new ImageIcon(VLogin.class.getResource("/img/logo.png")));
		
		setSize(ANCHO, ALTO);
		centrarVentana();
	}
	
	

	public JPasswordField getTxtPwd() {
		return txtPwd;
	}

	private void centrarVentana() {
		
		setPreferredSize(new Dimension(ANCHO, ALTO));  
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();               
		Dimension ventana = this.getPreferredSize();               
		setLocation((pantalla.width - ventana.width) / 2,  (pantalla.height - ventana.height) / 2);
				
	}
	
	
	
	
	public JTextField getTxtUsuario() {
		return txtUsuario;
	}

	public JButton getBtnLogin() {
		return btnLogin;
	}

	public void hacerVisible() {
		
		setVisible(true);
	}
	
	
	public void cleanData() {
		
		txtUsuario.setText("");
		txtPwd.setText("");
	}

	public void setControlador(AppControl control) {
		btnLogin.addActionListener(control);
		txtPwd.addActionListener(control);
		txtUsuario.addActionListener(control);
		
	}

	public Usuario comprobarDatos() {
		Usuario user = null;
		
		try {
			int id = Integer.parseInt(txtUsuario.getText().trim());
			
			if (id <= 0) {
				setError("El Id usuario debe ser un número entero positivo");
				cleanData();
			}else {
				
				String pwd = txtPwd.getText().trim();
				
				String error = Usuario.validarPwd(pwd);
				
				if (!error.isBlank()) {
					setError(error);
				}else {
					user = new Usuario(id, pwd);
				}
				
			}
		
		}catch (NumberFormatException e) {
			setError("El Id usuario debe ser numérico");
			cleanData();
		}
		
		
		
		
		return user;
	}

	public void setError(String error) {
		JOptionPane.showMessageDialog(this, error, "Error en datos", JOptionPane.ERROR_MESSAGE);
		
	}
}
