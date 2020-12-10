package practica03;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

public class cargarArchivoDinamica {

	public static ArrayList<String> nombreEmpresa = new ArrayList<String>();
	private static ArrayList<E> gestionResiduos;
	private static ArrayList<E> empresasOfertan;
//	private static String dir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "practica03" + File.separator + "casos"
//			+ File.separator + "Caso01-4x4-Salida.txt";
//	private static String dir2 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "practica03" + File.separator + "casos"
//			+ File.separator + "Caso01-4x4-Salida2.txt";
	private static String dir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "org" + File.separator + "compradores.txt";
	private static String dir2 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "org" + File.separator + "ofertas.txt";
	int[] benefcios;

	public static void leerDatosDinamica(String salida) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(salida));
		gestionResiduos = new ArrayList<E>();
		String[] x;
		String nombre;
		float flujo, contaminante;
		while (sc.hasNextLine()) {
			x = sc.nextLine().split("\t");
			nombre = x[0];
			flujo = Float.parseFloat(x[1]);
			contaminante = Float.parseFloat(x[2].replace(',', '.'));
			E e = new E(nombre, flujo, contaminante);
			gestionResiduos.add(e);
			System.out.println(e.printE1());
		}
		sc.close();
	}

	public static void leerDatosDinamica2(String salida2) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(salida2));
		empresasOfertan = new ArrayList<E>();
		String[] x;
		String nombre;
		float cantidadContrata, porcentajeOfrecido, precio;
		while (sc.hasNextLine()) {
			x = sc.nextLine().split("\t");
			nombre = x[0];
			cantidadContrata = Float.parseFloat(x[1]);
			porcentajeOfrecido = Float.parseFloat(x[2].replace(',', '.'));
			precio = Float.parseFloat(x[3]);
			E o = new E(nombre, cantidadContrata, porcentajeOfrecido, precio);
			empresasOfertan.add(o);
			System.out.println(o.printE2());
		}
		sc.close();
	}

	public static void main(String[] args) throws FileNotFoundException {
		//dir: nombre, flujo lixiviados contratados y contaminante declarado
		//dir2: nombre, cantidad absoluta contratada, porcentaje ofrecito y precio que oferta
		System.out.println("LEER DATOS EMPRESAS COMPRADORAS (Nombre, Flujo lixiviados y Contaminante declarado\n");
		leerDatosDinamica(dir);
		System.out.println("-----------------------------\n");
		System.out.println("LEER DATOS EMPRESAS QUE OFERTAN (Nombre, Cant Abs Contratada, % ofrecido y Precio Ofertado\n");
		leerDatosDinamica2(dir2);
		System.out.println("-----------------------------\n");
		E e = new E("BN2");
		System.out.println("BENEFICIO PARA LA EMPRESA "+e.getNombre().toString()+"\n");
		cargarBeneficio(e);
		System.out.println("-----------------------------\n");
		System.out.println("PESOS EXCEPTO "+e.getNombre().toString()+"\n");
		cargarPeso(e);
		System.out.println("\nNombre de empresas:\n"+nombreEmpresa.toString());
	}

	public static int[] cargarBeneficio(E o) throws FileNotFoundException {
		leerDatosDinamica2(dir2);
		int[] beneficios = new int[empresasOfertan.size()-1];
		int cont = 0;
		for (E e : empresasOfertan) {
			if (!e.getNombre().equals(o.getNombre())) {
				int b = o.beneficio(e);
				beneficios[cont] = b;
				System.out.println(o.getNombre() + " --> " + e.getNombre() + " = " + (beneficios[cont]/Dinamica.factorB)+"m$");
				cont++;
			}
		}
		return beneficios;
	}

	public static int[] cargarPeso(E o) {
		int[] costes = new int[empresasOfertan.size()-1];
		int cont = 0;
		for (E e : empresasOfertan) {
			if (!e.getNombre().equals(o.getNombre())) {
				costes[cont] = (int) (e.getMg() * e.getOfertado());
				nombreEmpresa.add(e.getNombre());
				System.out.println(e.getNombre() + " = " + (costes[cont]/Dinamica.factorP)+"mg");
				cont++;
			}
		}
		return costes;
	}

}
