package Solucion;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

public class Interfaz {

	/**
	 * Atributos de la clase.
	 */
	private JFrame frmForvotabloader;
	private JTextField txtColumna;
	private static File fichero;
	private static int columna;
	private JLabel lblPath;

	/**
	 * Launch the application.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception 
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interfaz window = new Interfaz();
					window.frmForvotabloader.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
	}

	/**
	 * Create the application.
	 */
	public Interfaz() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmForvotabloader = new JFrame();
		frmForvotabloader.setTitle("ForvoCSVReader");
		frmForvotabloader.setBounds(100, 100, 550, 130);
		frmForvotabloader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JButton btnDireccinDelArchivo = new JButton("Direcci\u00F3n del archivo");
		btnDireccinDelArchivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser selectorArchivos = new JFileChooser();
				selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				
				
				
				
				
				File workingDirectory = new File("C:\\Users\\Shark\\OneDrive - Universidad de los Andes\\Japonés\\Bases de datos Anki\\Texto de ubicación.txt");
				selectorArchivos.setCurrentDirectory(workingDirectory);
				selectorArchivos.setSelectedFile(workingDirectory);

				// Indica cual fue la accion de usuario sobre el JFilechooser
				int resultado = selectorArchivos.showOpenDialog(btnDireccinDelArchivo);
				if (resultado == JFileChooser.APPROVE_OPTION)
				{
				   fichero = selectorArchivos.getSelectedFile();
				   lblPath.setText(fichero.getAbsolutePath());  
				}
			}
		});
		frmForvotabloader.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		frmForvotabloader.getContentPane().add(btnDireccinDelArchivo);
		
		lblPath = new JLabel("No se ha seleccionado");
		lblPath.setHorizontalAlignment(SwingConstants.CENTER);
		frmForvotabloader.getContentPane().add(lblPath);
		
		JLabel ingreseNumeroColumna = new JLabel("Ingrese el n\u00FAmero de la columna donde se encuentra la expresi\u00F3n:");
		ingreseNumeroColumna.setHorizontalAlignment(SwingConstants.CENTER);
		frmForvotabloader.getContentPane().add(ingreseNumeroColumna);
		
		txtColumna = new JTextField();
		txtColumna.setHorizontalAlignment(SwingConstants.CENTER);
		txtColumna.setText("Ejemplo: 2\r\n");
		frmForvotabloader.getContentPane().add(txtColumna);
		txtColumna.setColumns(10);
		
		JButton btnNewButton = new JButton("Ejecutar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				columna = Integer.parseInt(txtColumna.getText());
				try {
					System.out.println("Se ejecuta el programa.");
					ForvoPageOpen main= new ForvoPageOpen(columna-1, fichero, ";");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		frmForvotabloader.getContentPane().add(btnNewButton);
	}

}
