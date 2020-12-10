package practica01;

import java.text.DecimalFormat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class GeneradorArchivos {

	/** The calle. */
	public static int calle;

	/** The avenida. */
	public static int avenida;

	/** The n empresas. */
	public static int nEmpresas;

	/** The a principal. */
	public static int aPrincipal;

	/** The formato flujo empresa. */
	public static DecimalFormat formatoFlujoEmpresa;

	/** The flujo empresa. */
	public static double flujoEmpresa;

	/** The formato contaminante. */
	public static DecimalFormat formatoContaminante;

	/** The contaminante. */
	public static double contaminante;

	/** The calles. */
	public static int calles;

	/** The avenidas. */
	public static int avenidas;

	static InputStreamReader isr = new InputStreamReader(System.in);
	static BufferedReader br = new BufferedReader(isr);

	/**
	 * Matriz empresa.
	 *
	 * @return the string
	 */
	public static String matrizEmpresa() {
		String salida = "%@MATRIZ_EMPRESAS\n%";
		char[] s;
		String[] s2;
		s = new char[26];
		s2 = new String[702];
		int cont = 0;

		// for que carga en dos arrays el abecedario
		for (int i = 0; i < 26; i++) {
			s[i] = (char) ('A' + i);
			s2[i] = String.valueOf(s[i]);
			cont++;
		}
		// for que recorre las letras del abecedario
		for (int i = 26; i <= 51; i++) {
			int posicion = i - 26;

			// for que concatena las letras del abecedario del for anterior con
			// las del primer array para rellenar el segundo array
			for (int z = 0; z < 26; z++) {
				s2[cont] = String.valueOf(s[posicion]) + String.valueOf(s[z]);
				cont++;
			}
		}

		if (avenida % 2 != 0) {
			for (int i = aPrincipal - 1; i >= 0; i--) {
				for (int j = 1; j <= calle; j++) {
					salida += s2[i];
					if (i == 0) {
						salida += j + " ";
					} else {
						salida += "N" + j + " ";
					}

				}
				salida += "\n%";
			}
		} else {
			for (int i = (aPrincipal - 1); i >= 0; i--) {
				for (int j = 1; j <= calle; j++) {
					salida += s2[i];
					if (i == 0) {
						salida += j + " ";
					} else {
						salida += "N" + j + " ";
					}
				}
				salida += "\n%";
			}
		}

		for (int i = 1; i <= aPrincipal; i++) {
			for (int j = 1; j <= calle; j++) {
				salida += s2[i];
				salida += "S" + j + " ";
			}
			salida += "\n%";
		}
		System.out.println(salida);
		return salida;
	}

	/**
	 * Matriz sensor.
	 *
	 * @return the string
	 */
	public static String matrizSensor() {
		String salida = "%@MATRIZ_SENSORES\n%";
		char[] s;
		String[] s2;
		s = new char[26];
		s2 = new String[702];
		int cont = 0;

		// for que carga en dos arrays el abecedario
		for (int i = 0; i < 26; i++) {
			s[i] = (char) ('A' + i);
			s2[i] = String.valueOf(s[i]);
			cont++;
		}
		// for que recorre las letras del abecedario
		for (int i = 26; i <= 51; i++) {
			int posicion = i - 26;

			// for que concatena las letras del abecedario del for anterior con
			// las del primer array para rellenar el segundo array
			for (int z = 0; z < 26; z++) {
				s2[cont] = String.valueOf(s[posicion]) + String.valueOf(s[z]);
				cont++;
			}
		}

		for (int i = (aPrincipal - 1); i > 0; i--) {
			for (int j = 1; j < calles + 2; j++) {
				salida += s2[i];
				if (i == 0) {
					salida += j + " ";
				} else {
					salida += "N" + j + " ";
				}
			}
			salida += "\n%";
		}

		for (int i = 1; i < calles + 2; i++) {
			salida += "A" + i + " ";
		}
		salida += "\n%";

		for (int i = 1; i <= aPrincipal; i++) {
			for (int j = 1; j <= calles + 1; j++) {
				salida += s2[i];
				salida += "S" + j + " ";
			}
			salida += "\n%";
		}
		System.out.println(salida);
		return salida;
	}

	/**
	 * Listado.
	 *
	 * @return the string
	 */
	public static String listado() {
		String salida = "\n@LISTADO\n";
		int direccion;
		char[] s;
		String[] s2;
		s = new char[26];
		s2 = new String[702];
		int cont = 0;

		// for que carga en dos arrays el abecedario
		for (int i = 0; i < 26; i++) {
			s[i] = (char) ('A' + i);
			s2[i] = String.valueOf(s[i]);
			cont++;
		}
		// for que recorre las letras del abecedario
		for (int i = 26; i <= 51; i++) {
			int posicion = i - 26;

			// for que concatena las letras del abecedario del for anterior con
			// las del primer array para rellenar el segundo array
			for (int z = 0; z < 26; z++) {
				s2[cont] = String.valueOf(s[posicion]) + String.valueOf(s[z]);
				cont++;
			}
		}

		for (int i = (aPrincipal - 1); i >= 0; i--) {
			for (int j = 1; j <= calle; j++) {
				direccion = (int) (Math.random() * 10);
				salida += s2[i];
				if (i == 0) {
					salida += j + " ";
				} else {
					salida += "N" + j + " ";
				}
				if (direccion <= 5) {
					salida += "<-- ";
				} else {
					salida += "--> ";
				}
				formatoFlujoEmpresa = new DecimalFormat();
				formatoFlujoEmpresa.setMinimumFractionDigits(2);
				formatoFlujoEmpresa.setMaximumFractionDigits(2);
				flujoEmpresa = Math.random() * 100;
				salida += formatoFlujoEmpresa.format(flujoEmpresa) + " - ";

				for (int z = 0; z < 2; z++) {
					formatoContaminante = new DecimalFormat();
					formatoContaminante.setMinimumFractionDigits(2);
					formatoContaminante.setMaximumFractionDigits(2);
					flujoEmpresa = Math.random() * 0.01;
					salida += formatoContaminante.format(flujoEmpresa) + ";";
				}
				salida += "\n";
			}
			salida += "\n";
		}

		for (int i = 1; i <= aPrincipal; i++) {
			for (int j = 1; j <= calle; j++) {
				direccion = (int) (Math.random() * 10);
				salida += s2[i];
				salida += "S" + j;
				if (direccion <= 5) {
					salida += " <-- ";
				} else {
					salida += " --> ";
				}
				formatoFlujoEmpresa = new DecimalFormat();
				formatoFlujoEmpresa.setMinimumFractionDigits(2);
				formatoFlujoEmpresa.setMaximumFractionDigits(2);
				flujoEmpresa = Math.random() * 100;
				salida += formatoFlujoEmpresa.format(flujoEmpresa) + " - ";

				for (int z = 0; z < 2; z++) {
					formatoContaminante = new DecimalFormat();
					formatoContaminante.setMinimumFractionDigits(2);
					formatoContaminante.setMaximumFractionDigits(2);
					flujoEmpresa = Math.random() * 0.01;
					salida += formatoContaminante.format(flujoEmpresa) + ";";
				}
				salida += "\n";

			}
			salida += "\n";
		}
		System.out.println(salida);
		return salida;
	}


	/**
	 * Crear archivo.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void crearArchivo() throws IOException {

		String info = "\n@n " + (calles) + " m " + avenidas + "\n";
		String ruta = "src/org/datos.txt";
		File archivo = new File(ruta);
		BufferedWriter bw;
		if (archivo.exists()) {
			bw = new BufferedWriter(new FileWriter(archivo));
			bw.write("El fichero de texto ya estaba creado.");
		} else {
			bw = new BufferedWriter(new FileWriter(archivo));
			bw.write(matrizEmpresa() + matrizSensor() + info + listado());
		}
		bw.close();
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {

		System.out.println("Introduce el número de calles (columnas): ");
		int numero2 = Integer.parseInt(br.readLine());
		// calle = (int) (Math.random() * (20 - 1) + 1);
		// calles = calle + 1;
		calles = numero2;
		calle = calles;
		int numero = 1;
		do {
			// avenida = (int) (Math.random() * (12 - 1) + 1);
			// avenidas = avenida + 1;
			// nEmpresas = (calle) * (avenida);
			System.out.println("Introduce el número de avenidas (filas) [NOTA: Deben ser pares]: ");
			numero = Integer.parseInt(br.readLine());
			avenidas = numero;
			avenida = avenidas;
			nEmpresas = numero * numero2;
		} while (numero % 2 != 0);

		aPrincipal = avenida / 2;
		System.out.println("%Nº DE EMPRESAS = " + (nEmpresas));
		System.out.println("%MATRIZ DE  = " + calle + " x " + avenida + "= " + nEmpresas);
		//matrizEmpresa();
		System.out.println("MATRIZ DE SENSORES CON " + (calles) + " CALLES POR " + avenidas + " AVENIDAS");
		if (avenida == 1) {
			System.out.println("AVENIDA PRINCIPAL: 2");
		} else if (avenida % 2 == 1) {
			System.out.println("AVENIDA PRINCIPAL: " + (aPrincipal + 2));
		} else {
			System.out.println("AVENIDA PRINCIPAL: " + (aPrincipal + 1));
		}
		//matrizSensor();

		//listado();

		crearArchivo();

	}

}
