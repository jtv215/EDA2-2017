package practica01;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.JSpinner;
import java.awt.Dimension;
import javax.swing.SpinnerNumberModel;

public class MainUserInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static String dir = System.getProperty("user.dir")+File.separator+"src"+File.separator+"practica01"+File.separator;
	private static Depuradora depuradora;
	private static String archivoDatos = "";
	private static String archivoNiveles = "";
	private static JFileChooser jfc = new JFileChooser(dir);
	private static JPanel panel_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
					    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					        if ("Nimbus".equals(info.getName())) {
					            UIManager.setLookAndFeel(info.getClassName());
					            break;
					        }
					    }
					} catch (Exception e) {}
					MainUserInterface frame = new MainUserInterface();
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
	public MainUserInterface() {
		setTitle("SISTEMA DE CONTROL DE CONTAMINACION EIDZ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 770, 453);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.WEST);

		JButton btnNiveles = new JButton("Niveles");
		panel_1.add(btnNiveles);

		JButton btnAbrir = new JButton("Abrir");
		panel_1.add(btnAbrir);

		JPanel panel_4 = new JPanel();
		panel.add(panel_4, BorderLayout.EAST);

		JButton btnAnalizar = new JButton("Analizar");
		panel_4.add(btnAnalizar);

		JButton btnRendimiento = new JButton("Rendimiento");
		panel_4.add(btnRendimiento);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_2, BorderLayout.SOUTH);

		JLabel lblNewLabel = new JLabel("Seleccione el archivo de niveles...");
		panel_2.add(lblNewLabel);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		panel_3 = new JPanel();
		scrollPane.setViewportView(panel_3);

		btnAbrir.setEnabled(false);
		btnAnalizar.setEnabled(false);
		btnRendimiento.setEnabled(false);

		JPanel panel_5 = new JPanel();
		panel.add(panel_5, BorderLayout.CENTER);

		JLabel lblCapacidadBalsamax = new JLabel("Capacidad Balsa (max):");
		panel_5.add(lblCapacidadBalsamax);

		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Double(10000), new Double(0), null, new Double(1)));
		spinner.setPreferredSize(new Dimension(75, 30));
		spinner.setMinimumSize(new Dimension(50, 20));
		panel_5.add(spinner);

		JLabel lblOcupacinActual = new JLabel("Ocupaci\u00F3n Actual:");
		panel_5.add(lblOcupacinActual);

		JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(1)));
		spinner_1.setPreferredSize(new Dimension(75, 30));
		panel_5.add(spinner_1);

		ActionListener niveles = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				seleccionarArchivoNiveles();
				if(!archivoNiveles.isEmpty()){
					btnAbrir.setEnabled(true);
					btnRendimiento.setEnabled(true);
					lblNewLabel.setText("Seleccione el archivo de datos...");
				}
			}
		};

		ActionListener abrir = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(seleccionarArchivo()){
					try {
						inicializarEscenario();
						createMap();
						btnAnalizar.setEnabled(true);
						lblNewLabel.setText(archivoDatos);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		};

		ActionListener analizar = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int value = -1;
				Object[] possibilities = {"1-Fuerza Bruta", "2-Divide y Vencerás", "3-Divide y Vencerás Mejorado", "4-GreedyA", "5-GreedyA Mejorado", "6-GreedyB"};
				String s = (String)JOptionPane.showInputDialog(
				                    contentPane,
				                    "Seleccione el algoritmo:",
				                    "Algoritmo",
				                    JOptionPane.QUESTION_MESSAGE,
				                    null,
				                    possibilities,
				                    "2-Divide y Vencerás");

				if ((s != null) && (s.length() > 0)) {
					value = Integer.parseInt(s.split("-")[0]);
				}
				depuradora.setCapacidadEmbalse((double) spinner.getValue());
				depuradora.setOcupadoEmbalse((double) spinner_1.getValue());
				depuradora.comprobarEstado(true, false, value);
				if(value!=6){
					createMap(depuradora.getCulpables());
				}else{
					createMap(depuradora.getCulpablesGreddy());
				}
			}

		};

		ActionListener rendimiento = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Rendimiento rendimiento = new Rendimiento(archivoNiveles);
					Object[] possibilities = {"Caso01", "Caso02", "Caso03", "Caso04"};
					String s = (String)JOptionPane.showInputDialog(
					                    contentPane,
					                    "Seleccione la prueba que desea realizar:",
					                    "Prueba de rendimiento",
					                    JOptionPane.QUESTION_MESSAGE,
					                    null,
					                    possibilities,
					                    "Caso01");

					if ((s != null) && (s.length() > 0)) {
						rendimiento.medir(s);
					}
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		};

		btnNiveles.addActionListener(niveles);
		btnAbrir.addActionListener(abrir);
		btnAnalizar.addActionListener(analizar);
		btnRendimiento.addActionListener(rendimiento);

	}

	private void createMap() {
		panel_3.removeAll();

		int alto = CargadorArchivos.matrizEmpresas.length;
		int ancho = CargadorArchivos.matrizEmpresas[0].length;
		panel_3.setLayout(new GridLayout(alto, ancho));
		if(alto*ancho>4096){//Se evita por eficiencia de memoria
//			panel_3.add(new JLabel("Demasiadas empresas para representar"), BorderLayout.NORTH);
			JOptionPane.showMessageDialog(contentPane, "Para evitar el consumo de memoria,\nse mostrará un esquema de situacion", "Atención", JOptionPane.WARNING_MESSAGE);
			panel_3.setLayout(new BorderLayout());
			boolean[][] infractores = new boolean[CargadorArchivos.matrizEmpresas.length][CargadorArchivos.matrizEmpresas[0].length];
//			for (int i = 0; i < CargadorArchivos.matrizEmpresas.length; i++) {
//				for (int j = 0; j < CargadorArchivos.matrizEmpresas[0].length; j++) {
//					infractores[i][j]=culpables.contains(CargadorArchivos.matrizEmpresas.length.getNombre());
//				}
//			}

			GridsCanvas xyz = new GridsCanvas(ancho, alto, CargadorArchivos.matrizEmpresas, infractores);
			panel_3.add(xyz, BorderLayout.CENTER);
		}else{
			for (int i = 0; i < alto; i++) {
				for (int j = 0; j < ancho; j++) {
					JPanel jp = new EmpresaPanel(CargadorArchivos.matrizEmpresas[i][j]);
					jp.setBorder(new LineBorder(Color.BLACK));
					if(i<CargadorArchivos.matrizEmpresas.length/2){
						jp.setBackground(new Color(226, 226, 226));
					}else{
						jp.setBackground(new Color(204, 204, 204));
					}

					panel_3.add(jp);
				}
			}
		}
		panel_3.revalidate();
		panel_3.repaint();
	}

	private void createMap(TreeSet<String> cuplables) {
		panel_3.removeAll();

		int alto = CargadorArchivos.matrizEmpresas.length;
		int ancho = CargadorArchivos.matrizEmpresas[0].length;
		panel_3.setLayout(new GridLayout(alto, ancho));
		if(alto*ancho>4096){//Se evita por eficiencia de memoria
//			panel_3.add(new JLabel("Demasiadas empresas para representar"));
//			JOptionPane.showMessageDialog(contentPane, "Para evitar el consumo de memoria,\nse mostrará un esquema de situacion", "Atención", JOptionPane.WARNING_MESSAGE);
			panel_3.setLayout(new BorderLayout());
			boolean[][] infractores = new boolean[CargadorArchivos.matrizEmpresas.length][CargadorArchivos.matrizEmpresas[0].length];
			for (int i = 0; i < CargadorArchivos.matrizEmpresas.length; i++) {
				for (int j = 0; j < CargadorArchivos.matrizEmpresas[0].length; j++) {
					infractores[i][j]=cuplables.contains(CargadorArchivos.matrizEmpresas[i][j].getNombre()+" "+CargadorArchivos.matrizEmpresas[i][j].isDireccionVertido());
				}
			}

			GridsCanvas xyz = new GridsCanvas(ancho, alto, CargadorArchivos.matrizEmpresas, infractores);
			panel_3.add(xyz, BorderLayout.CENTER);
		}else{
			for (int i = 0; i < alto; i++) {
				for (int j = 0; j < ancho; j++) {
					JPanel jp = new EmpresaPanel(CargadorArchivos.matrizEmpresas[i][j]);
					jp.setBorder(new LineBorder(Color.BLACK));
					if(i<CargadorArchivos.matrizEmpresas.length/2){
						if(cuplables.contains(CargadorArchivos.matrizEmpresas[i][j].getNombre()+" "+CargadorArchivos.matrizEmpresas[i][j].isDireccionVertido())){
							jp.setBackground(Color.RED);
						}else{
							jp.setBackground(new Color(226, 226, 226));
						}
					}else{
						if(cuplables.contains(CargadorArchivos.matrizEmpresas[i][j].getNombre()+" "+CargadorArchivos.matrizEmpresas[i][j].isDireccionVertido())){
							jp.setBackground(Color.RED);
						}else{
							jp.setBackground(new Color(204, 204, 204));
						}
					}

					panel_3.add(jp);
				}
			}
		}
		panel_3.revalidate();
		panel_3.repaint();
	}

	private void createMap(TreeMap<String, TreeSet<String>> culpablesGreddy) {
		panel_3.removeAll();

		int alto = CargadorArchivos.matrizEmpresas.length;
		int ancho = CargadorArchivos.matrizEmpresas[0].length;
		panel_3.setLayout(new GridLayout(alto, ancho));
		if(alto*ancho>4096){//Se evita por eficiencia de memoria
			panel_3.add(new JLabel("Demasiadas empresas para representar"));
//			JOptionPane.showMessageDialog(contentPane, "Para evitar el consumo de memoria,\nse mostrará un esquema de situacion", "Atención", JOptionPane.WARNING_MESSAGE);
//			panel_3.setLayout(new BorderLayout());
//			boolean[][] infractores = new boolean[CargadorArchivos.matrizEmpresas.length][CargadorArchivos.matrizEmpresas[0].length];
//			for (int i = 0; i < CargadorArchivos.matrizEmpresas.length; i++) {
//				for (int j = 0; j < CargadorArchivos.matrizEmpresas[0].length; j++) {
//					infractores[i][j]=cuplables.contains(CargadorArchivos.matrizEmpresas[i][j].getNombre());
//				}
//			}

//			GridsCanvas xyz = new GridsCanvas(ancho, alto, CargadorArchivos.matrizEmpresas, infractores);
//			panel_3.add(xyz, BorderLayout.CENTER);
		}else{
			for (int i = 0; i < alto; i++) {
				for (int j = 0; j < ancho; j++) {
					JPanel jp = new EmpresaPanel(CargadorArchivos.matrizEmpresas[i][j]);
					jp.setBorder(new LineBorder(Color.BLACK));
					String empresa = CargadorArchivos.matrizEmpresas[i][j].getNombre();
					empresa +=" "+CargadorArchivos.matrizEmpresas[i][j].isDireccionVertido();
						if(culpablesGreddy.get("sR").contains(empresa)){
							jp.setBackground(Color.decode("#FF0000"));
						}else if(culpablesGreddy.get("sN").contains(empresa)){
							jp.setBackground(Color.decode("#FF8000"));
						}else if(culpablesGreddy.get("sA").contains(empresa)){
							jp.setBackground(Color.decode("#FFFF00"));
						}else if(culpablesGreddy.get("gR").contains(empresa)){
							jp.setBackground(Color.decode("#F5A9A9"));
						}else if(culpablesGreddy.get("gN").contains(empresa)){
							jp.setBackground(Color.decode("#F5D0A9"));
						}else if(culpablesGreddy.get("gA").contains(empresa)){
							jp.setBackground(Color.decode("#F2F5A9"));
						}else{
							jp.setBackground(Color.decode("#00FF00"));

						}

					panel_3.add(jp);
				}
			}
		}
		panel_3.revalidate();
		panel_3.repaint();

	}

	public static void inicializarEscenario() throws FileNotFoundException{
		depuradora = new Depuradora();
		CargadorArchivos.cargarArchivo(archivoDatos);
		CargadorArchivos.cargarSensores(depuradora.getColector());
		depuradora.cargarNiveles(archivoNiveles);
	}

	public static boolean seleccionarArchivo(){
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setMultiSelectionEnabled(false);
		FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.TXT", "txt");
		jfc.setFileFilter(filtro);

		jfc.setApproveButtonText("Abrir archivo");
		jfc.setDialogTitle("Seleccione los datos");
		int seleccion = jfc.showOpenDialog(null);
		File abre = jfc.getSelectedFile();
		if(seleccion==JFileChooser.APPROVE_OPTION && abre!=null){
			archivoDatos = abre.getAbsolutePath();
		}else{
			return false;
		}
		return true;
	}

	public static boolean seleccionarArchivoNiveles(){
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setMultiSelectionEnabled(false);
		FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.TXT", "txt");
		jfc.setFileFilter(filtro);

		jfc.setApproveButtonText("Abrir archivo");

		jfc.setDialogTitle("Seleccione el archivo de niveles");
		int seleccion = jfc.showOpenDialog(null);
		File abre=jfc.getSelectedFile();
		if(seleccion == JFileChooser.APPROVE_OPTION && abre!=null){
			archivoNiveles = abre.getAbsolutePath();
		}else{
			return false;
		}
		return true;
	}

}
