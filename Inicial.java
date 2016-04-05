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

public class Inicial extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Inicial frame = new Inicial();
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
	public Inicial() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 245);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblCarreras = new JLabel("Carreras");
		lblCarreras.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblCarreras.setBounds(12, 13, 120, 22);
		panel.add(lblCarreras);
		
		JButton btnAtleta = new JButton("Atleta");
		btnAtleta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Atleta n = new Atleta();
				n.setVisible(true);
				Inicial.this.dispose();
			}
		});
		btnAtleta.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAtleta.setBounds(50, 81, 109, 40);
		panel.add(btnAtleta);
		
		JButton btnNewButton = new JButton("Organizador");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Organizador n = new Organizador();
				n.setVisible(true);
				Inicial.this.dispose();
			}
		});
		btnNewButton.setBounds(250, 82, 109, 40);
		panel.add(btnNewButton);
	}
}
