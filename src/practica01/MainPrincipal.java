package practica01;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainPrincipal {

	private static String dir = System.getProperty("user.dir")+File.separator+"src"+File.separator+"practica01"+File.separator;
	private static Depuradora depuradora;
	private static String archivoDatos = "";
	private static String archivoNiveles = "";
	private static JFileChooser jfc = new JFileChooser(dir);

	public static void main(String[] args) {
		cabecera();
		int opcion=0;
		do {
			opcion = menu();
			switch (opcion) {
			case 1:
				System.out.println("|==================================================|");
				System.out.println("|            CARGA DE DATOS DE EMPRESA             |");
				System.out.println("|==================================================|");
				if(archivoNiveles.isEmpty()){
					System.out.println("Debe cargar seleccionar primero el archivo de niveles");
				}else if(seleccionarArchivo()){
					try {
						inicializarEscenario();
						boolean verConsola=verConsola();
						boolean verGrafico=verGrafico();
						int algoritmo = seleccionarAlgoritmo();
						depuradora.comprobarEstado(verConsola, verGrafico, algoritmo);
					} catch (FileNotFoundException e) {
						System.out.println("ERROR: No se pudo inicializar el escenario");
					}
				}else{
					System.out.println("No se procederá a realizar la simulación");
				}
				break;
			case 2:
				seleccionarArchivoNiveles();
				break;
			case 3:
				System.out.println("|==================================================|");
				System.out.println("|      PRUEBA DE RENDIMIENTO DE ALGORITMOS         |");
				System.out.println("|==================================================|");
				int caso = submenu();
				try {
					Rendimiento rendimiento = new Rendimiento();
					switch (caso) {
					case 1:rendimiento.medir("Caso01");	break;
					case 2:rendimiento.medir("Caso02");	break;
					case 3:rendimiento.medir("Caso03");	break;
					case 4:rendimiento.medir("Caso04");	break;
					default:
						break;
					}
				} catch (FileNotFoundException e) {
					System.out.println("No se pudieron cargar los casos");
				}
				break;

			default:
				cierre();
				break;
			}

		} while (opcion!=4);
	}

	public static void cabecera(){
		System.out.println("|==================================================|");
		System.out.println("|     SISTEMA DE CONTROL DE CONTAMINACION EIDZ     |");
		System.out.println("|          New Almeria City, Boise (Idaho)         |");
		System.out.println("|==================================================|");
	}

	public static int menu(){
		System.out.println("|                                                  |");
		System.out.println("|   Seleccione una opción:                         |");
		System.out.println("|    1. Carga de datos de empresa.                 |");
		System.out.println("|    2. Carga de niveles criticos y desvios        |");
		System.out.println("|    3. Prueba de rendimiento de los algoritmos    |");
		System.out.println("|    4. Salir                                      |");
		return seleccionarOpcion(1, 4);
	}

	public static int submenu(){
		System.out.println("|                                                  |");
		System.out.println("|   Seleccione qué prueba desea efectuar           |");
		System.out.println("|    1. Caso01                                     |");
		System.out.println("|    2. Caso02                                     |");
		System.out.println("|    3. Caso03                                     |");
		System.out.println("|    4. Caso04                                     |");
		System.out.println("|    5. Salir                                      |");
		return seleccionarOpcion(1, 5);
	}

	public static void inicializarEscenario() throws FileNotFoundException{
		depuradora = new Depuradora();
		CargadorArchivos.cargarArchivo(archivoDatos);
		CargadorArchivos.cargarSensores(depuradora.getColector());
		depuradora.cargarNiveles(archivoNiveles);
	}

	private static boolean verConsola() {
		System.out.println("| ¿Desea mostrar por pantalla los resultados?      |");
		System.out.println("|   Seleccione una opción:                         |");
		System.out.println("|    1. Si                                         |");
		System.out.println("|    2. No                                         |");
		return seleccionarOpcion(1, 2)==1;
	}

	private static boolean verGrafico() {
		System.out.println("| ¿Desea mostrar el mapa al finalizar la busqueda? |");
		System.out.println("|   Seleccione una opción:                         |");
		System.out.println("|    1. Si                                         |");
		System.out.println("|    2. No                                         |");
		return seleccionarOpcion(1, 2)==1;
	}

	private static int seleccionarAlgoritmo() {
		System.out.println("| ¿Con que algoritmo desea realizar la busqueda?   |");
		System.out.println("|   Seleccione una opción:                         |");
		System.out.println("|    1. Fuerza Bruta                               |");
		System.out.println("|    2. Divide y Vencerás                          |");
		System.out.println("|    3. Divide y Vencerás Mejorado                 |");
		return seleccionarOpcion(1, 3);
	}

	public static void cierre(){
		System.out.println("|==================================================|");
		System.out.println("|     Practica realizada por:                      |");
		System.out.println("|                           Nector Rosales Ortuño  |");
		System.out.println("|                     Alejandro J. López Martínez  |");
		System.out.println("|                  Jefferson Max Tomala Villareal  |");
		System.out.println("|==================================================|");
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

	private static int seleccionarOpcion(int rangoInferior, int rangoSuperior){
		Scanner sc = new Scanner(System.in);
		do {
			try{
				int opcion = Integer.parseInt(sc.nextLine());
				if(opcion>=rangoInferior && opcion<=rangoSuperior){
					return opcion;
				}
			}catch (NumberFormatException e) {}
			System.out.println("No ha introducido una opcion válida\nSeleccione opción entre "+rangoInferior+" y "+rangoSuperior);
		} while (true);
	}

}
