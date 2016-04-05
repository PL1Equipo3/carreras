import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.ButtonGroup;

public class InscripcionesMain extends JFrame {

	private JPanel contentPane;
	private JTextField tfDNI;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private static InscripcionesMain frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new InscripcionesMain();
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
	public InscripcionesMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 418, 282);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblInscripcin = new JLabel("Inscripci\u00F3n");
		lblInscripcin.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblInscripcin.setBounds(12, 13, 137, 22);
		panel.add(lblInscripcin);
		
		JLabel lblEsLaPrimera = new JLabel("\u00BFEs la primera vez que se inscribe en una competici\u00F3n?");
		lblEsLaPrimera.setHorizontalAlignment(SwingConstants.CENTER);
		lblEsLaPrimera.setBounds(22, 48, 338, 16);
		panel.add(lblEsLaPrimera);
		
		final JButton btnSRegistrarse = new JButton("Registrarme");
		btnSRegistrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistroAtleta n = new RegistroAtleta();
				n.setVisible(true);
				InscripcionesMain.this.dispose();
			}
		});
		btnSRegistrarse.setBounds(218, 77, 116, 25);
		panel.add(btnSRegistrarse);
		
		final JLabel lblDni = new JLabel("Introduzca su DNI:");
		lblDni.setEnabled(false);
		lblDni.setBounds(104, 145, 116, 16);
		panel.add(lblDni);
		
		tfDNI = new JTextField();
		tfDNI.setEnabled(false);
		tfDNI.setBounds(218, 142, 116, 22);
		panel.add(tfDNI);
		tfDNI.setColumns(10);
		
		final JButton btnSiguiente = new JButton("Siguiente");
		btnSiguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!tfDNI.getText().equals("")){
					NuevaInscripcion n = new NuevaInscripcion(tfDNI.getText());
					n.setVisible(true);
					InscripcionesMain.this.dispose();
				}
				else{
					lblDni.setForeground(Color.RED);
				}
			}
		});
		btnSiguiente.setEnabled(false);
		btnSiguiente.setBounds(263, 177, 97, 25);
		panel.add(btnSiguiente);
		
		final JRadioButton rdbtnS = new JRadioButton("S\u00ED, es la primera vez");
		buttonGroup.add(rdbtnS);
		rdbtnS.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(rdbtnS.isSelected()){
					btnSRegistrarse.setEnabled(true);
				}
				else{
					btnSRegistrarse.setEnabled(false);
				}
			}
		});
		rdbtnS.setSelected(true);
		rdbtnS.setBounds(54, 77, 147, 25);
		panel.add(rdbtnS);
		
		final JRadioButton rdbtnNo = new JRadioButton("No, ya estoy registrado");
		buttonGroup.add(rdbtnNo);
		rdbtnNo.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (rdbtnNo.isSelected()){
					lblDni.setEnabled(true);
					tfDNI.setEnabled(true);
					btnSiguiente.setEnabled(true);
				}
				else{
					lblDni.setEnabled(false);
					tfDNI.setEnabled(false);
					btnSiguiente.setEnabled(false);
				}
			}
		});
		rdbtnNo.setBounds(54, 117, 207, 25);
		panel.add(rdbtnNo);
		
		JButton btnAtrs = new JButton("Atr\u00E1s");
		btnAtrs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Atleta n = new Atleta();
				n.setVisible(true);
				InscripcionesMain.this.dispose();
			}
		});
		btnAtrs.setBounds(12, 190, 69, 22);
		panel.add(btnAtrs);
	}
}
