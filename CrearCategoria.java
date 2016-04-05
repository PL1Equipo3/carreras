import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;















import java.awt.event.ActionEvent;



import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class CrearCategoria extends JFrame {
	
	private static CrearCompetici�n VCompetici�n;
	private JPanel contentPane;
	private JTextField tfNombre;
	private JTextField tfAnyoInicio;
	private JTextField tfAnyoFin;
	private JButton btnCrear;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CrearCategoria frame = new CrearCategoria(VCompetici�n);
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
	public CrearCategoria(CrearCompetici�n VComp) throws SQLException {
		try {
			//Conexi�n a la BD
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			//Ruta absoluta o relativa como parámetro de getConnection
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://Carreras 2� versi�n.accdb");
			final Statement s = conn.createStatement();
			
			//Creaci�n de los paneles para realizar la interfaz gr�fica
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 389, 245);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));
			setTitle("Nueva Categor�a");
			setContentPane(contentPane);
			
			
			//Panel donde se crea los elementos de la interfaz
			JPanel pCrearCategoria = new JPanel();
			pCrearCategoria.setLayout(null);
			contentPane.add(pCrearCategoria, BorderLayout.CENTER);
			
			
			//Label para el titulo
			JLabel lblTextoCrear = new JLabel("Crea una nueva categor\u00EDa");
			lblTextoCrear.setHorizontalAlignment(SwingConstants.CENTER);
			lblTextoCrear.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblTextoCrear.setBounds(23, 0, 295, 20);
			pCrearCategoria.add(lblTextoCrear);
					
			
			//Elementos gr�ficos para introducir el nombre--------------------------------------------------
			JLabel lblNombre = new JLabel("Nombre: ");
			lblNombre.setBounds(10, 42, 58, 14);
			pCrearCategoria.add(lblNombre);
			
			tfNombre = new JTextField();
			tfNombre.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent arg0) {
					if(tfNombre.getText().length() == 0 )
		            	btnCrear.setEnabled(false);
		            else
		            {
		            	btnCrear.setEnabled(true);
		            }
					
				}
			});
			tfNombre.setColumns(10);
			tfNombre.setBounds(107, 39, 115, 20);
			pCrearCategoria.add(tfNombre);
			
			
			//Elemento para introducir el a�o de inicio------------------------------------------------------------
			JLabel lblAnyoInicio = new JLabel("A\u00F1o inicio:");
			lblAnyoInicio.setBounds(10, 78, 58, 14);
			pCrearCategoria.add(lblAnyoInicio);
					
			tfAnyoInicio = new JTextField();
			tfAnyoInicio.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if(tfAnyoInicio.getText().length() == 0 )
		            	btnCrear.setEnabled(false);
		            else
		            {
		            	btnCrear.setEnabled(true);
		            }
				}
			});
			tfAnyoInicio.setColumns(10);
			tfAnyoInicio.setBounds(107, 75, 115, 20);
			pCrearCategoria.add(tfAnyoInicio);
			
			//Elemntos para introducir el a�o de fin ---------------------------------------------------------------
			JLabel lblAnyoFin = new JLabel("A\u00F1o fin:");
			lblAnyoFin.setBounds(10, 119, 46, 14);
			pCrearCategoria.add(lblAnyoFin);
			
			tfAnyoFin = new JTextField();
			tfAnyoFin.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if(tfAnyoFin.getText().length() == 0 )
		            	btnCrear.setEnabled(false);
		            else
		            {
		            	btnCrear.setEnabled(true);
		            }
				}
			});
			tfAnyoFin.setColumns(10);
			tfAnyoFin.setBounds(107, 116, 115, 20);
			pCrearCategoria.add(tfAnyoFin);
			
			
			//Elemento para elegir el sexo---------------------------------------------------------------------------------
			JLabel lblSexo = new JLabel("Sexo:");
			lblSexo.setBounds(10, 158, 46, 14);
			pCrearCategoria.add(lblSexo);
					
			final JComboBox cbSexo = new JComboBox();
			cbSexo.setModel(new DefaultComboBoxModel(new String[] {"Femenino", "Masculino"}));
			cbSexo.setBounds(107, 155, 115, 20);
			pCrearCategoria.add(cbSexo);
			
			
			
			
			//Botton de crear
			btnCrear = new JButton("Crear");
			btnCrear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					//categor�a introducida por el organizador
					String CategoriaIntro = tfNombre.getText()+" "+tfAnyoInicio.getText()+"-"+tfAnyoFin.getText()+" "+cbSexo.getSelectedItem();
					//Elementos para ver categorias de la lista
					String[] Categor�a;
					String orden;
					String delimitadores = "[ -]+";
					boolean Bien=true;
					String Mensaje ="";
					
					
					//valores comprobar que la edad  A�oIni< A�oFin
					int inicio= Integer.parseInt(tfAnyoInicio.getText());
					int fin= Integer.parseInt(tfAnyoFin.getText());
					if (inicio >= fin){
						JOptionPane.showMessageDialog(null, "El rango de edades introducido no es correcto","Error",JOptionPane.INFORMATION_MESSAGE);
						
					}else{
					
					int i=0;
					while (Bien && i< VCompetici�n.model.size()){
						Categor�a = VCompetici�n.model.getElementAt(i).toString().split(delimitadores);
						System.out.printf("1:"+Categor�a[0]+"\n");
						System.out.printf("2:"+Categor�a[1]+"\n");
						System.out.printf("3:"+Categor�a[2]+"\n");
						System.out.printf("4:"+Categor�a[3]+"\n");
						//Mirar si ya existe una categor�a con la combinaci�n Nombre-Sexo
						
					if (Categor�a[0].equals(tfNombre.getText()) && Categor�a[3].equals(cbSexo.getSelectedItem())){
						
						if (Bien==true){
							Mensaje = "La combinaci�n Nombre-Sexo de tu categor�a creada ya existe";
						}
						Bien = Bien && false;
						
                    }
					else if ( !(( (Integer.parseInt(tfAnyoInicio.getText()) < Integer.parseInt(Categor�a[1])) && (Integer.parseInt(tfAnyoFin.getText()) < Integer.parseInt(Categor�a[1]) ) ) 
							|| 
							( (Integer.parseInt(tfAnyoInicio.getText()) > Integer.parseInt(Categor�a[2])) && (Integer.parseInt(tfAnyoFin.getText()) > Integer.parseInt(Categor�a[2])) ) )) 
					{
						if (Bien==true){
							Mensaje = "El rango de direcciones elegido se contrapone a otra categor�a ya existente";
						}
						Bien = Bien && false;
						
					}
					else{
						
						if (Bien==true){
							Mensaje = "Categor�a correcta";
						}
						
						
					}
							
						i++;
						
					}//FOR
					if (Bien){
					JOptionPane.showMessageDialog(null, Mensaje,"�xito",JOptionPane.INFORMATION_MESSAGE);
					VCompetici�n.model.addElement(CategoriaIntro);
					CrearCategoria.this.dispose();
					} else JOptionPane.showMessageDialog(null, Mensaje,"Error",JOptionPane.INFORMATION_MESSAGE);
					}
					
				
				}//fin de la funcion  actionPerformed del boton crear
			});//fin de todo lo que tenga que ver con boton 
			btnCrear.setEnabled(false);
			btnCrear.setBounds(253, 154, 89, 23);
			pCrearCategoria.add(btnCrear);
			
			
			
			

		
		
		}//fin del try de la funcion 
		catch(Exception ex){
		ex.printStackTrace();
		}//fin del catch funci�n 
		
	}//fin del metodo CrearCategoria

	
}//fin de la clase CrearCategoria



