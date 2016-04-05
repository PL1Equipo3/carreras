import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Organizador extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Organizador frame = new Organizador();
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
	public Organizador() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 245);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JButton btnCrearCompeticin = new JButton("<html>Crear<br>competici\u00F3n</html>");
		btnCrearCompeticin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CrearCompetición n = new CrearCompetición();
				n.setVisible(true);
				Organizador.this.dispose();
			}
		});
		btnCrearCompeticin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnCrearCompeticin.setBounds(56, 81, 109, 40);
		panel.add(btnCrearCompeticin);
		
		JButton btnRecepcionDePagos = new JButton("<html>Recepci\u00F3n<br>de pagos</html>");
		btnRecepcionDePagos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RecepcionPagosGraf n = new RecepcionPagosGraf();
				n.setVisible(true);
				Organizador.this.dispose();
			}
		});
		btnRecepcionDePagos.setBounds(256, 82, 109, 40);
		panel.add(btnRecepcionDePagos);
		
		JButton button_2 = new JButton("Atr\u00E1s");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inicial n = new Inicial();
				n.setVisible(true);
				Organizador.this.dispose();
			}
		});
		button_2.setBounds(313, 150, 97, 25);
		panel.add(button_2);
		
		JLabel lblOrganizador = new JLabel("Organizador");
		lblOrganizador.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblOrganizador.setBounds(12, 13, 153, 34);
		panel.add(lblOrganizador);
	}

}
