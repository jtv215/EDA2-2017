package practica01;

import java.io.File;
import java.io.FileNotFoundException;

public class Rendimiento {

	private Depuradora depuradora;
	private static String dir = System.getProperty("user.dir")+File.separator+"src"+File.separator+"practica01"+File.separator;
	private int tamanoInicial;
	private int iteraciones;

	public Rendimiento() throws FileNotFoundException {
		this.depuradora = new Depuradora();
		this.tamanoInicial = 4; //Se comienzan las pruebas para un poligono de tamaño 4
		this.iteraciones = 10;
		depuradora.cargarNiveles(dir+"niveles.txt");
	}

	public Rendimiento(String archivoNiveles) throws FileNotFoundException {
		this.depuradora = new Depuradora();
		this.tamanoInicial = 4; //Se comienzan las pruebas para un poligono de tamaño 4
		this.iteraciones = 10;
		depuradora.cargarNiveles(archivoNiveles);
	}

	public void medir(String caso) throws FileNotFoundException{
		System.out.println("Ejecutando...");
		int t = tamanoInicial;
		long start, end;
		String salida = "N\tFB\tDV\tDVMej\tGrdyA\tGDAMej\tN_FB\tN_DV\tNDVMej\tN_GrdyA\tN_GDAMej\n";
		while (t<=256) {
			long[] tiempos = new long[5];
			int[] infracciones = new int[5];
			CargadorArchivos.cargarArchivo(dir+"casos"+File.separator+caso+"-"+t+"x"+t+".txt");
			CargadorArchivos.cargarSensores(depuradora.getColector());
			//Se realizan 10 iteraciones sobre cada uno de los algoritmos
			for (int i = 0; i < iteraciones; i++) {
				for (int j = 1; j <= 5; j++) {
					start = System.currentTimeMillis();
					depuradora.comprobarEstado(false,false, j);
					end = System.currentTimeMillis();
					tiempos[j-1] += (end-start); //Se suman todos los tiempos
					infracciones[j-1] += depuradora.getCulpables().size();
				}
			}
			//Se realiza la media y se agrega a la salida el resultado tanto de los tiempos como de los sensores infractores
			salida+=(t*t)+"\t"+(tiempos[0]/iteraciones)+"\t"+(tiempos[1]/iteraciones)+"\t"+(tiempos[2]/iteraciones)+"\t"+(tiempos[3]/iteraciones)+"\t"+(tiempos[4]/iteraciones)+"\t"+(infracciones[0]/iteraciones)+"\t"+(infracciones[1]/iteraciones)+"\t"+(infracciones[2]/iteraciones)+"\t"+(infracciones[3]/iteraciones)+"\t"+(infracciones[4]/iteraciones)+"\n";
			t*=2;//Duplica el tamaño por 2
		}
		System.out.println(salida);

	}





}
