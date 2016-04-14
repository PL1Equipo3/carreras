

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
import javax.swing.JCheckBox;
import javax.swing.JTextField;

public class CreacionPlazos extends JFrame {

	private JPanel contentPane;
	private static CrearCompetición CrearComp;
	private static String comp;
	private JTextField tfCuota;
	private JTextField tfCuotaFed;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreacionPlazos frame = new CreacionPlazos(CrearComp, comp);
					frame.setVisible(true);
					//Variables

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public CreacionPlazos(CrearCompetición CrearComp, String comp) {
		final String id_competicion = comp;

		try{


			//Conexión a la BD
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			//Ruta absoluta o relativa como parÃ¡metro de getConnection
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://Carreras 3ª versión.accdb");
			final Statement s = conn.createStatement();


			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 769, 429);
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
			final DefaultListModel lmCF = new DefaultListModel();
			String fechaLimite = "14-04-2019";

			final JList listIni = new JList();
			listIni.setBounds(309, 31, 91, 304);
			listIni.setModel(lmI);
			panel.add(listIni);


			final JComboBox cbDiaI = new JComboBox();
			cbDiaI.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
			cbDiaI.setBounds(21, 90, 49, 20);
			panel.add(cbDiaI);

			final JComboBox cbMesI = new JComboBox();
			cbMesI.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}));
			cbMesI.setBounds(102, 90, 49, 20);
			panel.add(cbMesI);

			final JComboBox cbAñoI = new JComboBox();
			cbAñoI.setModel(new DefaultComboBoxModel(new String[] {"2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028"}));
			cbAñoI.setBounds(181, 90, 63, 20);
			panel.add(cbAñoI);

			JList listFin = new JList();
			listFin.setBounds(421, 31, 91, 304);
			listFin.setModel(lmF);
			panel.add(listFin);

			JLabel fechaInicio = new JLabel("Fecha Inicio");
			fechaInicio.setBounds(321, 11, 68, 14);
			panel.add(fechaInicio);

			JLabel fechaFin = new JLabel("Fecha Fin");
			fechaFin.setBounds(442, 11, 56, 14);
			panel.add(fechaFin);

			JLabel titulo = new JLabel("Añadir plazos a\r\n competición");
			titulo.setFont(new Font("Tahoma", Font.BOLD, 15));
			titulo.setBounds(55, 0, 229, 50);
			panel.add(titulo);

			JList listCF = new JList();
			listCF.setBounds(642, 31, 77, 304);
			listCF.setModel(lmCF);
			panel.add(listCF);

			JButton bAceptar = new JButton("Aceptar");
			bAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String Dia, Mes, Año, DiaA, MesA, AñoA;
					boolean test;
					int AñoInt, MesInt, DiaInt, DiaAInt, MesAInt, AñoAInt;
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


							Dia = Dia.substring(0, 2);
							Mes = Mes.substring(3, 5);
							Año = Año.substring(6, 10);

							MesInt = Integer.parseInt(Mes);
							DiaInt = Integer.parseInt(Dia);
							AñoInt = Integer.parseInt(Año);

							String DiaL = fechaLimite.substring(0, 2);
							String MesL = fechaLimite.substring(3, 5);
							String AñoL = fechaLimite.substring(6, 10);

							int DiaLInt = Integer.parseInt(DiaL);
							int MesLInt = Integer.parseInt(MesL);
							int AñoLInt = Integer.parseInt(AñoL);

							if (ComparaFechas(DiaInt, DiaLInt, MesInt, MesLInt, AñoInt, AñoLInt) == true){
								test=false;
								JOptionPane.showMessageDialog(null, "Error al guardar. Ha introducido un plazo que sobrepasa la fecha de la carrera","Error",JOptionPane.ERROR_MESSAGE);
							}

							if (i != 0){
								DiaA = (String) lmF.getElementAt(i-1);
								MesA = (String) lmF.getElementAt(i-1);
								AñoA = (String) lmF.getElementAt(i-1);

								DiaA = DiaA.substring(0, 2);
								MesA = MesA.substring(3, 5);
								AñoA = AñoA.substring(6, 10);

								MesAInt = Integer.parseInt(MesA);
								DiaAInt = Integer.parseInt(DiaA);
								AñoAInt = Integer.parseInt(AñoA);


								if ((MesAInt == 12) && (DiaAInt == 31) && ((AñoAInt != AñoInt-1) || (MesInt != 1) || (DiaInt != 1))){
									JOptionPane.showMessageDialog(null, "Error al guardar. Un plazo debe empezar cuando acaba otro. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
									test=false;
								}
								else {
									if (((MesAInt == 4) || (MesAInt == 6) || (MesAInt == 9) || (MesAInt == 11)) && (DiaAInt == 30) && (AñoAInt == AñoInt) && (DiaInt != 1) && (MesAInt == MesInt-1)){
										JOptionPane.showMessageDialog(null, "Error al guardar. Un plazo debe empezar cuando acaba otro. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
										test=false;
									}
									else {

										if((AñoInt % 4 == 0 && AñoInt % 100 != 0 || AñoInt % 400 == 0) && ((MesAInt == 2) && (DiaAInt==29) && (DiaInt != 1))){
											JOptionPane.showMessageDialog(null, "Error al guardar. Un plazo debe empezar cuando acaba otro. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
											test=false;											
										}
										else {
											if((!(AñoInt % 4 == 0 && AñoInt % 100 != 0 || AñoInt % 400 == 0)) && ((MesAInt == 2) && (DiaAInt==28) && (DiaInt != 1))){											
												JOptionPane.showMessageDialog(null, "Error al guardar. Un plazo debe empezar cuando acaba otro. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
												test=false;												
											}
											else {

												if((DiaAInt == 31) && (MesAInt != MesInt-1) && (DiaInt != 1)){
													JOptionPane.showMessageDialog(null, "Error al guardar. Un plazo debe empezar cuando acaba otro. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
													test=false;
													
												}
												else {
													if ((AñoAInt != AñoInt) && (MesAInt!=12) && (DiaAInt!=31)){
														JOptionPane.showMessageDialog(null, "Error al guardar. Un plazo debe empezar cuando acaba otro. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
														test=false;
														System.out.println("Año");
													}
													else {

														if ((MesAInt != MesInt) && (DiaAInt<30)){
															JOptionPane.showMessageDialog(null, "Error al guardar. Un plazo debe empezar cuando acaba otro. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
															test=false;
															System.out.println("Mes");
														}
														else {
															if (DiaAInt != DiaInt-1){
																JOptionPane.showMessageDialog(null, "Error al guardar. Un plazo debe empezar cuando acaba otro. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
																test=false;
																System.out.println("dia");
															}

														}
													}
												}
											}
										}
									}
								}	//Fin del primer else
							}	//Fin if (i!=0)



							if (test==true){
								test=fechaCorrecta(Dia,Mes,AñoInt);
							}

							Dia = (String) lmF.getElementAt(i);
							Mes = (String) lmF.getElementAt(i);
							Año = (String) lmF.getElementAt(i);

							Dia=Dia.substring(0, 2);
							Mes=Mes.substring(3, 5);
							Año=Año.substring(6, 10);


							AñoInt = Integer.parseInt(Año);
							MesInt = Integer.parseInt(Mes);
							DiaInt = Integer.parseInt(Dia);


							DiaA = (String) lmI.getElementAt(i);
							MesA = (String) lmI.getElementAt(i);
							AñoA = (String) lmI.getElementAt(i);

							DiaA=DiaA.substring(0, 2);
							MesA=MesA.substring(3, 5);
							AñoA=AñoA.substring(6, 10);

							MesAInt = Integer.parseInt(MesA);
							DiaAInt = Integer.parseInt(DiaA);
							AñoAInt = Integer.parseInt(AñoA);

							DiaL = fechaLimite.substring(0, 2);
							MesL = fechaLimite.substring(3, 5);
							AñoL = fechaLimite.substring(6, 10);

							DiaLInt = Integer.parseInt(DiaL);
							MesLInt = Integer.parseInt(MesL);
							AñoLInt = Integer.parseInt(AñoL);

							if (ComparaFechas(DiaInt, DiaLInt, MesInt, MesLInt, AñoInt, AñoLInt) == true){
								test=false;
								JOptionPane.showMessageDialog(null, "Error al guardar. Ha introducido un plazo que sobrepasa la fecha de la carrera","Error",JOptionPane.ERROR_MESSAGE);
							}
							if (AñoAInt > AñoInt){
								JOptionPane.showMessageDialog(null, "Error al guardar. Uno de los plazos introducidos no es válido. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
								test=false;
							}else if (AñoAInt == AñoInt) {
								if (MesAInt > MesInt){
									JOptionPane.showMessageDialog(null, "Error al guardar. Uno de los plazos introducidos no es válido. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
									test=false;
								}else if (MesAInt == MesInt){
									if(DiaAInt >= DiaInt){
										JOptionPane.showMessageDialog(null, "Error al guardar. Uno de los plazos introducidos no es válido. Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
										test=false;
									}
								}
							}



							if (test==true){
								test=fechaCorrecta(Dia,Mes,AñoInt);
							}


						}	//Fin del for

						if (test==true){

							for (int i=0; i<lmI.size();i++){
								String DiaI = (String) lmI.getElementAt(i);
								String MesI = (String) lmI.getElementAt(i);
								String AñoI = (String) lmI.getElementAt(i);

								DiaI=DiaI.substring(0, 2);
								MesI=MesI.substring(3, 5);
								AñoI=AñoI.substring(6, 10);

								String DiaF = (String) lmF.getElementAt(i);
								String MesF = (String) lmF.getElementAt(i);
								String AñoF = (String) lmF.getElementAt(i);



								DiaF=DiaF.substring(0, 2);
								MesF=MesF.substring(3, 5);
								AñoF=AñoF.substring(6, 10);

								String Cuota = String.valueOf(lmC.getElementAt(i));
								/*
								try {
									String SQL="INSERT INTO Plazos VALUES (8, #"+AñoI+"-"+MesI+"-"+DiaI+"#, #"+AñoF+"-"+MesF+"-"+DiaF+"#, "+Cuota+", '"+id_competicion+"' )";
									System.out.println(SQL);
									s.execute(SQL);

								}
								catch (SQLException e) {

									e.printStackTrace();
								}
								 */
								String SQL="INSERT INTO Plazos VALUES (8, #"+AñoI+"-"+MesI+"-"+DiaI+"#, #"+AñoF+"-"+MesF+"-"+DiaF+"#, "+Cuota+", '"+id_competicion+"' )";
								System.out.println("Todo va bien pavo, ibas a escribir esto");
								System.out.println(SQL);
							}
							JOptionPane.showMessageDialog(null, "Datos Introducidos con éxito","OK",JOptionPane.INFORMATION_MESSAGE);
							System.exit(0);
						}	//Fin del if						
					}	//Fin del else

				}	//Fin actionPerformed
			}	//Fin botón aceptar
					);
			bAceptar.setBounds(630, 346, 89, 23);
			panel.add(bAceptar);

			JLabel lblDia = new JLabel("Dia");
			lblDia.setBounds(37, 74, 24, 14);
			panel.add(lblDia);

			JLabel lblMes = new JLabel("Mes");
			lblMes.setBounds(116, 74, 46, 14);
			panel.add(lblMes);

			JLabel lblAo = new JLabel("Año");
			lblAo.setBounds(195, 74, 46, 14);
			panel.add(lblAo);

			JList listC = new JList();
			listC.setBounds(540, 31, 77, 304);
			listC.setModel(lmC);
			panel.add(listC);

			JButton bBorrar = new JButton("Borrar");
			bBorrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//Boton borrar
					int index;
					if (listIni.isSelectionEmpty()==false){	//Lista Inicio
						index=listIni.getSelectedIndex();
						lmI.remove(index);
						lmF.remove(index);
						lmC.remove(index);
						lmCF.remove(index);
					}
					else if(listFin.isSelectionEmpty()==false){	//Lista Fin
						index=listFin.getSelectedIndex();
						lmI.remove(index);
						lmF.remove(index);
						lmC.remove(index);
						lmCF.remove(index);
					}
					else if (listC.isSelectionEmpty()==false){
						index=listC.getSelectedIndex();
						lmI.remove(index);
						lmF.remove(index);
						lmC.remove(index);
						lmCF.remove(index);
					}
					else if (listCF.isSelectionEmpty()==false){
						index=listCF.getSelectedIndex();
						lmI.remove(index);
						lmF.remove(index);
						lmC.remove(index);
						lmCF.remove(index);
					}

				}
			});
			bBorrar.setBounds(309, 346, 80, 23);
			panel.add(bBorrar);

			JLabel lblCuota = new JLabel("Cuota");
			lblCuota.setBounds(561, 11, 46, 14);
			panel.add(lblCuota);



			JLabel lblFechaInicio = new JLabel("Fecha Inicio");
			lblFechaInicio.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblFechaInicio.setBounds(116, 49, 100, 14);
			panel.add(lblFechaInicio);

			JComboBox cbDiaF = new JComboBox();
			cbDiaF.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
			cbDiaF.setBounds(21, 176, 49, 20);
			panel.add(cbDiaF);

			JLabel label = new JLabel("Dia");
			label.setBounds(37, 160, 24, 14);
			panel.add(label);

			JComboBox cbMesF = new JComboBox();
			cbMesF.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}));
			cbMesF.setBounds(102, 176, 49, 20);
			panel.add(cbMesF);

			JLabel label_1 = new JLabel("Mes");
			label_1.setBounds(116, 160, 46, 14);
			panel.add(label_1);

			JComboBox cbAñoF = new JComboBox();
			cbAñoF.setModel(new DefaultComboBoxModel(new String[] {"2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028"}));
			cbAñoF.setBounds(181, 176, 63, 20);
			panel.add(cbAñoF);

			JLabel label_2 = new JLabel("Año");
			label_2.setBounds(198, 160, 46, 14);
			panel.add(label_2);

			JLabel lblFechaFin = new JLabel("Fecha Fin");
			lblFechaFin.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblFechaFin.setBounds(116, 141, 89, 14);
			panel.add(lblFechaFin);

			JCheckBox chFed = new JCheckBox("Federado");
			chFed.setBounds(10, 265, 97, 23);
			panel.add(chFed);

			JLabel lblCuota_1 = new JLabel("Cuota");
			lblCuota_1.setBounds(116, 225, 46, 14);
			panel.add(lblCuota_1);

			JLabel lblCuotaFederado = new JLabel("Cuota Federado");
			lblCuotaFederado.setBounds(116, 298, 100, 14);
			panel.add(lblCuotaFederado);

			JButton bAñadir = new JButton("Añadir");	//El botón de añadir añade una fila entera con los datos recogidos en los campos fecha inicio, fecha fin, cuota y cuota federado 
			bAñadir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					//Variables
					String Dia,Mes,Año,FechaFin = null,FechaIni,tCuota,tCuotaFed,DiaTemp,MesTemp,AñoTemp;
					double Cuota = 0, CuotaFed = 0;
					int AñoInt, DiaInt, MesInt, index, DiaIntt, MesIntt, AñoIntt, i;
					boolean check=true, fechatest;

					//Fecah Inicio
					Dia=(String) cbDiaI.getSelectedItem();
					Mes=(String) cbMesI.getSelectedItem();
					Año=(String) cbAñoI.getSelectedItem();
					AñoInt= Integer.parseInt(Año);

					check=fechaCorrecta(Dia, Mes, AñoInt);
					FechaIni=Dia+"-"+Mes+"-"+Año;

					//FechaFin
					if (check==true){
						Dia=(String) cbDiaF.getSelectedItem();
						Mes=(String) cbMesF.getSelectedItem();
						Año=(String) cbAñoF.getSelectedItem();
						AñoInt= Integer.parseInt(Año);

						check=fechaCorrecta(Dia, Mes, AñoInt);
						FechaFin=Dia+"-"+Mes+"-"+Año;
					}
					//Cuota
					if (tfCuota.getText()==null){
						JOptionPane.showMessageDialog(null, "Error: El campo 'Cuota' esta vacio","Error",JOptionPane.ERROR_MESSAGE);
						check=false;
					}
					else{
						tCuota=tfCuota.getText();
						Cuota=Double.parseDouble(tCuota);
					}
					//Cuota Federado
					if(chFed.isSelected()){
						if (tfCuotaFed.getText()==null){
							JOptionPane.showMessageDialog(null, "Error: El campo 'Cuota' esta vacio","Error",JOptionPane.ERROR_MESSAGE);
							check=false;
						}
						else {
							tCuotaFed=tfCuotaFed.getText();
							CuotaFed=Double.parseDouble(tCuotaFed);
						}
					}
					else CuotaFed=Cuota;
					//Añadir datos a la lista
					if (check==true){
						//Meterlo en la posición adecuada
						if (lmI.size()==0){
							lmI.addElement(FechaIni);
							lmF.addElement(FechaFin);
							lmC.addElement(Cuota);
							lmCF.addElement(CuotaFed);
						}
						else{


							fechatest=true;
							Dia=(String) cbDiaI.getSelectedItem();
							Mes=(String) cbMesI.getSelectedItem();
							Año=(String) cbAñoI.getSelectedItem();
							AñoInt=Integer.parseInt(Año);
							MesInt=Integer.parseInt(Mes);
							DiaInt=Integer.parseInt(Dia);
							i=0;
							while(fechatest == true && i<lmI.size()){
								DiaTemp=(String) lmI.getElementAt(i);
								MesTemp=(String) lmI.getElementAt(i);
								AñoTemp=(String) lmI.getElementAt(i);

								DiaTemp=DiaTemp.substring(0, 2);
								MesTemp=MesTemp.substring(3, 5);
								AñoTemp=AñoTemp.substring(6, 10);

								AñoIntt=Integer.parseInt(AñoTemp);
								MesIntt=Integer.parseInt(MesTemp);
								DiaIntt=Integer.parseInt(DiaTemp);

								fechatest=ComparaFechas(DiaInt, DiaIntt, MesInt, MesIntt, AñoInt, AñoIntt);
								i++;

							}
							if (lmI.size() != i) i--;

							lmI.add(i, FechaIni);
							lmF.add(i, FechaFin);
							lmC.add(i, Cuota);
							lmCF.add(i, CuotaFed);
						}
					}

				}
			});
			bAñadir.setBounds(155, 346, 89, 23);
			panel.add(bAñadir);



			JLabel lblCuotaFed = new JLabel("Cuota Fed");
			lblCuotaFed.setBounds(652, 11, 67, 14);
			panel.add(lblCuotaFed);

			tfCuota = new JTextField();
			tfCuota.setBounds(21, 222, 86, 20);
			panel.add(tfCuota);
			tfCuota.setColumns(10);
			tfCuota.setText("0");

			tfCuotaFed = new JTextField();
			tfCuotaFed.setBounds(20, 295, 86, 20);
			panel.add(tfCuotaFed);
			tfCuotaFed.setColumns(10);
			tfCuotaFed.setText("0");
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}

	//Metodo para comprobar si una fecha es correcta
	public boolean fechaCorrecta (String Dia, String Mes, int AñoInt){
		boolean test = true;
		if (Mes.equals("02")){
			if(AñoInt % 4 == 0 && AñoInt % 100 != 0 || AñoInt % 400 == 0){			//Bisiesto

				if (Dia.equals("30") || Dia.equals("31"))  {
					JOptionPane.showMessageDialog(null, "Error al guardar. Febrero solo tiene 28 días (29 si bisiesto). Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
					test=false;}
			}
			else if (Dia.equals("30") || Dia.equals("31") || Dia.equals("29")) {JOptionPane.showMessageDialog(null, "Error al guardar. Febrero solo tiene 28 días (29 si bisiesto). Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
			test=false;}
		}
		else if (Mes.equals("04")||Mes.equals("06")||Mes.equals("09")||Mes.equals("11")){
			if (Dia.equals("31")) {JOptionPane.showMessageDialog(null, "Error al guardar. Abril, Junio, Septiembre y Noviembre tienen 30 días Revise los datos introducidos","Error",JOptionPane.ERROR_MESSAGE);
			test=false;}
		}		
		return test;
	}

	//Metodo para pasar una fecha del formato normal al formato SQL
	public String FechaSQL (String fecha){
		String SQL = null;
		String [] StringArray= fecha.split("-");
		SQL= StringArray[2]+"-"+StringArray[1]+"-"+StringArray[0];		
		return SQL;
	}

	public boolean ComparaFechas(int dia1, int dia2, int mes1, int mes2, int año1, int año2){	//Comprueba si una fecha es mayor estricto que otra.
		//Si la fecha "1" es mayor devuelve true en caso contrario false
		boolean check=false;
		if (año1 > año2){
			check=true;
		}else if((año1 == año2) && (mes1 > mes2)){
			check=true;
		}else if((año1 == año2) && (mes1 == mes2) && (dia1>dia2)){
			check=true;
		}

		return check;
	}
}
