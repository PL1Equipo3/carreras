import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.GregorianCalendar;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class RegistroAtleta extends JFrame {

	private JPanel contentPane;
	private JTextField tfNombre;
	private JTextField tfApellido;
	private JTextField tfDNI;
	private JTextField tfEmail;
	private JTextField tfTelfono;
	private JTextField tfDireccin;
	private JTextField tfCP;
	private JTextField tfLocalidad;
	private JTextField tfNmeroDeFederado;
	private JLabel lblNmeroDeFederado;
	
	private static RegistroAtleta frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new RegistroAtleta();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RegistroAtleta() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 409);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		tfNombre = new JTextField();
		tfNombre.setBounds(113, 48, 116, 22);
		panel.add(tfNombre);
		tfNombre.setColumns(10);

		JLabel lblTituloRegistro = new JLabel("Registro de nuevo atleta");
		lblTituloRegistro.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTituloRegistro.setBounds(12, 13, 203, 22);
		panel.add(lblTituloRegistro);

		/******* Nombre ******/
		//Label
		final JLabel lblNombre = new JLabel("Nombre*: ");
		lblNombre.setBounds(22, 51, 62, 16);
		panel.add(lblNombre);

		//TextField

		/******* Apellidos ******/
		//Label
		final JLabel lblApellidos = new JLabel("Apellidos*:");
		lblApellidos.setBounds(250, 45, 92, 22);
		panel.add(lblApellidos);

		//TextField
		tfApellido = new JTextField();
		tfApellido.setBounds(341, 48, 116, 22);
		panel.add(tfApellido);
		tfApellido.setColumns(10);

		/******* DNI ******/
		//Label
		final JLabel lblDni = new JLabel("DNI*:");
		lblDni.setBounds(22, 86, 56, 16);
		panel.add(lblDni);

		//TextField
		tfDNI = new JTextField();
		tfDNI.setBounds(113, 83, 116, 22);
		panel.add(tfDNI);
		tfDNI.setColumns(10);

		/******* Sexo ******/
		//Label
		final JLabel lblSexo = new JLabel("Sexo*:");
		lblSexo.setBounds(250, 80, 56, 22);
		panel.add(lblSexo);

		//ComboBox
		final JComboBox cbSexo = new JComboBox();
		cbSexo.setModel(new DefaultComboBoxModel(new String[] {"Seleccione", "Masculino", "Femenino"}));
		cbSexo.setSelectedIndex(0);
		cbSexo.setBounds(341, 80, 116, 22);
		panel.add(cbSexo);

		/******* Fecha de nacimiento ******/
		final JLabel lblFechaDeNacimiento = new JLabel("Fecha de nacimiento*:");
		lblFechaDeNacimiento.setBounds(22, 129, 153, 16);
		panel.add(lblFechaDeNacimiento);

		//ComboBoxes
		final JComboBox cbDía = new JComboBox();
		cbDía.setModel(new DefaultComboBoxModel(new String[] {"-"}));
		cbDía.setSelectedIndex(0);
		final JComboBox cbMes = new JComboBox();
		cbMes.setModel(new DefaultComboBoxModel(new String[] {"-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));
		cbMes.setSelectedIndex(0);
		final JComboBox cbAño = new JComboBox();

		//Vectores para los días
		final String[] Dias31 = new String[] {"-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
		final String[] Dias30 = new String[] {"-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
		final String[] Dias28 = new String[] {"-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29"};
		final String[] Dias29 = new String[] {"-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"};			

		/**** AÑO ****/
		//Label
		JLabel lblAño = new JLabel("Año");
		lblAño.setBounds(304, 115, 46, 14);
		panel.add(lblAño);

		//ComboBox
		cbAño.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cbMes.getSelectedItem().equals("2")){
					GregorianCalendar calendario = new GregorianCalendar();
					int año = Integer.parseInt((String) cbAño.getSelectedItem());
					if(calendario.isLeapYear( año ) ){
						cbDía.setModel(new DefaultComboBoxModel(Dias28));
					}
					else{
						cbDía.setModel(new DefaultComboBoxModel(Dias29));
					}
				}
			}
		});
		cbAño.setModel(new DefaultComboBoxModel(new String[] {"-", "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000", "1999", "1998", "1997", "1996", "1995", "1994", "1993", "1992", "1991", "1990", "1989", "1988", "1987", "1986", "1985", "1984", "1983", "1982", "1981", "1980", "1979", "1978", "1977", "1976", "1975", "1974", "1973", "1972", "1971", "1970", "1969", "1968", "1967", "1966", "1965", "1964", "1963", "1962", "1961", "1960", "1959", "1958", "1957", "1956", "1955", "1954", "1953", "1952", "1951", "1950"}));
		cbAño.setSelectedIndex(0);
		cbAño.setBounds(304, 128, 63, 20);
		panel.add(cbAño);


		/**** MES ****/
		//Label
		JLabel lblMes = new JLabel("Mes");
		lblMes.setBounds(235, 115, 46, 14);
		panel.add(lblMes);

		//ComboBox
		cbMes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cbMes.getSelectedItem().equals("1") || cbMes.getSelectedItem().equals("3") || cbMes.getSelectedItem().equals("5") || cbMes.getSelectedItem().equals("7") || cbMes.getSelectedItem().equals("8") || cbMes.getSelectedItem().equals("10") || cbMes.getSelectedItem().equals("12")){
					cbDía.setModel(new DefaultComboBoxModel(Dias31));
				}
				if(cbMes.getSelectedItem().equals("4") || cbMes.getSelectedItem().equals("6") || cbMes.getSelectedItem().equals("9") || cbMes.getSelectedItem().equals("11")){
					cbDía.setModel(new DefaultComboBoxModel(Dias30));
				}
				if(cbMes.getSelectedItem().equals("2")){
					GregorianCalendar calendario = new GregorianCalendar();
					int año = Integer.parseInt((String) cbAño.getSelectedItem());
					if(calendario.isLeapYear( año ) ){
						cbDía.setModel(new DefaultComboBoxModel(Dias28));
					}
					else{
						cbDía.setModel(new DefaultComboBoxModel(Dias29));
					}
				}
			}
		});
		cbMes.setBounds(234, 128, 47, 20);
		panel.add(cbMes);


		/**** DÍA ****/
		//Label
		JLabel lblDía = new JLabel("Día");
		lblDía.setBounds(167, 115, 46, 14);
		panel.add(lblDía);

		//ComboBox
		if(cbMes.getSelectedItem().equals("1") || cbMes.getSelectedItem().equals("3") || cbMes.getSelectedItem().equals("5") || cbMes.getSelectedItem().equals("7") || cbMes.getSelectedItem().equals("8") || cbMes.getSelectedItem().equals("10") || cbMes.getSelectedItem().equals("12")){
			cbDía.setModel(new DefaultComboBoxModel(Dias31));
		}
		if(cbMes.getSelectedItem().equals("4") || cbMes.getSelectedItem().equals("6") || cbMes.getSelectedItem().equals("9") || cbMes.getSelectedItem().equals("11")){
			cbDía.setModel(new DefaultComboBoxModel(Dias30));
		}
		if(cbMes.getSelectedItem().equals("2")){
			GregorianCalendar calendario = new GregorianCalendar();
			int año = Integer.parseInt((String) cbAño.getSelectedItem());
			if(calendario.isLeapYear( año ) ){
				cbDía.setModel(new DefaultComboBoxModel(Dias28));
			}
			else{
				cbDía.setModel(new DefaultComboBoxModel(Dias29));
			}
		}
		cbDía.setBounds(167, 128, 47, 20);
		panel.add(cbDía);

		/******* E-mail ******/
		//Label
		final JLabel lblEmail = new JLabel("E-mail*:");
		lblEmail.setBounds(22, 164, 70, 16);
		panel.add(lblEmail);

		//TextField
		tfEmail = new JTextField();
		tfEmail.setBounds(113, 161, 116, 22);
		panel.add(tfEmail);
		tfEmail.setColumns(10);

		/****** Teléfono ******/
		//Label
		JLabel lblTelfono = new JLabel("Tel\u00E9fono:");
		lblTelfono.setBounds(250, 161, 56, 22);
		panel.add(lblTelfono);

		//TextField
		tfTelfono = new JTextField();
		tfTelfono.setBounds(341, 161, 116, 22);
		panel.add(tfTelfono);
		tfTelfono.setColumns(10);

		/**** Dirección ****/
		//Label
		JLabel lblDireccin = new JLabel("Direcci\u00F3n:");
		lblDireccin.setBounds(22, 246, 70, 16);
		panel.add(lblDireccin);

		//TextField
		tfDireccin = new JTextField();
		tfDireccin.setBounds(113, 243, 344, 22);
		panel.add(tfDireccin);
		tfDireccin.setColumns(10);

		/**** C.P. ****/
		//Label
		JLabel lblCP = new JLabel("C.P.:");
		lblCP.setBounds(273, 281, 56, 16);
		panel.add(lblCP);

		//TextField
		tfCP = new JTextField();
		tfCP.setBounds(304, 278, 76, 22);
		panel.add(tfCP);
		tfCP.setColumns(10);

		/**** Localidad ****/
		//Label
		JLabel lblLocalidad = new JLabel("Localidad:");
		lblLocalidad.setBounds(22, 278, 80, 16);
		panel.add(lblLocalidad);

		//TextField
		tfLocalidad = new JTextField();
		tfLocalidad.setBounds(113, 278, 132, 22);
		panel.add(tfLocalidad);
		tfLocalidad.setColumns(10);

		/**** Número de Federado ****/
		//Label
		lblNmeroDeFederado = new JLabel("N\u00FAmero de federado*:");
		lblNmeroDeFederado.setBounds(205, 207, 132, 16);
		panel.add(lblNmeroDeFederado);
		lblNmeroDeFederado.setVisible(false);

		//TextField
		tfNmeroDeFederado = new JTextField();
		tfNmeroDeFederado.setBounds(341, 204, 116, 22);
		panel.add(tfNmeroDeFederado);
		tfNmeroDeFederado.setColumns(10);
		tfNmeroDeFederado.setVisible(false);

		/**** Federado? ****/
		final JCheckBox chckbxFederado = new JCheckBox("\u00BFEst\u00E1 usted federado?");
		chckbxFederado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxFederado.isSelected()){
					lblNmeroDeFederado.setVisible(true);
					tfNmeroDeFederado.setVisible(true);
				}
				else{
					lblNmeroDeFederado.setVisible(false);
					tfNmeroDeFederado.setVisible(false);
				}
			}
		});
		chckbxFederado.setHorizontalAlignment(SwingConstants.TRAILING);
		chckbxFederado.setBounds(22, 203, 155, 25);
		panel.add(chckbxFederado);

		/**** Mensajes de error ****/
		final JLabel lblMesnajeError = new JLabel("");
		lblMesnajeError.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMesnajeError.setBounds(123, 318, 216, 16);
		panel.add(lblMesnajeError);

		/**** Botón Aceptar ****/
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean ok = true;
				if(tfNombre.getText().equals("")) {
					ok = false;
					lblNombre.setForeground(Color.RED);
				}
				if(tfApellido.getText().equals("")) {
					ok = false;
					lblApellidos.setForeground(Color.RED);
				}
				if(tfDNI.getText().equals("")) {
					ok = false;
					lblDni.setForeground(Color.RED);
				}
				if(cbSexo.getSelectedIndex()==0) {
					ok = false;
					lblSexo.setForeground(Color.RED);
				}
				if(tfEmail.getText().equals("")) {
					ok = false;
					lblEmail.setForeground(Color.RED);
				}
				if(cbDía.getSelectedIndex()==0 || cbMes.getSelectedIndex()==0 || cbAño.getSelectedIndex()==0) {
					ok = false;
					lblFechaDeNacimiento.setForeground(Color.RED);
				}
				if(chckbxFederado.isSelected() && tfNmeroDeFederado.getText().equals("")) {
					ok = false;
					lblNmeroDeFederado.setForeground(Color.RED);
				}

				if(ok==false){
					lblMesnajeError.setText("Introduzca todos los campos obligatorios.");
					lblMesnajeError.setForeground(Color.RED);
				}
				else{
					//Conexión a la BD
					try {
						//Conexión a la BD
						Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
						//Ruta absoluta o relativa como parÃ¡metro de getConnection
						Connection conn=DriverManager.getConnection("jdbc:ucanaccess://Carreras 2ª versión.accdb");
						Statement s = conn.createStatement();
						String mensaje;
						
						s.execute("SELECT * FROM Atleta WHERE (DNI='" + tfDNI.getText() + "')");
						//Si no se había registrado antes
						if ((s.getResultSet()).next()==false) {
							String nuevoAtleta = "INSERT INTO Atleta (DNI, Nombre, Apellidos, Sexo, [Fecha nacimiento], [E-mail]) VALUES ("
									+ "'"+tfDNI.getText()+"', "   //DNI
									+ "'"+tfNombre.getText()+"', "   //Nombre
									+ "'"+tfApellido.getText()+"', "   //Apellidos
									+ "'"+cbSexo.getSelectedItem()+"', "   //Sexo
									+ "#"+cbAño.getSelectedItem()+"-"+cbMes.getSelectedItem()+"-"+cbDía.getSelectedItem()+"#"+", "    //Fecha de nacimiento
									+ "'"+tfEmail.getText()+"')";   //E-mail
							s.execute(nuevoAtleta);
							
							if(!tfTelfono.getText().equals("")){
								s.execute("UPDATE Atleta SET Teléfono="+Integer.parseInt(tfTelfono.getText())+" WHERE (DNI='"+tfDNI.getText()+"')");
							}
							if(!tfNmeroDeFederado.getText().equals("")){
								s.execute("UPDATE Atleta SET [Número federado]="+Integer.parseInt(tfNmeroDeFederado.getText())+" WHERE (DNI='"+tfDNI.getText()+"')");
							}
							if(!tfDireccin.getText().equals("")){
								s.execute("UPDATE Atleta SET Dirección='"+tfDireccin.getText()+"' WHERE (DNI='"+tfDNI.getText()+"')");
							}
							if(!tfCP.getText().equals("")){
								s.execute("UPDATE Atleta SET [Código postal]="+tfCP.getText()+" WHERE (DNI='"+tfDNI.getText()+"')");
							}
							if(!tfLocalidad.getText().equals("")){
								s.execute("UPDATE Atleta SET Localidad='"+tfLocalidad.getText()+"' WHERE (DNI='"+tfDNI.getText()+"')");
							}

							s.execute("SELECT * FROM Atleta WHERE (DNI='" + tfDNI.getText() + "')");
							ResultSet atleta = s.getResultSet();
							mensaje="Registro exitoso";
						}
						//Ya está registrado
						else{
							// TODO pop-up que avise de que ya registrado y muestre datos
							mensaje="Registro fallido: atleta ya registrado";
						}
						if (mensaje.equals("Registro exitoso")){
							NuevaInscripcion n = new NuevaInscripcion(tfDNI.getText());
							n.setVisible(true);
						}
						else {
							InscripcionesMain n = new InscripcionesMain();
							n.setVisible(true);
						}
						ResultadoRegistro r = new ResultadoRegistro(mensaje);
						r.setVisible(true);
						RegistroAtleta.this.dispose();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnAceptar.setBounds(363, 314, 97, 25);
		panel.add(btnAceptar);
		
		JButton btnAtras = new JButton("Atr\u00E1s");
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InscripcionesMain n = new InscripcionesMain();
				n.setVisible(true);
				RegistroAtleta.this.dispose();
			}
		});
		btnAtras.setBounds(12, 314, 97, 25);
		panel.add(btnAtras);


	}
}
