package practica04;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CargarDatos {

//	private static String dir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "practica04" + File.separator + "datos.txt";
//	private static ArrayList<Empresa> gEmpresas; //grupo de empresas (nodo)

	public static List<Nodo> leerDatosE(String salida) {
		try {
			Scanner sc = new Scanner(new File(salida));
//			gEmpresas = new ArrayList<Empresa>();
			String[] x;
			String nombre = null, vierte = null;
			List<String> contaminantes = null;
			Empresa e = null;
			Nodo n = null;
			List<Empresa> nodo = new ArrayList<Empresa>();
			List<Nodo> zonaGris = new ArrayList<Nodo>();
			while (sc.hasNextLine()) {
				contaminantes = new ArrayList<String>();
				String linea = sc.nextLine();
				if (linea.startsWith("%")) {
					continue;
				}
				if (linea.contains("--")) {
					x = linea.split(" ");
					nombre = x[0];
					vierte = x[1];
					linea = sc.nextLine();
				}
				while(linea.startsWith("c")){
					contaminantes.add(linea);
					linea = sc.nextLine();
				}
				e = new Empresa(nombre, vierte, contaminantes);
				if (!linea.startsWith("#")) {
					nodo.add(e);
				}else {
					n = new Nodo(nodo);
					zonaGris.add(n);
					nodo = new ArrayList<Empresa>();
				}
			}
			sc.close();
			return zonaGris;
		} catch (Exception e) {

		}
		return null;
	}




//	public static void main(String[] args) throws FileNotFoundException {
//
//		ArrayList<Nodo> listaNodos = leerDatosE(dir);
//		System.out.println(listaNodos);
//
//		//Ejemplo de funcionamiento de metodos en Clase Calculador
//		Nodo A = listaNodos.get(0);
//		Nodo B = listaNodos.get(1);
//		System.out.println(Calculador.getCBTInterno(A));
//		System.out.println(Calculador.getCBTInterno(B));
//		System.out.println(Calculador.getPRTInterno(A));
//		System.out.println(Calculador.getPRTInterno(B));
//		System.out.println(Calculador.getCBTNodoDesdeHasta(A, B));
//		System.out.println(Calculador.getCBTNodoDesdeHasta(B, A));
//		System.out.println(Calculador.getCBTHastaPS(A));
//		System.out.println(Calculador.getCBTHastaPS(B));
//		System.out.println(Calculador.getCBTDesdePS(A));
//		System.out.println(Calculador.getCBTDesdePS(B));
//	}




}
