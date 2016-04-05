import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JTextField;

public class RecepcionPagosGraf extends JFrame {

	private JPanel contentPane;
	private JTextPane textPane;
	static Statement s;

	private static File archivo;
	private static BufferedReader br = null;	
	private static PrintWriter pw = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecepcionPagosGraf frame = new RecepcionPagosGraf();
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
	public RecepcionPagosGraf() {
		try {
			//Conexión a la BD
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			//Ruta absoluta o relativa como parÃ¡metro de getConnection
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://Carreras 2ª versión.accdb");

			s = conn.createStatement();
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 537, 398);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblRecepcinDePagos = new JLabel("Recepci\u00F3n de pagos");
		lblRecepcinDePagos.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblRecepcinDePagos.setBounds(12, 13, 164, 27);
		panel.add(lblRecepcinDePagos);

		final JLabel lblMensaje = new JLabel("Seleccione un archivo .csv:");
		lblMensaje.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblMensaje.setBounds(22, 47, 459, 16);
		panel.add(lblMensaje);

		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setBounds(0, 65, 509, 277);
		panel.add(fileChooser);

		textPane = new JTextPane();
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		textPane.setBounds(32, 76, 449, 213);
		panel.add(textPane);
		textPane.setVisible(false);

		final JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Organizador n = new Organizador();
				n.setVisible(true);
				RecepcionPagosGraf.this.dispose();
			}
		});
		btnAceptar.setBounds(400, 303, 97, 25);
		panel.add(btnAceptar);

		textField = new JTextField();
		textField.setBounds(32, 76, 449, 27);
		panel.add(textField);
		textField.setColumns(10);
		textField.setVisible(false);

		final JButton btnSiguiente = new JButton("Siguiente");
		btnSiguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				try {
					if(textField.getText().equals("")){
						lblMensaje.setForeground(Color.RED);
					}
					else {
						String out = textField.getText();
						String path = (archivo.getParent()).replace("\\", "/");
						pw =new PrintWriter(new FileWriter(path+"/"+out+".csv"));
						lblMensaje.setForeground(Color.BLACK);
						lblMensaje.setText("Actualización de la Base de Datos:");
						textField.setVisible(false);
						btnSiguiente.setVisible(false);
						textPane.setVisible(true);
						btnAceptar.setVisible(true);
						recepcion();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSiguiente.setBounds(400, 116, 97, 25);
		panel.add(btnSiguiente);
		textPane.setVisible(false);

		fileChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				archivo = fileChooser.getSelectedFile();
				try {
					if(e.getActionCommand().equals("CancelSelection")){
						Organizador n = new Organizador();
						n.setVisible(true);
						RecepcionPagosGraf.this.dispose();
					}
					else {
						if(archivo.getName().endsWith(".csv")) {
							br = new BufferedReader(new FileReader (archivo));

							fileChooser.setVisible(false);
							textField.setVisible(true);
							btnSiguiente.setVisible(true);
							lblMensaje.setForeground(Color.BLACK);
							lblMensaje.setText("¿Cómo quiere que se llame el archivo de salida?:");
						}
						else{
							lblMensaje.setForeground(Color.RED);
							System.out.println("FILE NOT FOUND");
						}
					}
				} catch (Exception e1) { e1.printStackTrace();	}
			}
		});
	}

	enum error { ID_INCORRECTO, YA_INSCRITO, YA_CANCELADA, FUERA_DE_PLAZO, PAGO_DE_MAS, PAGO_DE_MENOS, ERROR_FORMATO };
	String[] errores = {"El Id de inscripción es incorrecto",
			"Ya se ha pagado está inscripción",
			"La inscripción estaba cancelada",
			"Pago realizado fuera de plazo",
			"Pago superior a la cuota, se devuleve la diferencia",
			"Pago insuficiente",
	"Error en el archivo de entrada"};
	private JTextField textField;


	/*----------METODOS------------*/
	void recepcion () {
		// Lectura de fichero        
		try {
			String registro;
			String[] reg;
			String id_inscripcion;
			Date fecha_pago = null;
			float abonado = 0;
			String estado;

			String text="<html>";

			// Abrir fichero de lectura
			registro = br.readLine();

			while (registro != null) {
				reg = registro.split(",");
				id_inscripcion = reg[0];
				boolean seguir=true;

				try{
					SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
					fecha_pago = formatoDelTexto.parse(reg[1]);				
					abonado = Float.parseFloat(reg[2]);
				}
				catch (Exception e){
					text+=id_inscripcion+" - Error de formato<br>";
					System.out.println("Error de formato");
					devolución(id_inscripcion, abonado, error.ERROR_FORMATO);
					seguir=false;
				}

				if(seguir) {
					ResultSet rs = s.executeQuery("SELECT Id_inscripción, [Fecha inscripción], [Cuota abonada], Estado FROM Inscripción WHERE (Id_inscripción='" + id_inscripcion +"')");

					if (rs.next()==false) {
						text+=id_inscripcion+" - Id_inscripción incorrecto<br>";
						System.out.println("Id_inscripción incorrecto");
						// Error pago: Devolver transferencia
						devolución(id_inscripcion, abonado, error.ID_INCORRECTO);
					}
					else {
						if ((rs.getString(4)).equals("Inscrito")){
							text+=id_inscripcion+" - Ya pagado<br>";
							System.out.println("Ya pagado");
							// Error pago: Devolver transferencia
							devolución(id_inscripcion, abonado, error.YA_INSCRITO);
						}
						else {
							if((rs.getString(4)).equals("Cancelado")) {
								text+=id_inscripcion+" - Ya cancelado<br>";
								System.out.println("Cancelado");
								// Error pago: Devolver transferencia
								devolución(id_inscripcion, abonado, error.YA_CANCELADA);
							}

							else {
								Date fecha_inscripción = rs.getDate(2);
								Calendar fecha_limite = Calendar.getInstance();
								fecha_limite.setTime(fecha_inscripción);
								fecha_limite.add(Calendar.DAY_OF_YEAR, 2);

								if (fecha_pago.after(fecha_limite.getTime())){
									text+=id_inscripcion+" - Pago fuera de plazo<br>";
									System.out.println("Pago fuera de plazo");
									// Error pago: Devolver transferencia
									devolución(id_inscripcion, abonado, error.FUERA_DE_PLAZO);
									s.execute("UPDATE Inscripción SET Estado='Cancelado' WHERE (Id_inscripción='" + id_inscripcion +"')");
								}
								else {
									float cuota = rs.getFloat(3);
									if (abonado < cuota) {
										text+=id_inscripcion+" - Pago insuficiente<br>";
										System.out.println("Pago insuficiente");
										// Error pago: Devolver transferencia
										devolución(id_inscripcion, abonado, error.PAGO_DE_MENOS);
										s.execute("UPDATE Inscripción SET Estado='Cancelado' WHERE (Id_inscripción='" + id_inscripcion +"')");
									}
									else {
										if (abonado > cuota) {
											text+=id_inscripcion+" - Pago de más<br>";
											System.out.println("Pago de más");
											// Error pago: Devolver transferencia
											devolución(id_inscripcion, abonado-cuota, error.PAGO_DE_MAS);
											s.execute("UPDATE Inscripción SET Estado='Cancelado' WHERE (Id_inscripción='" + id_inscripcion +"')");
										}
										else {
											s.execute("UPDATE Inscripción SET Estado='Inscrito' WHERE (Id_inscripción='" + id_inscripcion +"')");
											text+=id_inscripcion+" - Pago correcto. Inscrito.<br>";
											System.out.println("Pago correcto");
											// Necesario ??????
											//s.executeQuery("UPDATE Inscripción SET [Cuota abonada]="+ abonado +" WHERE (Id_competición='" + id_inscripcion +"')");
										}
									}
								}
							}
						}
					}				
				}
				registro = br.readLine();
			}
			System.out.println("Fin de la actualización");
			text+="</html>";
			textPane.setText(text);

		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			// En el finally cerramos los ficheros, para asegurarnos de que se cierran tanto si todo va bien como si salta una excepcion.
			try{                    
				if( null != br ){   
					br.close();     
					pw.close();
				}                  
			}catch (Exception e2){ 
				e2.printStackTrace();
			}
		}
	}

	// Escritura fichero

	private void devolución (String Id, float devolución, error e) {
		// Documentación ficheros: http://chuwiki.chuidiang.org/index.php?title=Lectura_y_Escritura_de_Ficheros_en_Java
		int i = 0;
		switch(e){
		case ID_INCORRECTO: i=0;
		case YA_INSCRITO: i=1;
		case YA_CANCELADA: i=2;
		case FUERA_DE_PLAZO: i=3;
		case PAGO_DE_MAS: i=4;
		case PAGO_DE_MENOS: i=5;
		case ERROR_FORMATO: i=6;
		}
		pw.println(Id+","+devolución+","+"Motivo de la devolución: "+ errores[i]);
	}
}
