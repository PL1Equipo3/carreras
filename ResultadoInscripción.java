import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ResultadoInscripción extends JFrame {

	private final JPanel contentPanel = new JPanel();
	private static ResultadoInscripción dialog;
	
	/**
	 * Launch the application.
	 * @param mensaje 
	 */
	public static void main(String mensaje) {
		try {
			dialog = new ResultadoInscripción(mensaje);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ResultadoInscripción(String mensaje) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 259);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);
			
			JLabel lblTitulo = new JLabel("Resultado de la Inscripci\u00F3n");
			lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 18));
			lblTitulo.setBounds(12, 13, 278, 33);
			panel.add(lblTitulo);
			
			JLabel lblResultado = new JLabel(mensaje);
			lblResultado.setHorizontalAlignment(SwingConstants.CENTER);
			lblResultado.setBounds(59, 59, 303, 95);
			panel.add(lblResultado);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {						
						//Ir a pestaña principal
						Atleta n = new Atleta();
						n.setVisible(true);
						ResultadoInscripción.this.dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
