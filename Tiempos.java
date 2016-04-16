import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Tiempos extends JFrame {

	private JPanel contentPane;
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
					Tiempos frame = new Tiempos();
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
	public Tiempos() {

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
		setBounds(100, 100, 667, 404);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbExito = new JLabel("Fichero cargado con éxito. Se creará un fichero de feedback con los posibles errores");
		lbExito.setBounds(91, 39, 469, 14);
		contentPane.add(lbExito);
		lbExito.setVisible(false);

		JLabel lblTiemposDeLos = new JLabel("Tiempos de los atletas");
		lblTiemposDeLos.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTiemposDeLos.setBounds(213, 11, 179, 36);
		contentPane.add(lblTiemposDeLos);

		JLabel lbFichero = new JLabel("Seleccione el fichero .csv con los tiempos de los atletas");
		lbFichero.setBounds(141, 58, 356, 14);
		contentPane.add(lbFichero);

		JButton bSig = new JButton("Siguiente\r\n");
		bSig.setBounds(539, 309, 89, 23);
		contentPane.add(bSig);
		bSig.setVisible(false);
		/* TO-DO
		 * -Leer el archivo .csv y almacenarlo en variables
		 * -Comprobar los que estén mal para borrarlos y escribirlo en el fichero de salida
		 * -Actualizar la base de datos
		 */
		
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				archivo = fileChooser.getSelectedFile();
				try {
				if(e.getActionCommand().equals("CancelSelection")){
					//Crear objeto pantalla anterior
					//Pantalla anterior visible
					Tiempos.this.dispose();
				}
				else {
					if(archivo.getName().endsWith(".csv")) {
						br = new BufferedReader(new FileReader (archivo));
						fileChooser.setVisible(false);
						//textField.setVisible(true);
						//btnSiguiente.setVisible(true);
						//lblMensaje.setForeground(Color.BLACK);
						//lblMensaje.setText("¿Cómo quiere que se llame el archivo de salida?:");
						lbExito.setVisible(true);
						bSig.setVisible(true);
						lbFichero.setVisible(false);
					}
					else{
						JOptionPane.showMessageDialog(null, "Error: El fichero no posee la extensión '.csv'","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
				}catch (Exception e1) { e1.printStackTrace(); } 
			}});
		fileChooser.setBounds(10, 77, 631, 277);
		contentPane.add(fileChooser);

		

	}
}
