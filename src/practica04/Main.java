package practica04;

import java.io.File;
import java.util.List;

public class Main {

	private static String dir = System.getProperty("user.dir") +
			File.separator + "src" + File.separator + "practica04" + File.separator;

	public static void main(String[] args) {
		String archivo = "datos.txt";
		List<Nodo> listaNodos = CargarDatos.leerDatosE(dir+archivo);
		long start = System.currentTimeMillis();
		Backtracking bt = new Backtracking(480, 30, 5);
		long end = System.currentTimeMillis();
		bt.algoritmoTSP(listaNodos);
		List<Empresa> solucion = bt.getSolucion();
		System.out.print("SOLUCION: ");
		System.out.println(solucion);
		System.out.print("TOTAL(HORAS): ");
		System.out.println(bt.getTotal());
		System.out.println("JORNADAS(DIAS TRABAJADOS): "+((int)(bt.getTotal()/480)+1));
		System.out.println("Comprobacion en "+(end-start)+" Milsegs");
	}
}
