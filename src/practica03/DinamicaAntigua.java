package practica03;

import java.util.ArrayList;

public class DinamicaAntigua {

	public static int []beneficio={1,6,18,22,28};
	public static int []APesos={1,2,5,6,7};
	public static int n=5;



	public static int capacidad=11;
	public static int matriz[][]=  new int [APesos.length+1][capacidad+1];
	static ArrayList<Integer> s = new ArrayList<Integer>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	for(int i=1;i<=APesos.length;i++){
		for(int w=1;w<=capacidad;w++){

			if(w>=APesos[i-1]){
				if(matriz[i-1][w]<matriz[i-1][w-APesos[i-1]]+beneficio[i-1]){
					matriz[i][w]=matriz[i-1][w-APesos[i-1]]+beneficio[i-1];
				}else{
					matriz[i][w]=matriz[i-1][w];
				}
			}else{
				matriz[i][w]=matriz[i-1][w];
			}

		}
	}



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


	System.out.println("Tamao de la mochila: " +capacidad);
	System.out.println("Nmero de objetos es: "+n);
	System.out.println("ArrayPesos:");
	for (int i = 0; i < APesos.length; i++) {
		System.out.print(APesos[i] +" ");
	}
	System.out.println("\nBeneficio: ");
	for (int i = 0; i < beneficio.length; i++) {
		System.out.print(beneficio[i]+" ");
	}
	System.out.println("\nObjetos agregados a la solucin:");
	test(n-1,capacidad);

	System.out.print("El beneficio de la mochila es: ");
	beneficioTotal(s);
	}

	private static void beneficioTotal(ArrayList<Integer> beneficio) {
		// TODO Auto-generated method stub
		int suma=0;
		for (int i = 0; i < beneficio.size(); i++) {
			suma=suma+beneficio.get(i);

		}
		System.out.println(suma);
	}

	//Algoritmo recursivo selecionar objetos que van a ingresar a la mochila
	private static void test(int n, int capacidad) {
		if(n>0){
			if(capacidad<APesos[n-1] ){
				test(n-1,capacidad);
			}else{
				if( (matriz[n][capacidad]>matriz[n-1][capacidad])){
					s.add(n);
					System.out.println("Objeto n: "+n +" Con beneficio: "+beneficio[n-1]+" y  peso: "+APesos[n-1]);
					test(n-1,capacidad-APesos[n-1]);
				}else{
					test(n-1,capacidad);
				}
			}
		}
	}


}
