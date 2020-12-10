package practica03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class Dinamica {

	private static String dir = System.getProperty("user.dir")+File.separator+"src"+File.separator+"org"+File.separator;
	static InputStreamReader isr = new InputStreamReader(System.in);
	static BufferedReader br = new BufferedReader(isr);
	static E e;
	private static int[] beneficio;
	private static int[] pesos;
	static double factorB = 1000.0;
	static double factorP = 100.0;

	//Capacidad: El peso a alcanzar y no exceder en el caso de la mochila normal
	//y peso a alcanzar o superar en el caso de la mochila inversa
	private static int capacidad;
	//Determina si se trata de una mochila inversa
	private static boolean reverse = true;

	//Variables auxiliares
	private static int n;
	private static int[][] matriz;
	private static ArrayList<Integer> aux;//auxiliar
	private static ArrayList<Integer> s;//Solucion final

	public static void empresas(String archivo) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(archivo));
		ArrayList<String> empresas = new ArrayList<String>();
		while (sc.hasNextLine()) {
			String nombre = sc.nextLine().split(" ")[0].trim();
			if (nombre.startsWith("@")||nombre.startsWith("%") || nombre.isEmpty()) {
				continue;
			}
			empresas.add(nombre);
		}
		sc.close();
		System.out.println(empresas.toString());
	}

	//Metodo principal
	public static void main(String[] args) throws NumberFormatException, IOException {
		System.out.println("Introduce el tamaño de la mochila:");
		capacidad = Integer.parseInt(br.readLine());
		System.out.println("Empresas que pueden comprar:");
		System.out.println();
		empresas(dir+"datos.txt");
		System.out.println();
		System.out.println("Escribe la empresa que deseas que compre:");
		e = new E(br.readLine());
		System.out.println();
		inicializar();
		mochila();
//		imprimirMatriz();
		mostrarArrays();
		test();
		finalizar();
		beneficioTotal();
	}

	//Inicializa las variables.
	public static void inicializar() throws FileNotFoundException{
		beneficio = cargarArchivoDinamica.cargarBeneficio(e);
		pesos = cargarArchivoDinamica.cargarPeso(e);
		aux = new ArrayList<Integer>();
		s = new ArrayList<Integer>();
		//Si es una mochila inversa, se rellena la mochila de solucion con todos los elementos
		//y se calcula la nueva capacidad de la mochila inversa (Total de pesos - capacidad)
		capacidad *=factorP;
		if(reverse){
			int suma = 0;
			for (int i = 0; i < pesos.length; i++) {
				suma+=pesos[i];
				s.add(i);
			}
			capacidad = suma - capacidad;
		}
		//Se genera la matriz a rellenar
		matriz =  new int [pesos.length][capacidad+1];
		n = pesos.length;

	}

	public static void mochila() {
		//Inicializamos la tabla sin objetos. Esta parte no es necesaria si se trata de una matriz de
		//datos primitivos (int) ya que por defecto la matriz se inicializa a 0
//		for (int w = 0; w <= capacidad; w++) {
//			matriz[0][w]=0;
//		}

		for (int k = 1; k < n; k++) { //Analizamos el efecto de contar con cada elemento
			for (int w = 0; w <= pesos[k]-1 && w<=capacidad; w++) { //Para pesos de mochila inferiores al elemento a escoger
				matriz[k][w]=matriz[k-1][w];
			}
			for (int w = pesos[k]; w <= capacidad; w++) {//Comprobacion por si hay mejora
				matriz[k][w]=Math.max(matriz[k-1][w-pesos[k]]+beneficio[k], matriz[k-1][w]);
			}
		}
	}

	private static void imprimirMatriz(){
		//ver matriz  fila
		System.out.printf("%4s","");
		for(int i=0;i<matriz[0].length;i++){
			System.out.printf("%4s",i+"");
		}
		System.out.println();

		//ver matriz
		for(int i=0;i<matriz.length;i++){
			System.out.printf("%4s",i+"|");//elementos columnas
			for(int j=0;j<matriz[i].length;j++){
				System.out.printf("%4s", matriz[i][j]);

			}
			System.out.println();
		}
	}

	private static void mostrarArrays(){
		System.out.println("Tamaño de la mochila: " +capacidad/factorP);
		System.out.println("Número de objetos es: "+n);
		System.out.println("ArrayPesos:");
		for (int i = 0; i < pesos.length; i++) {
			System.out.print(pesos[i]/factorP +" ");
		}
		System.out.println("\nBeneficio: ");
		for (int i = 0; i < beneficio.length; i++) {
			System.out.print(beneficio[i]/factorB+" ");
		}
	}

	//Algoritmo para selecionar los objetos que van a ingresar a la mochila
	private static void test(){
		System.out.println("\nObjetos agregados a la solución:");
		test(n-1,capacidad);//Llamada al metodo recursivo
	}

	//Algoritmo recursivo selecionar los objetos que van a ingresar a la mochila
	private static void test(int j, int c) {
		if(j>0){
			if(c<pesos[j] ){
				test(j-1,c);
			}else{
				if( (matriz[j-1][c-pesos[j]]+beneficio[j]>matriz[j-1][c])){
					aux.add(j);
					test(j-1,c-pesos[j]);
				}else{
					test(j-1,c);
				}
			}
		}
	}

	private static void finalizar(){
		if(reverse){//Si es una mochila inversa, se extraen de la solucion los guardados en aux
			s.removeAll(aux);
		}else{//Si no es inversa, se establece aux como la solucion
			s=aux;
		}
		s.sort(null);//Ordenacion no necesaria: realizado para mostrar el resultado en orden
		//Se muestran los objetos seleccionados
		for (int k=0; k < s.size(); k++) {
			Integer i = s.get(k);
			System.out.println("Empresa nº: "+(k+1) +" ("+cargarArchivoDinamica.nombreEmpresa.get(i)+") "+" Con beneficio: "+beneficio[i]/factorB+" m$ y  peso: "+pesos[i]/factorP+" mg");
		}
	}

	//Muestra el benedicio y el peso total de los objetos escogidos
	private static void beneficioTotal() {
		int sumaB=0;
		int sumaP=0;
		for (int i = 0; i < s.size(); i++) {
			sumaB=sumaB+beneficio[s.get(i)];
			sumaP=sumaP+pesos[s.get(i)];
		}
		System.out.print("El coste de la compra es: ");
		System.out.println((sumaB/factorB)+"m$ con total de contaminantes "+(sumaP/factorP)+"mg");
	}


}







