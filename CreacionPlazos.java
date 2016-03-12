import java.awt.BorderLayout;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JList;
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

public class CreacionPlazos extends JFrame {

	private JPanel contentPane;
	static Statement s;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreacionPlazos frame = new CreacionPlazos();
					frame.setVisible(true);
					//Conexión a la BD
					Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");		
					//Ruta absoluta o relativa como parámetro de getConnection
					Connection conn=DriverManager.getConnection("jdbc:ucanaccess://Carreras.accdb");
					s = conn.createStatement();

					//Variables
					String id_competicion = null, fecha_limite=null;			//Se recibe por teclado
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 567, 348);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, "name_613939773442738");
		panel.setLayout(null);


		final DefaultListModel lmI  = new DefaultListModel();
		final DefaultListModel lmF  = new DefaultListModel();

		final JList listIni = new JList();
		listIni.setBounds(294, 31, 91, 243);
		listIni.setModel(lmI);
		panel.add(listIni);
		

		final JComboBox cbDia = new JComboBox();
		cbDia.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
		cbDia.setBounds(58, 79, 41, 20);
		panel.add(cbDia);

		final JComboBox cbMes = new JComboBox();
		cbMes.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));
		cbMes.setBounds(137, 79, 46, 20);
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
		fechaFin.setBounds(411, 11, 46, 14);
		panel.add(fechaFin);

		JLabel titulo = new JLabel("Añadir fechas a\r\n competición");
		titulo.setFont(new Font("Tahoma", Font.BOLD, 15));
		titulo.setBounds(55, 0, 229, 50);
		panel.add(titulo);

		JButton bAceptar = new JButton("Aceptar");
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
			}
		});
		bBorrar.setBounds(188, 143, 89, 23);
		panel.add(bBorrar);
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "SwingAction_1");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
