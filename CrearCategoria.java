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
	
	private static CrearCompetición VCompetición;
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
					CrearCategoria frame = new CrearCategoria(VCompetición);
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
	public CrearCategoria(CrearCompetición VComp) throws SQLException {
		try {
			//Conexión a la BD
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			//Ruta absoluta o relativa como parÃ¡metro de getConnection
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://Carreras 2ª versión.accdb");
			final Statement s = conn.createStatement();
			
			//Creación de los paneles para realizar la interfaz gráfica
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 389, 245);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));
			setTitle("Nueva Categoría");
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
					
			
			//Elementos gráficos para introducir el nombre--------------------------------------------------
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
			
			
			//Elemento para introducir el año de inicio------------------------------------------------------------
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
			
			//Elemntos para introducir el año de fin ---------------------------------------------------------------
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
					
					//categoría introducida por el organizador
					String CategoriaIntro = tfNombre.getText()+" "+tfAnyoInicio.getText()+"-"+tfAnyoFin.getText()+" "+cbSexo.getSelectedItem();
					//Elementos para ver categorias de la lista
					String[] Categoría;
					String orden;
					String delimitadores = "[ -]+";
					boolean Bien=true;
					String Mensaje ="";
					
					
					//valores comprobar que la edad  AñoIni< AñoFin
					int inicio= Integer.parseInt(tfAnyoInicio.getText());
					int fin= Integer.parseInt(tfAnyoFin.getText());
					if (inicio >= fin){
						JOptionPane.showMessageDialog(null, "El rango de edades introducido no es correcto","Error",JOptionPane.INFORMATION_MESSAGE);
						
					}else{
					
					int i=0;
					while (Bien && i< VCompetición.model.size()){
						Categoría = VCompetición.model.getElementAt(i).toString().split(delimitadores);
						System.out.printf("1:"+Categoría[0]+"\n");
						System.out.printf("2:"+Categoría[1]+"\n");
						System.out.printf("3:"+Categoría[2]+"\n");
						System.out.printf("4:"+Categoría[3]+"\n");
						//Mirar si ya existe una categoría con la combinación Nombre-Sexo
						
					if (Categoría[0].equals(tfNombre.getText()) && Categoría[3].equals(cbSexo.getSelectedItem())){
						
						if (Bien==true){
							Mensaje = "La combinación Nombre-Sexo de tu categoría creada ya existe";
						}
						Bien = Bien && false;
						
                    }
					else if ( !(( (Integer.parseInt(tfAnyoInicio.getText()) < Integer.parseInt(Categoría[1])) && (Integer.parseInt(tfAnyoFin.getText()) < Integer.parseInt(Categoría[1]) ) ) 
							|| 
							( (Integer.parseInt(tfAnyoInicio.getText()) > Integer.parseInt(Categoría[2])) && (Integer.parseInt(tfAnyoFin.getText()) > Integer.parseInt(Categoría[2])) ) )) 
					{
						if (Bien==true){
							Mensaje = "El rango de direcciones elegido se contrapone a otra categoría ya existente";
						}
						Bien = Bien && false;
						
					}
					else{
						
						if (Bien==true){
							Mensaje = "Categoría correcta";
						}
						
						
					}
							
						i++;
						
					}//FOR
					if (Bien){
					JOptionPane.showMessageDialog(null, Mensaje,"Éxito",JOptionPane.INFORMATION_MESSAGE);
					VCompetición.model.addElement(CategoriaIntro);
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
		}//fin del catch función 
		
	}//fin del metodo CrearCategoria

	
}//fin de la clase CrearCategoria



