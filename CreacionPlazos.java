
//Recibir id_competicion por teclado

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.JDesktopPane;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import java.sql.*;
import javax.swing.JFormattedTextField;

public class CreacionPlazos extends JFrame {

	private JPanel contentPane;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreacionPlazos frame = new CreacionPlazos();
					frame.setVisible(true);
					//Variables

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CreacionPlazos() {
		final String id_competicion = "Travesera13";	//Se recibe por teclado

		try{


			//Conexión a la BD
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			//Ruta absoluta o relativa como parÃ¡metro de getConnection
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://Carreras.accdb");
			final Statement s = conn.createStatement();


			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 630, 348);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(new CardLayout(0, 0));

			JPanel panel = new JPanel();
			contentPane.add(panel, "name_613939773442738");
			panel.setLayout(null);


			final DefaultListModel lmI  = new DefaultListModel();
			final DefaultListModel lmF  = new DefaultListModel();
			final DefaultListModel lmC  = new DefaultListModel();

			final JList listIni = new JList();
			listIni.setBounds(294, 31, 91, 243);
			listIni.setModel(lmI);
			panel.add(listIni);


			final JComboBox cbDia = new JComboBox();
			cbDia.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
			cbDia.setBounds(58, 79, 49, 20);
			panel.add(cbDia);

			final JComboBox cbMes = new JComboBox();
			cbMes.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}));
			cbMes.setBounds(137, 79, 49, 20);
			panel.add(cbMes);

			final JComboBox cbAño = new JComboBox();
			cbAño.setModel(new DefaultComboBoxModel(new String[] {"2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028"}));
			cbAño.setBounds(214, 79, 63, 20);
			panel.add(cbAño);

			JButton bAñadirIni = new JButton("Añadir fecha de incio");
			bAñadirIni.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String dia = (String) cbDia.getSelectedItem();
					String mes = (String) cbMes.getSelectedItem();
					String año = (String) cbAño.getSelectedItem();
					String fechaIni=año+"-"+mes+"-"+dia;

					lmI.addElement(fechaIni);

				}
			});
			bAñadirIni.setBounds(10, 128, 155, 23);
			panel.add(bAñadirIni);

			JList listFin = new JList();
			listFin.setBounds(391, 31, 91, 243);
			listFin.setModel(lmF);
			panel.add(listFin);

			JLabel fechaInicio = new JLabel("Fecha Inicio");
			fechaInicio.setBounds(313, 11, 68, 14);
			panel.add(fechaInicio);

			JLabel fechaFin = new JLabel("Fecha Fin");
			fechaFin.setBounds(411, 11, 56, 14);
			panel.add(fechaFin);

			JLabel titulo = new JLabel("Añadir fechas a\r\n competición");
			titulo.setFont(new Font("Tahoma", Font.BOLD, 15));
			titulo.setBounds(55, 0, 229, 50);
			panel.add(titulo);

			JButton bAceptar = new JButton("Aceptar");
			bAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String Dia, Mes, Año;
					boolean test;
					int AñoInt;
					//Validación de todas las fechas
					if ((lmI.size() != lmF.size()) || (lmI.size() != lmC.size())){
						JOptionPane.showMessageDialog(null, "Error al guardar. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
					}
					else {
						test=true;
						for (int i=0; i<lmI.size();i++){
							Dia =(String) lmI.getElementAt(i);
							Mes = (String) lmI.getElementAt(i);
							Año = (String) lmI.getElementAt(i);


							Dia=Dia.substring(8, 9);
							Mes=Mes.substring(5, 6);
							Año=Año.substring(0, 3);

							AñoInt = Integer.parseInt(Año);



							if (Mes.equals("02")){
								if(AñoInt % 4 == 0 && AñoInt % 100 != 0 || AñoInt % 400 == 0){			//Bisiesto

									if (Dia.equals("30") || Dia.equals("31"))  {
										JOptionPane.showMessageDialog(null, "Error al guardar. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
										test=false;}
								}
								else if (Dia.equals("30") || Dia.equals("31") || Dia.equals("29")) {JOptionPane.showMessageDialog(null, "Error al guardar. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
								test=false;}
							}
							else if (Mes.equals("04")||Mes.equals("06")||Mes.equals("09")||Mes.equals("11")){
								if (Dia.equals("31")) {JOptionPane.showMessageDialog(null, "Error al guardar. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
								test=false;}
							}



							Dia =(String) lmF.getElementAt(i);
							Mes = (String) lmF.getElementAt(i);
							Año = (String) lmF.getElementAt(i);

							Dia=Dia.substring(8, 9);
							Mes=Mes.substring(5, 6);
							Año=Año.substring(0, 3);

							AñoInt = Integer.parseInt(Año);


							if (Mes.equals("02")){
								if(AñoInt % 4 == 0 && AñoInt % 100 != 0 || AñoInt % 400 == 0){			//Bisiesto

									if (Dia.equals("30") || Dia.equals("31"))  {
										JOptionPane.showMessageDialog(null, "Error al guardar. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
										test=false;}
								}
								else if (Dia.equals("30") || Dia.equals("31") || Dia.equals("29")) {JOptionPane.showMessageDialog(null, "Error al guardar. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
								test=false;}
							}
							else if (Mes.equals("04")||Mes.equals("06")||Mes.equals("09")||Mes.equals("11")){
								if (Dia.equals("31")) {JOptionPane.showMessageDialog(null, "Error al guardar. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
								test=false;}
							}




						}	//Fin del for
						if (test=true){

							for (int i=0; i<lmI.size();i++){
								String DiaI = (String) lmI.getElementAt(i);
								String MesI = (String) lmI.getElementAt(i);
								String AñoI = (String) lmI.getElementAt(i);

								DiaI=DiaI.substring(8, 10);
								MesI=MesI.substring(5, 7);
								AñoI=AñoI.substring(0, 4);

								String DiaF = (String) lmF.getElementAt(i);
								String MesF = (String) lmF.getElementAt(i);
								String AñoF = (String) lmF.getElementAt(i);



								DiaF=DiaF.substring(8, 10);
								MesF=MesF.substring(5, 7);
								AñoF=AñoF.substring(0, 4);

								String Cuota = (String) lmC.getElementAt(i);
								try {
									String SQL="INSERT INTO Plazos VALUES (8, #"+AñoI+"-"+MesI+"-"+DiaI+"#, #"+AñoF+"-"+MesF+"-"+DiaF+"#, "+Cuota+", '"+id_competicion+"' )";
									System.out.println(SQL);
									s.execute(SQL);
									
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								JOptionPane.showMessageDialog(null, "Datos Introducidos con éxito","OK",JOptionPane.INFORMATION_MESSAGE);

							}

						}
						JOptionPane.showMessageDialog(null, "Datos Introducidos con éxito","OK",JOptionPane.INFORMATION_MESSAGE);
						System.exit(0);
					}	//Fin del else
				}
			}
					);
			bAceptar.setBounds(10, 263, 89, 23);
			panel.add(bAceptar);

			JLabel lblDia = new JLabel("Dia");
			lblDia.setBounds(65, 64, 24, 14);
			panel.add(lblDia);

			JLabel lblMes = new JLabel("Mes");
			lblMes.setBounds(147, 64, 46, 14);
			panel.add(lblMes);

			JLabel lblAo = new JLabel("Año");
			lblAo.setBounds(231, 61, 46, 14);
			panel.add(lblAo);

			JButton bFechafin = new JButton("Añadir fecha fin");
			bFechafin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String diaF = (String) cbDia.getSelectedItem();
					String mesF = (String) cbMes.getSelectedItem();
					String añoF = (String) cbAño.getSelectedItem();
					String fechaFin=añoF+"-"+mesF+"-"+diaF;

					lmF.addElement(fechaFin);

				}
			});




			bFechafin.setBounds(10, 163, 155, 23);
			panel.add(bFechafin);

			JButton bBorrar = new JButton("Borrar");
			bBorrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (lmI.getSize()!=0){
						lmI.remove(lmI.getSize()-1);
					}
					if (lmF.getSize()!=0){
						lmF.remove(lmF.getSize()-1);
					}
					if (lmC.getSize() !=0){
						lmC.remove(lmC.getSize()-1);
					}
				}
			});
			bBorrar.setBounds(188, 143, 89, 23);
			panel.add(bBorrar);

			JLabel lblCuota = new JLabel("Cuota");
			lblCuota.setBounds(509, 11, 46, 14);
			panel.add(lblCuota);

			JList listC = new JList();
			listC.setBounds(492, 31, 77, 243);
			listC.setModel(lmC);
			panel.add(listC);

			final JFormattedTextField tfCuota = new JFormattedTextField();
			tfCuota.setText("0€");
			tfCuota.setBounds(10, 211, 89, 20);
			panel.add(tfCuota);

			JButton bCuota = new JButton("Añadir Cuota");
			bCuota.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String Cuota;
					Cuota =tfCuota.getText();
					lmC.addElement(Cuota);				
				}
			});
			bCuota.setBounds(157, 210, 127, 23);
			panel.add(bCuota);
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}

}

