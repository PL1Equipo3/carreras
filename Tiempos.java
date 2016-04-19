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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Color;

@SuppressWarnings("serial")
public class Tiempos extends JFrame {

	private JPanel contentPane;
	static Statement s;

	private static File archivo;
	private static BufferedReader br = null;	
	private JTextField tfSalida;
	static String comp = null;

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
		ArrayList<String> nombres = new ArrayList<String>();
		ArrayList<String> tiempos = new ArrayList<String>();
		ArrayList<String> id = new ArrayList<String>();
		ArrayList<Integer> errores = new ArrayList<Integer>();


		JLabel lbExito = new JLabel("Fichero cargado con éxito. Introduzca el nombre del fichero de salida");
		lbExito.setBounds(91, 39, 469, 14);
		contentPane.add(lbExito);
		lbExito.setVisible(false);

		final JFileChooser fileChooser = new JFileChooser();

		JLabel lblTiemposDeLos = new JLabel("Tiempos de los atletas");
		lblTiemposDeLos.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTiemposDeLos.setBounds(213, 11, 179, 36);
		contentPane.add(lblTiemposDeLos);

		JLabel lblVacio = new JLabel("Introduzca un nombre");
		lblVacio.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblVacio.setForeground(Color.RED);
		lblVacio.setBounds(514, 105, 137, 14);
		contentPane.add(lblVacio);
		lblVacio.setVisible(false);

		JLabel lbFichero = new JLabel("Seleccione el fichero .csv con los tiempos de los atletas");
		lbFichero.setBounds(141, 58, 356, 14);
		contentPane.add(lbFichero);

		tfSalida = new JTextField();
		tfSalida.setBounds(72, 102, 425, 20);
		contentPane.add(tfSalida);
		tfSalida.setColumns(10);
		tfSalida.setVisible(false);

		JButton bSig = new JButton("Siguiente\r\n");
		bSig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean test;
				int contador=0,i = 0;
				String nom_salida=tfSalida.getText();
				if (nom_salida.equals("")){
					JOptionPane.showMessageDialog(null, "Error: introduzca un nombre para el archivo de salida","Error",JOptionPane.ERROR_MESSAGE);
				}
				else {
					for (i=0 ; i<id.size(); i++){
						test=true;

						if (id.get(i).equals(comp) == false) test=false;

						if (((tiempos.get(i).contains("[0-9]+")) && (tiempos.get(i).contains(":")))){
							test=false;
						}

						if (!nombres.get(i).matches("[0-9]+")){
							test=false;
						}

						if (test==false){
							errores.add(contador, i);
							contador++;
						}


					}	//Fin del for


					if ((errores.size() != 0) && (errores.size() != id.size())){
						String temp1 = null;
						String temp2 = null;
						String temp3 = null;
						for (i=0 ; i<errores.size() ; i++){
							temp1 = id.get(errores.get(i));
							temp2 = tiempos.get(errores.get(i));
							temp3 = nombres.get(errores.get(i));

							id.remove(temp1);
							tiempos.remove(temp2);
							nombres.remove(temp3);

						}
					}	//Fin del if


					if (errores.size() == id.size()){
						JOptionPane.showMessageDialog(null, "Error: El fichero que ha cargado no posee ningún dato válido","Error",JOptionPane.ERROR_MESSAGE);
						fileChooser.setVisible(true);
						lbExito.setVisible(false);
						bSig.setVisible(false);
						lbFichero.setVisible(true);
						tfSalida.setVisible(false);

					}
					else {
						try{
							for (i=0; i<id.size(); i++){
								String id_insc = nombres.get(i)+comp;
								s.execute("UPDATE Inscripción SET Estado='Finalizado', Tiempo='"+tiempos.get(i)+"' WHERE Id_Inscripción='"+id_insc+"'");	
							}

						}
						catch (Exception e2){ e2.printStackTrace(); }
					}

				}	//Fin del primer else

				//Cerrar mi ventana y abrir la de wanteiro
			}
		});
		bSig.setBounds(539, 309, 89, 23);
		contentPane.add(bSig);
		bSig.setVisible(false);
		/* TO-DO
		 * -Actualizar la base de datos
		 */



		fileChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				archivo = fileChooser.getSelectedFile();

				String[] buffer;
				String linea;
				String ruta=archivo.getAbsolutePath();	//Para Wanteiro
				try {
					if(e.getActionCommand().equals("CancelSelection")){
						Tiempos.this.dispose();
						//Abrir Inicial
					}
					else {
						if(archivo.getName().endsWith(".csv")) {
							br = new BufferedReader(new FileReader (archivo));
							fileChooser.setVisible(false);
							lbExito.setVisible(true);
							bSig.setVisible(true);
							lbFichero.setVisible(false);
							tfSalida.setVisible(true);

							//Guardar datos del fichero
							comp=br.readLine();
							linea=br.readLine();
							while (linea != null){
								buffer = linea.split(",");
								id.add(buffer[0]);
								nombres.add(buffer[1]);
								tiempos.add(buffer[2]);
								linea=br.readLine();
							}

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
