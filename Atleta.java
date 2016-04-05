import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Atleta extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Atleta frame = new Atleta();
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
	public Atleta() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 245);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblAtleta = new JLabel("Atleta");
		lblAtleta.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblAtleta.setBounds(12, 13, 137, 29);
		panel.add(lblAtleta);
		
		JButton btnInscripcin = new JButton("Inscripci\u00F3n");
		btnInscripcin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InscripcionesMain n = new InscripcionesMain();
				n.setVisible(true);
				Atleta.this.dispose();
			}
		});
		btnInscripcin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnInscripcin.setBounds(56, 81, 109, 40);
		panel.add(btnInscripcin);
		
		JButton btnConsulta = new JButton("Consulta");
		btnConsulta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultarInscripción n = new ConsultarInscripción();
				n.setVisible(true);
				Atleta.this.dispose();
			}
		});
		btnConsulta.setBounds(256, 82, 109, 40);
		panel.add(btnConsulta);
		
		JButton btnAtrs = new JButton("Atr\u00E1s");
		btnAtrs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inicial n = new Inicial();
				n.setVisible(true);
				Atleta.this.dispose();
			}
		});
		btnAtrs.setBounds(313, 150, 97, 25);
		panel.add(btnAtrs);
	}

}
