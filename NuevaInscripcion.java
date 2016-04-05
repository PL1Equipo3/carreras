import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class NuevaInscripcion extends JFrame {

	private JPanel contentPane;
	private final ButtonGroup bgTipoCarrera = new ButtonGroup();
	private Connection conn;
	private static Statement s;
	private static NuevaInscripcion frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run(String DNI) {
				try {
					frame = new NuevaInscripcion(DNI);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NuevaInscripcion(final String DNI) {
		//Conexión a la BD
		try {
			//Conexión a la BD
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			//Ruta absoluta o relativa como parÃ¡metro de getConnection
			conn=DriverManager.getConnection("jdbc:ucanaccess://Carreras 2ª versión.accdb");
			s = conn.createStatement();

			s.execute("SELECT * FROM Atleta WHERE (DNI='" + DNI + "')");
			ResultSet rs = s.getResultSet();
			rs.next();

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 500, 400);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));
			setContentPane(contentPane);

			JPanel panel = new JPanel();
			contentPane.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);


			JLabel lblNuevaInscripcin = new JLabel("Nueva Inscripci\u00F3n");
			lblNuevaInscripcin.setFont(new Font("Tahoma", Font.PLAIN, 18));
			lblNuevaInscripcin.setBounds(12, 13, 157, 22);
			panel.add(lblNuevaInscripcin);


			/**** Datos Personales ****/
			JLabel lblSusDatosPersonales = new JLabel("Sus datos personales:");
			lblSusDatosPersonales.setBounds(22, 48, 147, 16);
			panel.add(lblSusDatosPersonales);

			/** NOMBRE **/
			//Título
			JLabel lblNombre = new JLabel("Nombre:");
			lblNombre.setBounds(70, 77, 56, 16);
			panel.add(lblNombre);

			//Label
			JLabel lblNombreTxt = new JLabel(rs.getString(2));
			lblNombreTxt.setBounds(127, 77, 135, 16);
			panel.add(lblNombreTxt);

			/** DNI **/
			//Título
			JLabel lblDni = new JLabel("DNI:");
			lblDni.setBounds(274, 77, 56, 16);
			panel.add(lblDni);

			//Label
			JLabel lblDniTxt = new JLabel(rs.getString(1));
			lblDniTxt.setBounds(310, 77, 119, 16);
			panel.add(lblDniTxt);

			/** Feca de nacimiento **/
			//Título
			JLabel lblFechaDeNacimiento = new JLabel("Fecha de nacimiento:");
			lblFechaDeNacimiento.setBounds(70, 95, 136, 16);
			panel.add(lblFechaDeNacimiento);

			//Label
			JLabel lblFechaNacTxt = new JLabel((rs.getDate(5)).toString());
			lblFechaNacTxt.setBounds(196, 95, 66, 16);
			panel.add(lblFechaNacTxt);

			/** E-mail **/
			//Título
			JLabel lblEmail = new JLabel("E-mail:");
			lblEmail.setBounds(274, 95, 56, 16);
			panel.add(lblEmail);

			//Label
			JLabel lblEmailTxt = new JLabel(rs.getString(6));
			lblEmailTxt.setBounds(320, 95, 109, 16);
			panel.add(lblEmailTxt);


			/**** Seleccionar Carrera ****/
			JLabel lblSeleccioneLaCompeticin = new JLabel("Seleccione la competici\u00F3n a la que quiere inscribirse:");
			lblSeleccioneLaCompeticin.setBounds(22, 138, 343, 16);
			panel.add(lblSeleccioneLaCompeticin);

			final JComboBox cbListaCompeticiones = new JComboBox();
			cbListaCompeticiones.setModel(new DefaultComboBoxModel(new String[] {"Seleccione una"}));
			cbListaCompeticiones.setBounds(70, 196, 359, 22);
			panel.add(cbListaCompeticiones);

			// Por defecto se muestran las carreras de montaña
			s.execute("SELECT Nombre, Id_competición FROM Competición WHERE (Tipo='Montaña')");
			ResultSet competiciones = s.getResultSet();
			ArrayList<String> model = new ArrayList<String>();
			model.add("Seleccione una");
			while(competiciones.next()) {
				model.add(competiciones.getString(2));		
			}
			cbListaCompeticiones.setModel(new DefaultComboBoxModel(model.toArray()));

			// Tipo
			JLabel lblTipo = new JLabel("Tipo de carrera:");
			lblTipo.setBounds(70, 167, 99, 16);
			panel.add(lblTipo);

			final JRadioButton rdbtnMontaa = new JRadioButton("Monta\u00F1a");
			rdbtnMontaa.setSelected(true);
			rdbtnMontaa.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if(rdbtnMontaa.isSelected()){
						try {
							s.execute("SELECT Nombre, Id_competición FROM Competición WHERE (Tipo='Montaña')");
							ResultSet competiciones = s.getResultSet();
							ArrayList<String> model = new ArrayList<String>();
							model.add("Seleccione una");
							while(competiciones.next()) {
								model.add(competiciones.getString(2));		
							}
							cbListaCompeticiones.setModel(new DefaultComboBoxModel(model.toArray()));
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			bgTipoCarrera.add(rdbtnMontaa);
			rdbtnMontaa.setBounds(196, 163, 99, 25);
			panel.add(rdbtnMontaa);

			JRadioButton rdbtnCarreraPopular = new JRadioButton("Carrera popular");
			rdbtnCarreraPopular.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					try {
						s.execute("SELECT Nombre, Id_competición FROM Competición WHERE (Tipo='Carrera Popular')");

						ResultSet competiciones = s.getResultSet();
						ArrayList<String> model = new ArrayList<String>();
						model.add("Seleccione una");
						while(competiciones.next()) {
							model.add(competiciones.getString(2));		
						}
						cbListaCompeticiones.setModel(new DefaultComboBoxModel(model.toArray()));
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

				}
			});
			bgTipoCarrera.add(rdbtnCarreraPopular);
			rdbtnCarreraPopular.setBounds(310, 163, 119, 25);
			panel.add(rdbtnCarreraPopular);		

			final JLabel lblMensajeError = new JLabel("");
			lblMensajeError.setVerticalAlignment(SwingConstants.TOP);
			lblMensajeError.setBounds(70, 242, 359, 50);
			panel.add(lblMensajeError);

			JButton btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(cbListaCompeticiones.getSelectedIndex()==0){
						lblMensajeError.setText("Por favor selecccione una competición.");
						lblMensajeError.setForeground(Color.RED);
					}
					else{
						try {
							s.execute("SELECT * FROM Atleta WHERE (DNI='"+DNI+"')");
							ResultSet atleta = s.getResultSet();
							atleta.next();
							s.execute("SELECT * FROM Competición WHERE (Id_competición='"+cbListaCompeticiones.getSelectedItem()+"')");
							ResultSet competicion = s.getResultSet();
							competicion.next();
							Resultado success = inscripcion(atleta, competicion);
							String mensaje="";
							ResultSet inscripcion;
							switch(success){
								case FUERA_PLAZO: 
									mensaje="Inscripción fallida: fuera de plazo"; 
									lblMensajeError.setText(mensaje);
									lblMensajeError.setForeground(Color.RED);
									break;
								case PLAZAS_AGOTADAS: 
									mensaje="Inscripción fallida: plazas agotadas"; 
									lblMensajeError.setText(mensaje);
									lblMensajeError.setForeground(Color.RED);
									break;
								case YA_INSCRITO: 
									mensaje="<html>Inscripción fallida: usted ya está inscrito<br>"; 
									s.execute("SELECT * FROM Inscripción WHERE Id_inscripción='"+atleta.getString(1) + "-" + competicion.getString(1)+"'");
									inscripcion = s.getResultSet();
									inscripcion.next();		
									mensaje+=("Id de inscripción: " + inscripcion.getString(1) + "<br>"
											+"Categoría: " + inscripcion.getString(6) + "</html>");
									lblMensajeError.setText(mensaje);
									lblMensajeError.setForeground(Color.RED);
									break;
								case EXITO: 
									mensaje="<html>Inscripción exitosa<br>"; 
									s.execute("SELECT * FROM Inscripción WHERE Id_inscripción='"+atleta.getString(1) + "-" + competicion.getString(1)+"'");
									inscripcion = s.getResultSet();
									inscripcion.next();			

									mensaje+=("Id de inscripción: " + inscripcion.getString(1) + "<br>"
											+"Categoría: " + inscripcion.getString(6) + "<br>"
											+"Cuota a pagar: " + inscripcion.getFloat(4) +"€<br>" );

									Calendar calendar = Calendar.getInstance();
									calendar.setTime((Calendar.getInstance()).getTime()); // Configuramos la fecha que se recibe
									calendar.add(Calendar.DAY_OF_YEAR, 2);
									mensaje+=("Tiene 2 días para realizar la transferencia (hasta el "+(new SimpleDateFormat("yyy-MM-dd")).format(calendar.getTime())+".)</html>");
									ResultadoInscripción n = new ResultadoInscripción(mensaje);
									n.setVisible(true);
									NuevaInscripcion.this.dispose();
									break;
							}
							
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});
			btnAceptar.setBounds(363, 305, 97, 25);
			panel.add(btnAceptar);
			
			JButton btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {			
					InscripcionesMain n = new InscripcionesMain();
					n.setVisible(true);			
					NuevaInscripcion.this.dispose();
				}
			});
			btnCancelar.setBounds(12, 305, 97, 25);
			panel.add(btnCancelar);



		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	static private enum Resultado {FUERA_PLAZO, PLAZAS_AGOTADAS, YA_INSCRITO, EXITO, OTRO};
	
	static Resultado inscripcion(ResultSet atleta, ResultSet competicion){
		/* COMPROBAR QUE ESTÁ DENTRO DE PLAZO */
		try {
			s.execute("SELECT MIN([Fecha inicio]), MAX([Fecha fin]) FROM Plazos WHERE (Id_competición='" + competicion.getString(1) +"')");

			ResultSet fechas = s.getResultSet();
			fechas.next();

			//Conseguimos la fecha actual a partir de la hora del sistema
			java.util.Date fechaActual = (Calendar.getInstance()).getTime();
			//Formateador para dar el formato que reguiere sql
			SimpleDateFormat formateador = new SimpleDateFormat("yyy-MM-dd");
			String fechaSistema=formateador.format(fechaActual);

			//Pasar las fechas a tipo java.sql.Date para poder compararlas
			java.sql.Date fechaSQL = new java.sql.Date(fechaActual.getTime());
			java.sql.Date fechaMin = new java.sql.Date((fechas.getDate(1)).getTime());
			java.sql.Date fechaMax = new java.sql.Date((fechas.getDate(2)).getTime());
			//Si la fecha actual no está entre la fecha de comienzo del primer plazo de inscripción 
			//para esta competición y la de fin del último plazo, no se permite realizar la inscripción
			if ( !(fechaSQL.after(fechaMin) && fechaSQL.before(fechaMax)) ) {
				System.out.println("Fuera de plazo");
				return Resultado.FUERA_PLAZO;
			}



			/* COMPROBAR QUE HAY PLAZAS LIBRES */
			s.execute("SELECT COUNT(*) FROM Inscripción WHERE (Id_competición='" + competicion.getString(1) +"')");
			ResultSet inscritos = s.getResultSet();
			inscritos.next();
			//Si el número de atletas inscritos es igual (o mayor) al de plazas disponibles es que ya no hay más
			//inscritos.getInt(1)=número de inscripciones registradas en la BD para esa competición
			//competicion.getInt(4)=número máximo de plazas disponibles para la competición seleccionada
			if (inscritos.getInt(1)>=competicion.getInt(4)) {
				System.out.println("Plazas agotadas");
				return Resultado.FUERA_PLAZO;
			}



			/* COMPROBAR QUE EL ATLETA NO ESTÁ YA INSCRITO EN LA COMPETICIÓN */
			s.execute("SELECT * FROM Inscripción WHERE (Id_atleta='" + atleta.getString(1) +"' AND Id_competición='" + competicion.getString(1) + "')");
			if ((s.getResultSet()).next()) {
				System.out.println("Usted ya está inscrito en esta competición");
				return Resultado.YA_INSCRITO;
			}

			//Calcular la categoría a la que pertenece el atleta
			//Parámetros: String Id_Inscripcion, String Id_competicion, String Id_atleta
			String categoria = CalculaCategoria(atleta.getString(1) + "-" + competicion.getString(1), competicion.getString(1), atleta.getString(1), atleta.getDate(5), atleta.getString(4));

			//Calcular la cuota a pagar
			//Parámetros: String Id_Inscripcion, String Id_competicion, String Id_atleta
			float cuota = Pagar(atleta.getString(1)+"-"+competicion.getString(1), competicion.getString(1), atleta.getString(1), fechaSQL);

			//Si está dentro de plazo, hay plazas disponibles y el atleta no está ya inscrito, registrar la inscripción
			s.execute("INSERT INTO Inscripción VALUES ('"
					+ atleta.getString(1) + "-" + competicion.getString(1) + "', "  //Id_inscripción
					+ "#" + fechaSistema + "#, "									//Fecha de inscripción
					+ "'Preinscrito', "												//Estado
					+ cuota + ", "													//Cuota
					+ "'Tranferencia', "											//Forma de pago
					+ "'" + categoria + "', "										//Categoria
					+ "'" + atleta.getString(1) + "', "								//Id_atleta
					+ "'" + competicion.getString(1) + "')" );						//Id_competición


			s.execute("SELECT * FROM Inscripción WHERE Id_inscripción='"+atleta.getString(1) + "-" + competicion.getString(1)+"'");
			ResultSet inscripcion = s.getResultSet();
			inscripcion.next();			

			System.out.println("\tId de inscripción: " + inscripcion.getString(1) + "\n"
					+"\tCategoría: " + inscripcion.getString(6) + "\n"
					+"\tCuota a pagar: " + inscripcion.getFloat(4) +"€" );

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaActual); // Configuramos la fecha que se recibe
			calendar.add(Calendar.DAY_OF_YEAR, 2);
			System.out.println("\tTiene 2 días para realizar la transferencia (hasta el "+formateador.format(calendar.getTime())+".)");
			return Resultado.EXITO;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Resultado.OTRO;
	}

	private static float Pagar(String Id_inscripción, String Id_competición, String Id_atleta, Date fechaInscripcion) throws Exception{  

		float Cuota_abonada = 0;
		String orden;
		Date  fechaInicio, fechaFin;
		ResultSet rs;

		/*
			//Actualiza el estado de la inscripción a Preinscrito
			orden = "UPDATE Inscripción SET Estado = 'Preinscrito' WHERE "
					+ "(Id_inscripción = '"+Id_inscripción+"')";
			s.execute(orden);
			//Actualiza la forma de pago de la inscripción a Transferencia
			orden = "UPDATE Inscripción SET [Forma de pago] = 'Transferencia' WHERE "
					+ "(Id_inscripción = '"+Id_inscripción+"')";
			s.execute(orden);
			//Obtener la fecha de inscripción
			orden = "SELECT [Fecha inscripción] FROM Inscripción WHERE "
					+ "(Id_inscripción = '"+Id_inscripción+"')";
			rs=s.executeQuery(orden);
		    fechaInscripcion=rs.getDate(1);
		 */

		//Modificar el precio según el plazo en el que se realiza la inscripción
		//----------------------------------------------------------------------

		//Obtener las fechas de los plazos
		orden = "SELECT * FROM Plazos WHERE (Id_competición = '"+Id_competición+"')";
		rs=s.executeQuery(orden);
		rs.next();

		fechaInicio=rs.getDate(2);
		fechaFin=rs.getDate(3);
		//System.out.println(fechaInicio);
		//System.out.println(fechaFin);
		
		String sInicio = (String) (fechaInicio.toString()).subSequence(0, 9);
		String sFin = (String) (fechaFin.toString()).subSequence(0, 9);
		String sInscripcion = (String) (fechaInscripcion.toString()).subSequence(0, 9);
		

		boolean cond = !( (fechaInicio.before(fechaInscripcion) || sInicio.equals(sInscripcion)) 
				&& (fechaFin.after(fechaInscripcion) || sFin.equals(sInscripcion)) );

		//Comprobar en que plazo se realizó la inscripción
		while( cond ) {
			rs.next();
			fechaInicio=rs.getDate(2);
			fechaFin=rs.getDate(3);

			cond = !( (fechaInicio.before(fechaInscripcion) || sInicio.equals(sInscripcion)) 
					&& (fechaFin.after(fechaInscripcion) || sFin.equals(sInscripcion)) );
		}
		
		Cuota_abonada = rs.getFloat(4);
		//System.out.println(Cuota_abonada);

		//Modificar el precio según si está federado o no
		orden = "SELECT Tipo FROM Competición WHERE (Id_competición = '"+Id_competición+"')";
		rs=s.executeQuery(orden);
		rs.next();
		if((rs.getString(1)).equals("Montaña")){
			orden = "SELECT [Número federado] FROM Atleta WHERE (DNI='"+Id_atleta+"')";
			rs=s.executeQuery(orden);
			rs.next();
			if(rs.getString(1)!=null){
				Cuota_abonada=Cuota_abonada-15;	
			}
		}

		//Actualiza el precio en la Cuota abonada
		/*orden = "UPDATE Inscripción SET [Cuota abonada] = "+Cuota_abonada+" WHERE (Id_inscripción = '"+Id_inscripción+"')";
			s.execute(orden);
			System.out.println(Cuota_abonada);
		 */
		return Cuota_abonada;
	}
	
	private static String CalculaCategoria (String Id_Inscripcion, String Id_competicion, String Id_atleta, Date FechaNac, String sexo) throws Exception{			
		//Variables y ED
		String SQL, FechaNC = null, FechaN = null, C=null;
		ArrayList<String> Categorias = new ArrayList<String>();
		ArrayList<Integer> edadMin = new ArrayList<Integer>();
		ArrayList<Integer> edadMax = new ArrayList<Integer>();
		int Cont=0, varEdadMin, varEdadMax;		


		//Objeto SimpleDateFormat para pasar de String a Date
		//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		/*
		//Seleccionar corredorde la BD
		SQL= "SELECT * FROM Atleta";	
		s.execute(SQL);
		ResultSet rs = s.getResultSet();
		while((rs!=null) && (rs.next())){
			FechaNC=rs.getString(5);   //El 5 porque es donde esta la fecha de nacimiento
			sexo=rs.getString(4);      //En el 4 está el sexo
		}		
		*/

		//FechaN=FechaNC.substring(0, 10);						//Cortar String			
		//java.util.Date FechaNac = formatter.parse(FechaN);		//Pasar String a Date			
		int Edad = CalculaEdad(FechaNac);						//Calcular Edad

		//Consulta SQL para saber las categorías 
		SQL="SELECT * FROM Categoría INNER JOIN [Categorías de la competición] ON Categoría.Id_categoría=[Categorías de la competición].Id_categoría WHERE Categoría.Sexo='" + sexo + "' AND [Categorías de la competición].Id_competición='" + Id_competicion +"'";
		s.execute(SQL);
		ResultSet rs = s.getResultSet();
		while((rs!=null) && (rs.next())){			//Almacenar categorías y edades
			Categorias.add(rs.getString(2));
			edadMin.add(rs.getInt(3));
			edadMax.add(rs.getInt(4));
			Cont++;
		}

		//Calcular en que competición está
		for (int i=0; i<Cont; i++){
			varEdadMin=edadMin.get(i);
			varEdadMax=edadMax.get(i);
			if (varEdadMax == 0) varEdadMax=200;	//Control del valor máx de la última categoría que es 0
			if ((Edad>=varEdadMin) && (Edad<=varEdadMax)){
				C=Categorias.get(i);
			}
		}

		//Escribirla en la BD
		/*
			SQL="UPDATE Inscripción SET Categoría='" + C +"' WHERE Id_Inscripción='" + Id_Inscripcion + "'";
			s.execute(SQL);
		 */
		return C;

	}
	
	private static int CalculaEdad(java.util.Date fechaNac){
		int Edad = 0;
		Calendar dob = Calendar.getInstance();
		dob.setTime(fechaNac);
		Calendar today = Calendar.getInstance();
		Edad = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		if (today.get(Calendar.DAY_OF_YEAR) <= dob.get(Calendar.DAY_OF_YEAR))
			Edad--;
		return Edad;
	}
}
