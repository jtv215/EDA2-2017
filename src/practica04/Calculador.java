package practica04;

public class Calculador {

	// Tiempo de analisis de un contaminante (minutos)
	private static int PRT = 30;
	// Tiempo de desplazamiento de un tramo (minutos)
	private static int CBT = 5;

	/**
	 * Obtiene el valor de PRT
	 *
	 * @return el valor de PRT
	 */
	public static int getPRT() {
		return PRT;
	}

	/**
	 * Establece el nuevo valor de PRT
	 *
	 * @param pRT
	 *            el nuevo PRT
	 */
	public static void setPRT(int pRT) {
		PRT = pRT;
	}

	/**
	 * Obtiene el valor de CBT
	 *
	 * @return el valor de CBT
	 */
	public static int getCBT() {
		return CBT;
	}

	/**
	 * Establece el nuevo valor de CBT
	 *
	 * @param cBT
	 *            el nuevo CBT
	 */
	public static void setCBT(int cBT) {
		CBT = cBT;
	}

	/**
	 * Obtiene exclusivamente el tiempo de análisis de todos los contaminantes
	 * de una empresa
	 *
	 * @param e
	 *            la empresa
	 * @return el tiempo de analisis de todos sus contaminantes
	 */
	public static int getPRTEmpresa(Empresa e) {
		return e.getContaminantes().size() * PRT;
	}

	/**
	 * Obtiene exclusivamente el tiempo de desplazamiento interno entre las
	 * empresas de un nodo
	 *
	 * @param nodo
	 *            el nodo de empresas
	 * @return el coste del desplazamiento interno
	 */
	public static int getCBTInterno(Nodo nodo) {
		int cbt = 0;
		for (int i = 1; i < nodo.getEmpresas().size(); i++) {
			cbt += getCBTDesdeHasta(nodo.getEmpresas().get(i - 1), nodo.getEmpresas().get(i));
		}
		return cbt;
	}

	/**
	 * Obtiene exclusivamente el tiempo de analisis interno de los contaminantes
	 * de las empresas que forman el nodo
	 *
	 * @param nodo
	 *            el nodo de empresas
	 * @return el coste de analisis interno de contaminantes
	 */
	public static int getPRTInterno(Nodo nodo) {
		int prt = 0;
		for (Empresa e : nodo.getEmpresas()) {
			prt += getPRTEmpresa(e);
		}
		return prt;
	}

	/**
	 * Obtiene el coste total de desplazamiento y analisis de contaminantes de
	 * un nodo
	 *
	 * @param nodo
	 *            el nodo de empresas
	 * @return el coste total de desplazamiento y analisis interno
	 */
	public static int getPRTyCBTInterno(Nodo nodo) {
		return getCBTInterno(nodo) + getPRTInterno(nodo);
	}

	/**
	 * Obtiene el coste de desplazamiento desde la ultima empresa de un nodo
	 * hasta la primera empresa de otro nodo
	 *
	 * @param n1
	 *            nodo de partida
	 * @param n2
	 *            nodo de llegada
	 * @return
	 */
	public static int getCBTNodoDesdeHasta(Nodo n1, Nodo n2) {
		Empresa ultimaN1 = n1.getEmpresas().get(n1.getEmpresas().size() - 1);
		Empresa primeraN2 = n2.getEmpresas().get(0);
		return getCBTDesdeHasta(ultimaN1, primeraN2);
	}

	/**
	 * Obtiene el coste desde una empresa hasta PS, pasando por A1
	 *
	 * @param e
	 *            la empresa de partida
	 * @return el coste desde una empresa hasta PS
	 */
	public static int getCBTHastaPS(Empresa e) {
		Empresa a = new Empresa("AN1", "<--", null);
		return getCBTDesdeHasta(e, a) + 5 * CBT;
	}

	/**
	 * Obtiene el coste desde la ultima empresa de un nodo hasta PS, pasando por
	 * A1
	 *
	 * @param n
	 *            el nodo de partida
	 * @return el coste desde la ultima empresa de un hasta PS
	 */
	public static int getCBTHastaPS(Nodo n) {
		return getCBTHastaPS(n.getEmpresas().get(n.getEmpresas().size() - 1));
	}

	/**
	 * Obtiene el coste desde PS hasta la primera empresa de un nodo , pasando
	 * por A1
	 *
	 * @param n
	 *            el nodo de llegada
	 * @return el coste desde PS hasta la primera empresa de un nodo
	 */
	public static int getCBTDesdePS(Nodo n) {
		return getCBTHastaPS(n.getEmpresas().get(0));
	}

	/**
	 * Obtiene el coste de desplazamiento desde una empresa hasta otra, teniendo
	 * en cuenta el lado de vertido de sus residuos
	 *
	 * @param desde
	 *            la empresa de partida
	 * @param hasta
	 *            la empresa de llegada
	 * @return el coste de desplazamiento de una empresa hasta otra
	 */
	public static int getCBTDesdeHasta(Empresa desde, Empresa hasta) {
		String letras1, letras2;
		int x1, x2, y1, y2;
		if(desde.getNombre().equals("PS")){
			x1=-4;
			y1=0;
		}else{
			letras1 = desde.getNombre().replaceAll("[^a-zA-Z]", "");
			x1 = Integer.parseInt(desde.getNombre().replaceAll("[a-zA-Z]", ""))
					+ (desde.getVierte().equals("<--") ? 0 : 1);
			y1 = cambiarLetraNumero(letras1);
		}
		if(hasta.getNombre().equals("PS")){
			x2=-4;
			y2=0;
		}else{
			letras2 = hasta.getNombre().replaceAll("[^a-zA-Z]", "");
			x2 = Integer.parseInt(hasta.getNombre().replaceAll("[a-zA-Z]", ""))
					+ (hasta.getVierte().equals("<--") ? 0 : 1);
			y2 = cambiarLetraNumero(letras2);
		}

		int tramos = Math.abs(x1 - x2) + Math.abs(y1 - y2);
		return tramos * CBT;
	}

	/**
	 * Obtiene el valor correspondiente a la letra (Avenida) a partir de su
	 * nombre completo
	 *
	 * @param nombre
	 *            el nombre de una empresa
	 * @return el valor correspondiente a la avenida en la que esta situado
	 *         (A=0, BN=1, CN=2, BS=-1, CS=-2.....)
	 */
	public static int cambiarLetraNumero(String nombre) {
		// Cambiar letra a numero
		int signo = 1;
		if (nombre.endsWith("N")) {
			nombre = nombre.substring(0, nombre.length() - 1);
		} else if (nombre.endsWith("S")) {
			nombre = nombre.substring(0, nombre.length() - 1);
			signo = -1;
		}
		int k = 0;
		for (int i = 0; i < nombre.length(); i++) {
			k += (nombre.charAt(i) - 64) * Math.pow(26, nombre.length() - 1 - i);
		}
		return (k - 1) * signo;
	}

	/* Algoritrno BackTracking */

//	public static void algoritmoBackTracking() {
//		int MaxDia = 480; // Variable global con los mnutos max al dia
//		s = new ArrayList<Nodo>(); // Variable global para la solucion.
//		total = (int) Double.POSITIVE_INFINITY; // Variable global para el coste final
//		pila = new Stack<Nodo>(); // Variable global pila con PS										// incluida
//		pila.add(PS);
//		copial = listaCopia(original);
//		algoritmoBackTracking(copial, 0, 0, 1);
//	}

//	private static void algoritmoBackTracking(ArrayList<Nodo> copia1, int total, int suma, int pscont) {
//		if (copia1.isEmpty()) {
//			if (Calculador.getPRTDesdePS(copia1.get(0))+suma<total) {
//				suma += Calculador.getPRTDesdePS(copia1.get(0));
//				copia1.add(PS);
//				total = suma;
//				for (Nodo n : pila) {
//					s.add(n);
//				}
//				pila.pop();
//			}
//		}else {
//			for (Nodo nodo : copia1) {
//				permutacion = Permutation.permutarRecursiva((List<Empresa>) nodo.getEmpresas());
//				for (List<Empresa> e : permutacion) {
//					int a = Calculador.getCBTDesdeHasta(pila.peek().getEmpresas().get(pila.peek().getEmpresas().size()-1), nodo.getEmpresas().get(0));
//					int b = Calculador.getPRTInterno(copia1.get(0));
//					int coste = a + b;
//				}
//
//			}
//
//		}
//
//	}
//
//
//	public static void main(String[] args) {
//		algoritmoBackTracking();
//	}

}