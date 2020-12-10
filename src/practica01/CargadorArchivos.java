package practica01;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CargadorArchivos {

	public static Empresa[][] matrizEmpresas;

	public static void cargarArchivo(String archivo) throws FileNotFoundException{
		File file = new File(archivo);
		Scanner sc = new Scanner(file);
		String linea = "";
		String[] x ;
		int f=0, c=0;
//		boolean creado = true;
		while (sc.hasNextLine()) {
			linea = sc.nextLine();
			if(linea.startsWith("%") || linea.isEmpty()) continue;
			if(linea.startsWith("@L")){
				while (sc.hasNextLine()) {
					linea = sc.nextLine().trim();
					if(linea.isEmpty()) continue;
					x = linea.split(" ");

					String nombre = x[0];
					String vierte = x[1];
					double flujo = Double.parseDouble(x[2].replace(",", "."));
					x=x[4].split(";");
					HashMap<String, Double> hm = new HashMap<String, Double>();
					for (int i = 0; i < x.length; i++) {
						hm.put("c"+(i+1), Double.parseDouble(x[i].replace(",", ".")));
					}
					Empresa e = new Empresa(nombre, vierte, flujo, hm);
					matrizEmpresas[f][c]=e;
					c++;
					f+=c/matrizEmpresas[0].length;
					c%=matrizEmpresas[0].length;
				}
			}
			if(linea.startsWith("@n")){
				linea=linea.replace("@n ", "");
				linea=linea.replace("m ", "");
				x=linea.split(" ");
				matrizEmpresas = new Empresa[Integer.parseInt(x[1])][Integer.parseInt(x[0])];
			}
//			if(creado){
//				x=linea.split("x");
//				matrizEmpresas = new Empresa[Integer.parseInt(x[1])][Integer.parseInt(x[0])];
//				creado = false;
//			}

		}
		sc.close();

	}

	public static void cargarSensores(ArrayList<DobleArrayList<Sensor>> colector){
		colector.clear();
		for (int i = 0; i <= matrizEmpresas[0].length; i++) { //Insercion las estructuras en la avenida principal (No incluye sensores)
			colector.add(new DobleArrayList<Sensor>());
		}
		for (int i = 0; i < matrizEmpresas.length/2; i++) {//Generar sensores por defecto del norte desde AXX, seguido de BNXX en adelante
			for (int j = 0; j <= matrizEmpresas[0].length; j++) {
				colector.get(j).addNorte(new Sensor(generarLetra(i)+(i==0?"":"N")+(j+1)));
			}
		}

		for (int i = 0; i < matrizEmpresas.length/2-1; i++) {//Generar sensores por defecto del sur desde BSXX en adelante (un sensor menos al sur ya que es innecesario)
			for (int j = 0; j <= matrizEmpresas[0].length; j++) {
				colector.get(j).addSur(new Sensor(generarLetra(i+1)+"S"+(j+1)));
			}
		}

		//Agregamos las empresas del norte a los sensores y recalcula sus caudales y contaminantes
		for (int i = 0; i < matrizEmpresas.length/2; i++) {
			//Inserta las empresas de una avenida (fila en la matriz), por ejemplo, CN01, CN02, CN03,...
			for (int j = 0; j < matrizEmpresas[0].length; j++) {
				int posicion = 0;
				if(matrizEmpresas[i][j].isDireccionVertido().equals("-->")){//Si vierte a la derecha, vierte a la calle siguiente
					posicion++;
				}
				//Se añade al sensor el flujo y el contaminante de la empresa
				colector.get(j+posicion).getNorte().get(matrizEmpresas.length/2-i-1).agregarFlujoYContaminantes(matrizEmpresas[i][j]);

			}
			//Si no es la ultima empresa, le suma el caudal y el contaminante del sensor superior (por ejemplo: BN01 = BN01 + CN01)
			if(i>0){
				for (int j = 0; j < matrizEmpresas[0].length+1; j++) {
					Sensor superior = colector.get(j).getNorte().get(matrizEmpresas.length/2-i);
					colector.get(j).getNorte().get(matrizEmpresas.length/2-i-1).agregarFlujoYContaminantesSensor(superior);
				}
			}
		}

		//Agregamos las empresas del sur a los sensores y recalcula sus caudales y contaminantes
		for (int i = matrizEmpresas.length-1; i >= matrizEmpresas.length/2; i--) {
			//Inserta las empresas de una avenida (fila en la matriz), por ejemplo, CS01, CS02, CS03,...
			for (int j = 0; j < matrizEmpresas[0].length; j++) {
				int posicion = 0;
				if(matrizEmpresas[i][j].isDireccionVertido().equals("-->")){
					posicion++;
				}
				//Si es la primera empresa del sur, su caudal es agregado al sensor de la avenida (primero del norte)
				if(i==matrizEmpresas.length/2){
					colector.get(j+posicion).getPrimeroNorte().agregarFlujoYContaminantes(matrizEmpresas[i][j]);
				}else{
					//Se añade al sensor el flujo y el contaminante de la empresa
					colector.get(j+posicion).getSur().get(i-matrizEmpresas.length/2-1).agregarFlujoYContaminantes(matrizEmpresas[i][j]);
				}

			}

			//Si no es la ultima empresa, le suma el caudal y el contaminante del sensor superior (por ejemplo: CS01 = CS01 + DS01)
			//SI es la primera empresa del sur, su caudal es agregado al norte (por ejemplo: A01 = A01 + BS01)
			if(i==matrizEmpresas.length/2){
				for (int j = 0; j < matrizEmpresas[0].length+1; j++) {
					Sensor superior = colector.get(j).getSur().get(i-matrizEmpresas.length/2);
					colector.get(j).getPrimeroNorte().agregarFlujoYContaminantesSensor(superior);//Se agrega a AXX
				}
			}else if(i<matrizEmpresas.length-1){
				for (int j = 0; j < matrizEmpresas[0].length+1; j++) {
					Sensor superior = colector.get(j).getSur().get(i-matrizEmpresas.length/2);
					colector.get(j).getSur().get(i-matrizEmpresas.length/2-1).agregarFlujoYContaminantesSensor(superior);
				}
			}
		}

		//Se entiende que a los sensores nombrados como "A" son sensores que recogen los caudales de las empresas "AXX" y "BSXX", así como el que proviene
		//del sensor "BNXX" junto con el del sensor sur "BSXX", además del caudal que proviene del sensor "AXX+1"
		for (int i = matrizEmpresas[0].length; i>=0; i--) {
			Sensor s = colector.get(i).getPrimeroNorte();
			if(i<matrizEmpresas[0].length){
				s.agregarFlujoYContaminantesSensor(colector.get(i+1).getPrimeroNorte());
			}
		}

	}

	//Carga el archivo de datos con los limites criticos y de desvio sobre el mapa pasado por parametro
	public static void cargarNiveles(HashMap<String, Double[]> niveles, String archivo) throws FileNotFoundException {
		File file = new File(archivo);
		Scanner sc = new Scanner(file);
		String[] x;
		while (sc.hasNextLine()) {
			x = sc.nextLine().split("\t");
			niveles.put(x[0], new Double[]{Double.parseDouble(x[1]),Double.parseDouble(x[2])});
		}
		sc.close();
	}

	private static String generarLetra(int i){
		String s = "";
		do{
			s = s + (char)(65+i%26);
			i=i/26-1;
		}while(i>0);
		return s;
	}

}
