package practica03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	private static String dir = System.getProperty("user.dir")+File.separator+"src"+File.separator+"org"+File.separator;
	private static double limiteDesvio = 1.0;
	private static double limiteMax;

	public static void main(String[] args) throws NumberFormatException, IOException {
		limiteMax = limiteDesvio * 0.25;
		generarArchivo(dir+"datos.txt", dir+"compradores.txt", dir+"ofertas.txt");
		System.out.println("...Ficheros generados correctamente en /src/org/");
	}

	public static void generarArchivo(String archivo, String salida, String salida2) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(archivo));
		PrintWriter pw = new PrintWriter(new File(salida));
		PrintWriter pw2 = new PrintWriter(new File(salida2));
		while (sc.hasNextLine()) {
			String nombre = sc.nextLine().split(" ")[0].trim();
			if (nombre.startsWith("@")||nombre.startsWith("%") || nombre.isEmpty()) {
				continue;
			}
			int flujo = (int) (Math.random()*101);
			double mgMax = flujo * limiteMax;
			double aleatorio = (Math.random() * mgMax);
			pw.println(nombre+"\t"+flujo+"\t"+(String.format("%.2f", aleatorio)));
			double val = ((mgMax-aleatorio)/mgMax)*100;
			val=(int)(val>50?50:val);
			pw2.println(nombre+"\t"+mgMax+"\t"+(String.format("%.2f", val))+"\t"+(String.format("%.0f", Math.random()*(10-1)+1)));
//			System.out.println();

		}
		sc.close();
		pw.close();
		pw2.close();
	}



















}
