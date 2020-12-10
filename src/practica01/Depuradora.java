package practica01;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

public class Depuradora {

	private ArrayList<DobleArrayList<Sensor>> colector; //Estructura para el colector principal
	private HashMap<String, Double[]> niveles; //key=contaminante, value=[critico,desvio]
	private Embalse embalse;
	private TreeSet<String> culpables;
	private TreeMap<String, TreeSet<String>> culpablesGreddy;
	private boolean consola;
	private static long tiempoLimite=-1;

	public Depuradora() {
		this.colector = new ArrayList<DobleArrayList<Sensor>>();
		this.niveles = new HashMap<String, Double[]>();
		this.embalse = new Embalse(10000, 0);
		this.consola = true;
	}

	public boolean isDentroDeTiempo(){
		if(tiempoLimite<0) return true;
		if(System.currentTimeMillis()<tiempoLimite){
			return true;
		}else{
			tiempoLimite=-1;
			return false;
		}
	}

	public void setCapacidadEmbalse(double capacidad){
		embalse.setCapacidad(capacidad);
	}

	public void setOcupadoEmbalse(double ocupado){
		embalse.setOcupado(ocupado);
	}

	public ArrayList<DobleArrayList<Sensor>> getColector() {
		return colector;
	}

	public DobleArrayList<Sensor> getDA(int index){
		if(index<0 || index>=colector.size()){
			throw new IndexOutOfBoundsException("Indice fuera de rango: "+index);
		}
		return colector.get(index);
	}

	public DobleArrayList<Sensor> getFirst(){
		if(colector.size()<0){
			throw new IndexOutOfBoundsException("Colector vacio");
		}
		return getDA(0);
	}

	public Sensor getFirstNorte(int index){
		if(colector.size()<0){
			throw new IndexOutOfBoundsException("Colector vacio");
		}
		return getDA(index).getPrimeroNorte();
	}

	public Sensor getLastNorte(int index){
		if(colector.size()<0){
			throw new IndexOutOfBoundsException("Colector vacio");
		}
		return getDA(index).getLastNorte();
	}

	public Sensor getFirstSur(int index){
		if(colector.size()<0){
			throw new IndexOutOfBoundsException("Colector vacio");
		}
		return getDA(index).getPrimeroSur();
	}

	public Sensor getLastSur(int index){
		if(colector.size()<0){
			throw new IndexOutOfBoundsException("Colector vacio");
		}
		return getDA(index).getLastSur();
	}

	public TreeSet<String> getCulpables(){
		return this.culpables;
	}

	public TreeMap<String, TreeSet<String>> getCulpablesGreddy() {
		return culpablesGreddy;
	}

	//Metodo principal que verifica la contaminacion de la zona industrial completa
	//Si se superan los niveles, se procede a la busqueda mediante el algoritmo divide y venceras
	public void comprobarEstado(boolean consola, boolean graph, int algoritmo){
		boolean previoConsola = this.consola;
		this.consola=consola;
		culpables = new TreeSet<String>();
		long start = System.currentTimeMillis();
		int nivel = verificarNivel(getFirst().getPrimeroNorte());
		switch (nivel) {
		case 2: println("NIVEL CRITICO: PARADA DE TODAS LAS INDUSTRIAS");
			switch (algoritmo) {
			case 1: FuerzaBruta(culpables); break;
			case 2: DV(culpables); break;
			case 3: DVMejorado(culpables); break;
			case 4: GreedyA(); break;
			case 5: GreedyAMejorado(); break;
			case 6: GreedyB(); break;
			default: System.out.println("Algoritmo desconocido");break;
			}
			break;
		case 1: println("NIVEL ALERTA: DESVIO DE FLUJOS A BALSA DE CORRECCION");
			tiempoLimite = embalse.calcularTiempoLimite(getFirstNorte(0));
			switch (algoritmo) {
			case 1: FuerzaBruta(culpables); break;
			case 2: DV(culpables); break;
			case 3: DVMejorado(culpables); break;
			case 4: GreedyA(); break;
			case 5: GreedyAMejorado(); break;
			case 6: GreedyB(); break;
			default: System.out.println("Algoritmo desconocido");break;
			}
			break;
		default:println("NIVEL ESTABLE: FLUJOS A LA DEPURADORA");
			break;
		}
		long end = System.currentTimeMillis();
		println("Comprobacion en "+((end-start)/1000.0)+"segs");

		tiempoLimite=-1;
		this.consola = previoConsola;//Se deja la visualizacion por consola que estaba establecida inicialmente
		if(graph){
			new MatrizUI("EIDZ - Control de contaminacion", 300, 300, CargadorArchivos.matrizEmpresas, culpables).setVisible(true);
		}
	}

	private void FuerzaBruta(TreeSet<String> culpables){
		for (DobleArrayList<Sensor> DA : colector) {
			for (Sensor s : DA.getNorte()) {
				if(!isDentroDeTiempo()) println("BALSA LLENA: PARADA DE INDUSTRIAS RESTANTES");
				if(verificarNivel(s)==0){
					println("SENSOR CORRECTO "+s.getNombre());
				}else{
					println("INFRACCION EN SENSOR "+s.getNombre());
					culpables.addAll(s.getEmpresas());
				}
			}
			for (Sensor s : DA.getSur()) {
				if(!isDentroDeTiempo()) println("BALSA LLENA: PARADA DE INDUSTRIAS RESTANTES");
				if(verificarNivel(s)==0){
					println("SENSOR CORRECTO "+s.getNombre());
				}else{
					println("INFRACCION EN SENSOR "+s.getNombre());
					culpables.addAll(s.getEmpresas());
				}
			}
		}
	}

	private void DV(TreeSet<String> culpables){
		DV(0, colector.size()-1, colector, culpables);
	}

	@SuppressWarnings("unchecked")
	private <T> void DV(int inicio, int fin, ArrayList<T> colector, TreeSet<String> culpables) {
		if(!isDentroDeTiempo()) println("BALSA LLENA: PARADA DE INDUSTRIAS RESTANTES");
		//SI SUFICIENTEMENTE PEQUEÑO
		if(inicio >= fin){
			//Se ha reducido el problema de busqueda en los sensores de la AVENIDA PRINCIPAL
			//Ahora será necesario realizar la busqueda sobre los sensores de la calle
			if(colector.get(inicio) instanceof DobleArrayList){
				DobleArrayList<Sensor> DA = (DobleArrayList<Sensor>) colector.get(inicio);
				Sensor BNx = DA.getNorte().get(1);
				Sensor BSx = DA.getPrimeroSur();
				//Si el sensor BNx no está infringiendo, todo la calle norte desde BNx se puede reactivar
				if(verificarNivel(BNx)==0){
					println("CORRECTO DESDE "+BNx.getNombre()+" HASTA "+DA.getLastNorte().getNombre());
					//Si el sensor BSx no está infringiendo, todo la calle sur desde BSx se puede reactivar
					//Esto deja sólo un sospechoso de infraccion, siendo Ax la única opcion de sospecha
					if(verificarNivel(BSx)==0){
						println("CORRECTO DESDE "+BSx.getNombre()+" HASTA "+DA.getLastSur().getNombre());
						if(verificarNivel(DA.getPrimeroNorte())==0){
							println("CORRECTO EN "+DA.getPrimeroNorte().getNombre());
						}else{
							println("IDENTIFICADO POSIBLE INFRACTOR EN "+DA.getPrimeroNorte().getNombre());//Ax
							culpables.addAll(DA.getPrimeroNorte().getEmpresas());
						}
					//Si el sensor BSx si está infringiendo, no es posible descartar a Ax como sospechosa, por lo que
					//se identifica como posible sospechoso y además, se realiza la busqueda en la calle Sur
					}else{
						println("IDENTIFICADO POSIBLE INFRACTOR EN "+DA.getPrimeroNorte().getNombre());//Ax
						culpables.addAll(DA.getPrimeroNorte().getEmpresas());
						DV(0, DA.getSur().size()-1, DA.getSur(), culpables);
					}
				//Si el sensor BNx si está infringiendo, no se puede descartar a Ax como sospechosa, por lo que se identifica
				//como tal y además se debe realizar una busqueda en la calle Norte. Previo a la busqueda en el norte, se puede
				//comprobar si el sensor BSx estuviera infringiendo, en cuyo caso tambien sería necesario realizar una busqueda en
				//la calle Sur. En caso de no infringir, la calle sur podria ser reactivada.
				}else{
					println("IDENTIFICADO POSIBLE INFRACTOR EN "+DA.getPrimeroNorte().getNombre());//Ax
					culpables.addAll(DA.getPrimeroNorte().getEmpresas());
					if(verificarNivel(BSx)==0){//Si calle sur es correcta
						println("CORRECTO DESDE "+BSx.getNombre()+" HASTA "+DA.getLastSur().getNombre());
						DV(0, DA.getNorte().size()-1, DA.getNorte(), culpables);
					}else{//Si no, se procede a la busqueda en el norte y el sur
						DV(0, DA.getNorte().size()-1, DA.getNorte(), culpables);
						DV(0, DA.getSur().size()-1, DA.getSur(), culpables);
					}
				}

			//Si no, es que se está realizando la busqueda sobre la CALLE (norte o sur)
			}else{
				Sensor s = (Sensor) colector.get(inicio);
				if(verificarNivel(s)==0){
					println("SENSOR CORRECTO "+s.getNombre());
				}else{
					println("IDENTIFICADO POSIBLE INFRACTOR EN "+s.getNombre());
					culpables.addAll(s.getEmpresas());
				}
			}
		//ALGORITMO RECURSIVO
		}else{
			//DESCOMPOSICION (Division del problema en dos subproblemas de igual tamaño)
			int mitad = (inicio+fin)/2;
			//Si es una instancia de DobleArrayList significa que se está realizando la busqueda sobre la AVENIDA PRINCIPAL
			if(colector.get(mitad) instanceof DobleArrayList){
				DobleArrayList<Sensor> DA = (DobleArrayList<Sensor>) colector.get(mitad);
				Sensor Amitad = DA.getPrimeroNorte();
				//Si el sensor Amitad no infringe los limites, se pueden reactivar los sensores desde Afin, siendo solo necesario
				//realizar una busqueda desde Ainicio hasta Amitad-1
				if(verificarNivel(Amitad)==0){
					println("CORRECTO DESDE "+Amitad.getNombre()+" HASTA "+((DobleArrayList<Sensor>) colector.get(fin)).getPrimeroNorte().getNombre());
					DV(inicio, mitad-1, colector, culpables);
				//Si no, es necesario realizar la busqueda en ambas mitades
				}else{
					DV(mitad+1, fin, colector, culpables);
					DV(inicio, mitad, colector, culpables);
				}
			//Si no, es que se está realizando la busqueda sobre la CALLE (norte o sur)
			}else{
				Sensor sMitad = (Sensor) colector.get(mitad);
				//Si el sensor de la mitad no infringe los limites, se pueden reactivar los sensores desde sMitad hasta sFin. Es necesaria la busqueda
				//en la primera mitad
				if(verificarNivel(sMitad)==0){
					println("CORRECTO DESDE "+sMitad.getNombre()+" HASTA "+((Sensor) colector.get(fin)).getNombre());
					DV(inicio, mitad-1, colector, culpables);
				//En caso de que supere los limites, será necesario realizar una busqueda en las dos mitades
				}else{
					DV(mitad+1, fin, colector, culpables);
					DV(inicio, mitad, colector, culpables);
				}
			}
		}

	}

	private void DVMejorado(TreeSet<String> culpables){
//		DVMejorado(0, colector.size()-1, colector, culpables, crearDescuento());
		DVMejorado(0, colector.size()-1, colector, culpables);
	}

//	@SuppressWarnings("unchecked")
//	private <T> Sensor DVMejorado(int inicio, int fin, ArrayList<T> colector, TreeSet<String> culpables, Sensor referencia) {
//		if(!isDentroDeTiempo()) println("BALSA LLENA: PARADA DE INDUSTRIAS RESTANTES");
//		//SI SUFICIENTEMENTE PEQUEÑO
//		if(inicio >= fin){
//			//Se ha reducido el problema de busqueda en los sensores de la AVENIDA PRINCIPAL
//			//Ahora será necesario realizar la busqueda sobre los sensores de la calle
//			if(colector.get(inicio) instanceof DobleArrayList){
//				DobleArrayList<Sensor> DA = (DobleArrayList<Sensor>) colector.get(inicio);
//				Sensor REF = diferencia(DA.getPrimeroNorte(), referencia);
//				if(verificarNivel(REF)==0){
//					println("CORRECTO EN "+DA.getPrimeroNorte().getNombre());
//				}else{
//					Sensor BNx = DA.getNorte().get(1);
//					Sensor BSx = DA.getPrimeroSur();
//					//Si el sensor BNx no está infringiendo, todo la calle norte desde BNx se puede reactivar
//					if(verificarNivel(BNx)==0){
//						println("CORRECTO DESDE "+BNx.getNombre()+" HASTA "+DA.getLastNorte().getNombre());
//						//Si el sensor BSx no está infringiendo, todo la calle sur desde BSx se puede reactivar
//						//Esto deja sólo un sospechoso de infraccion, siendo Ax la única opcion de sospecha
//						if(verificarNivel(BSx)==0){
//							println("CORRECTO DESDE "+BSx.getNombre()+" HASTA "+DA.getLastSur().getNombre());
//							println("IDENTIFICADO POSIBLE INFRACTOR EN "+DA.getPrimeroNorte().getNombre());//Ax
//							culpables.addAll(DA.getPrimeroNorte().getEmpresas());
//						//Si el sensor BSx si está infringiendo, no es posible descartar a Ax como sospechosa, por lo que
//						//se identifica como posible sospechoso y además, se realiza la busqueda en la calle Sur
//						}else{
////							println("IDENTIFICADO POSIBLE INFRACTOR EN "+DA.getPrimeroNorte().getNombre());//Ax
////							culpables.addAll(DA.getPrimeroNorte().getEmpresas());
//							DVMejorado(0, DA.getSur().size()-1, DA.getSur(), culpables, crearDescuento());
//						}
//					//Si el sensor BNx si está infringiendo, no se puede descartar a Ax como sospechosa, por lo que se identifica
//					//como tal y además se debe realizar una busqueda en la calle Norte. Previo a la busqueda en el norte, se puede
//					//comprobar si el sensor BSx estuviera infringiendo, en cuyo caso tambien sería necesario realizar una busqueda en
//					//la calle Sur. En caso de no infringir, la calle sur podria ser reactivada.
//					}else{
////						println("IDENTIFICADO POSIBLE INFRACTOR EN "+DA.getPrimeroNorte().getNombre());//Ax
////						culpables.addAll(DA.getPrimeroNorte().getEmpresas());
//						if(verificarNivel(BSx)==0){//Si calle sur es correcta
//							println("CORRECTO DESDE "+BSx.getNombre()+" HASTA "+DA.getLastSur().getNombre());
//							DVMejorado(0, DA.getNorte().size()-1, DA.getNorte(), culpables, crearDescuento());
//						}else{//Si no, se procede a la busqueda en el norte y el sur
//							DVMejorado(0, DA.getSur().size()-1, DA.getSur(), culpables, crearDescuento());
//							DVMejorado(0, DA.getNorte().size()-1, DA.getNorte(), culpables, crearDescuento());
//						}
//					}
//				}
//				return crearDescuento(DA.getPrimeroNorte());
//
//			//Si no, es que se está realizando la busqueda sobre la CALLE (norte o sur)
//			}else{
//				Sensor s = (Sensor) colector.get(inicio);
//				Sensor REF = diferencia(s, referencia);
//				if(verificarNivel(REF)==0){
//					println("SENSOR CORRECTO "+s.getNombre());
//				}else{
//					println("IDENTIFICADO POSIBLE INFRACTOR EN "+s.getNombre());
//					culpables.addAll(s.getEmpresas());
//				}
//				return crearDescuento(s);
//			}
//
//		//ALGORITMO RECURSIVO
//		}else{
//			//DESCOMPOSICION (Division del problema en dos subproblemas de igual tamaño)
//			int mitad = (inicio+fin)/2;
//			//Si es una instancia de DobleArrayList significa que se está realizando la busqueda sobre la AVENIDA PRINCIPAL
//			if(colector.get(mitad) instanceof DobleArrayList){
//				DobleArrayList<Sensor> DA = (DobleArrayList<Sensor>) colector.get(mitad);
//				Sensor Amitad = DA.getPrimeroNorte();
//				Sensor REF = diferencia(Amitad, referencia);
//				//Si el sensor Amitad no infringe los limites, se pueden reactivar los sensores desde Afin, siendo solo necesario
//				//realizar una busqueda desde Ainicio hasta Amitad-1
//				if(verificarNivel(REF)==0){
//					println("CORRECTO DESDE "+Amitad.getNombre()+" HASTA "+((DobleArrayList<Sensor>) colector.get(fin)).getPrimeroNorte().getNombre());
//					referencia = crearDescuento(Amitad);
//					referencia = DVMejorado(inicio, mitad-1, colector, culpables, referencia);
//				//Si no, es necesario realizar la busqueda en ambas mitades
//				}else{
//					referencia = DVMejorado(mitad+1, fin, colector, culpables, referencia);
//					referencia = DVMejorado(inicio, mitad, colector, culpables, referencia);
//				}
//			//Si no, es que se está realizando la busqueda sobre la CALLE (norte o sur)
//			}else{
//				Sensor sMitad = (Sensor) colector.get(mitad);
//				Sensor REF = diferencia(sMitad, referencia);
//				//Si el sensor de la mitad no infringe los limites, se pueden reactivar los sensores desde sMitad hasta sFin. Es necesaria la busqueda
//				//en la primera mitad
//				if(verificarNivel(REF)==0){
//					println("CORRECTO DESDE "+sMitad.getNombre()+" HASTA "+((Sensor) colector.get(fin)).getNombre());
//					referencia = crearDescuento(sMitad);
//					referencia = DVMejorado(inicio, mitad-1, colector, culpables, referencia);
//				//En caso de que supere los limites, será necesario realizar una busqueda en las dos mitades
//				}else{
//					referencia = DVMejorado(mitad+1, fin, colector, culpables, referencia);
//					referencia = DVMejorado(inicio, mitad, colector, culpables, referencia);
//				}
//			}
//			return referencia;
//		}
//
//	}

	@SuppressWarnings("unchecked")
	private <T> void DVMejorado(int inicio, int fin, ArrayList<T> colector, TreeSet<String> culpables) {
		if(!isDentroDeTiempo()) println("BALSA LLENA: PARADA DE INDUSTRIAS RESTANTES");
		//SI SUFICIENTEMENTE PEQUEÑO
		if(inicio >= fin){
			//Se ha reducido el problema de busqueda en los sensores de la AVENIDA PRINCIPAL
			//Ahora será necesario realizar la busqueda sobre los sensores de la calle
			if(colector.get(inicio) instanceof DobleArrayList){
				DobleArrayList<Sensor> DA = (DobleArrayList<Sensor>) colector.get(inicio);
				Sensor Axx, BNx, BSx, AxxPrevio = null;
				Axx = DA.getPrimeroNorte();
				BNx = DA.getNorte().get(1);
				BSx = DA.getPrimeroSur();
				if(inicio<colector.size()-1){
					AxxPrevio = ((DobleArrayList<Sensor>) colector.get(inicio+1)).getPrimeroNorte();
				}
				//En primer lugar, se determina si el sensor de la avenida (aislado) supera o no los
				//limites
				Sensor REF = diferencia(Axx,BNx,BSx,AxxPrevio);
				if(verificarNivel(REF)==0){
					println("CORRECTO EN "+Axx.getNombre());
				}else{
					println("INFRACTOR EN "+Axx.getNombre());
					culpables.addAll(Axx.getEmpresas());
				}
				//Despues se comprueba si se supera el límite en la calle norte.
				if(verificarNivel(BNx)==0){
					println("CORRECTO DESDE "+BNx.getNombre()+" HASTA "+DA.getLastNorte().getNombre());
				}else{
					DVMejorado(1, DA.getNorte().size()-1, DA.getNorte(), culpables);
				}
				//Y se comprueba del mismo modo para la calle sur
				if(verificarNivel(BSx)==0){
					println("CORRECTO DESDE "+BSx.getNombre()+" HASTA "+DA.getLastSur().getNombre());
				}else{
					DVMejorado(0, DA.getSur().size()-1, DA.getSur(), culpables);
				}
			//En caso de no tratarse de un DobleArrayList, estaremos trabajando sobre un arraylist de sensores
			//por lo que se trata del caso base mas interno. Ahora sólo habrá que determinar si el sensor aislado
			//supera o no los limites.
			}else{
				Sensor s = (Sensor) colector.get(inicio);
				Sensor sPrevio = null;
				if(inicio<colector.size()-1){
					sPrevio = (Sensor) colector.get(inicio+1);
				}
				Sensor REF = diferencia(s, sPrevio);
				if(verificarNivel(REF)==0){
					println("SENSOR CORRECTO "+s.getNombre());
				}else{
					println("INFRACTOR EN "+s.getNombre());
					culpables.addAll(s.getEmpresas());
				}

			}
		//ALGORITMO RECURSIVO
		}else{
			//DESCOMPOSICION (Division del problema en dos subproblemas de igual tamaño)
			int mitad = (inicio+fin)/2;
			Sensor Amitad, ini, mit, fi;
			//Si es una instancia de DobleArrayList significa que se está realizando la busqueda sobre la AVENIDA PRINCIPAL
			if(colector.get(mitad) instanceof DobleArrayList){
				DobleArrayList<Sensor> DA = (DobleArrayList<Sensor>) colector.get(mitad);
				Amitad = DA.getPrimeroNorte();
				ini = ((DobleArrayList<Sensor>) colector.get(inicio)).getPrimeroNorte();
				mit = ((DobleArrayList<Sensor>) colector.get(mitad+1)).getPrimeroNorte();
				fi = ((DobleArrayList<Sensor>) colector.get(fin)).getPrimeroNorte();
			//Si no, es que se está realizando la busqueda sobre la CALLE (norte o sur)
			}else{
				Amitad = (Sensor) colector.get(mitad);
				ini = (Sensor) colector.get(inicio);
				mit = (Sensor) colector.get(mitad+1);
				fi = (Sensor) colector.get(fin);
			}

			//Si no se superan los limites, se puede reactivar desde la mitad hasta fin. Solo será
			//necesario verificar desde inicio hasta mitad-1
			if(verificarNivel(Amitad)==0){
				println("CORRECTO DESDE "+Amitad.getNombre()+" HASTA "+fi.getNombre());
				DVMejorado(inicio, mitad-1, colector, culpables);
			//Si no, se realiza una llamada recursiva a la derecha (mitad+1, fin) y se determina
			//si es necesario realizar la llamada recursiva a la izquierda determinando la diferencia
			//que hay entre inicio y mitad+1
			}else{
				DVMejorado(mitad+1, fin, colector, culpables);
				Sensor REF = diferencia(ini, mit);
				if(verificarNivel(REF)==0){
					println("CORRECTO DESDE "+ini.getNombre()+" HASTA "+Amitad.getNombre());
				}else{
					DVMejorado(inicio, mitad, colector, culpables);
				}
			}
		}

	}

	private void GreedyA(){
		Sensor Axx, BNx = null, BSx = null, APrevio, REF;
		for (int j = colector.size()-1; j >= 0; j--) {
			Axx = colector.get(j).getPrimeroNorte();
			for (int i = colector.get(j).getNorte().size()-1; i >= 1; i--) {
				if(!isDentroDeTiempo()) println("BALSA LLENA: PARADA DE INDUSTRIAS RESTANTES");
				BNx = colector.get(j).getNorte().get(i);
				if(i < colector.get(j).getNorte().size()-1){
					REF = colector.get(j).getNorte().get(i+1);
				}else{
					REF = crearDescuento();
				}
				REF = diferencia(BNx, REF);
				if(verificarNivel(REF)==0){
					println("SENSOR CORRECTO "+BNx.getNombre());
				}else{
					println("IDENTIFICADO POSIBLE INFRACTOR EN "+BNx.getNombre());
					culpables.addAll(BNx.getEmpresas());
				}
			}
			for (int i = colector.get(j).getSur().size()-1; i >= 0; i--) {
				if(!isDentroDeTiempo()) println("BALSA LLENA: PARADA DE INDUSTRIAS RESTANTES");
				BSx = colector.get(j).getSur().get(i);
				if(i < colector.get(j).getSur().size()-1){
					REF = colector.get(j).getSur().get(i+1);
				}else{
					REF = crearDescuento();
				}
				REF = diferencia(BSx, REF);
				if(verificarNivel(REF)==0){
					println("SENSOR CORRECTO "+BSx.getNombre());
				}else{
					println("IDENTIFICADO POSIBLE INFRACTOR EN "+BSx.getNombre());
					culpables.addAll(BSx.getEmpresas());
				}
			}
			if(j<colector.size()-1){
				APrevio = colector.get(j+1).getPrimeroNorte();
			}else{
				APrevio = crearDescuento();
			}
			REF = diferencia(Axx, BNx);
			REF = diferencia(REF, BSx);
			REF = diferencia(REF, APrevio);
			if(verificarNivel(REF)==0){
				println("SENSOR CORRECTO "+Axx.getNombre());
			}else{
				println("IDENTIFICADO POSIBLE INFRACTOR EN "+Axx.getNombre());
				culpables.addAll(Axx.getEmpresas());
			}
		}
	}

	private void GreedyAMejorado(){
		Sensor Axx, BNx = null, BSx = null, APrevio, REF;
		for (int j = 0; j < colector.size(); j++) {
			if(!isDentroDeTiempo()) println("BALSA LLENA: PARADA DE INDUSTRIAS RESTANTES");
			Axx = colector.get(j).getPrimeroNorte();
			if(verificarNivel(Axx)==0){
				println("REACTIVAR DESDE "+Axx.getNombre());
				break;
			}
			BNx = colector.get(j).getNorte().get(1);
			BSx = colector.get(j).getSur().get(0);
			if(j<colector.size()-1){
				APrevio = colector.get(j+1).getPrimeroNorte();
			}else{
				APrevio = crearDescuento();
			}
			REF = diferencia(Axx, BNx);
			REF = diferencia(REF, BSx);
			REF = diferencia(REF, APrevio);
			if(verificarNivel(REF)==0){
				println("REACTIVAR SENSOR "+Axx.getNombre());
			}else{
				println("IDENTIFICADO POSIBLE INFRACTOR EN "+Axx.getNombre());
				culpables.addAll(Axx.getEmpresas());
			}
			for (int i = 1; i < colector.get(j).getNorte().size(); i++) {
				if(!isDentroDeTiempo()) println("BALSA LLENA: PARADA DE INDUSTRIAS RESTANTES");
				BNx = colector.get(j).getNorte().get(i);
				if(verificarNivel(BNx)==0){
					println("REACTIVAR DESDE "+BNx.getNombre());
					break;
				}
				if(i < colector.get(j).getNorte().size()-1){
					REF = colector.get(j).getNorte().get(i+1);
				}else{
					REF = crearDescuento();
				}
				REF = diferencia(BNx, REF);
				if(verificarNivel(REF)==0){
					println("REACTIVAR DESDE "+BNx.getNombre());
				}else{
					println("IDENTIFICADO POSIBLE INFRACTOR EN "+BNx.getNombre());
					culpables.addAll(BNx.getEmpresas());
				}
			}
			for (int i = 0; i < colector.get(j).getSur().size(); i++) {
				if(!isDentroDeTiempo()) println("BALSA LLENA: PARADA DE INDUSTRIAS RESTANTES");
				BSx = colector.get(j).getSur().get(i);
				if(verificarNivel(BSx)==0){
					println("REACTIVAR DESDE "+BSx.getNombre());
					break;
				}
				if(i < colector.get(j).getSur().size()-1){
					REF = colector.get(j).getSur().get(i+1);
				}else{
					REF = crearDescuento();
				}
				REF = diferencia(BSx, REF);
				if(verificarNivel(REF)==0){
					println("REACTIVAR DESDE "+BSx.getNombre());
				}else{
					println("IDENTIFICADO POSIBLE INFRACTOR EN "+BSx.getNombre());
					culpables.addAll(BSx.getEmpresas());
				}
			}
			if(j<colector.size()-1){
				APrevio = colector.get(j+1).getPrimeroNorte();
			}else{
				APrevio = crearDescuento();
			}
			REF = diferencia(Axx, BNx);
			REF = diferencia(REF, BSx);
			REF = diferencia(REF, APrevio);
			if(verificarNivel(REF)==0){
				println("SENSOR CORRECTO "+Axx.getNombre());
			}else{
				println("IDENTIFICADO POSIBLE INFRACTOR EN "+Axx.getNombre());
				culpables.addAll(Axx.getEmpresas());
			}
		}
	}



	//Este es el Greddy B que tocamos ayer 24/5
	//hicimos cambios para que salieran saltos de lineas
	//colocación de  % para tenerlo como comentario a la hora de cargar el archivo

	private void GreedyB(){
		culpablesGreddy = new TreeMap<String, TreeSet<String>>();
		culpablesGreddy.put("sR", new TreeSet<String>());
		culpablesGreddy.put("sN", new TreeSet<String>());
		culpablesGreddy.put("sA", new TreeSet<String>());
		culpablesGreddy.put("gR", new TreeSet<String>());
		culpablesGreddy.put("gN", new TreeSet<String>());
		culpablesGreddy.put("gA", new TreeSet<String>());
		Sensor Axx, BNx = null, BSx = null, APrevio, REF;
		StringBuilder listadoEmpresasSeguras=new StringBuilder();
		StringBuilder listadoEmpresasGris=new StringBuilder();
		for (int j = colector.size()-1; j >= 0; j--) {
			Axx = colector.get(j).getPrimeroNorte();
			for (int i = colector.get(j).getNorte().size()-1; i >= 1; i--) {
				BNx = colector.get(j).getNorte().get(i);
				if(i < colector.get(j).getNorte().size()-1){
					REF = colector.get(j).getNorte().get(i+1);
				}else{
					REF = crearDescuento();
				}
				REF = diferencia(BNx, REF);
				if(BNx.getEmpresas().size()>1){
					String[] colores = probabilidadContaminantes(REF, false);
					if(!colores[0].equals("0")){
						for (String empresa : BNx.getEmpresas()) {
							listadoEmpresasGris.append(empresa+" "+colores[0]+"\n"+colores[1]+"\n");
							culpablesGreddy.get("g"+colores[0]).add(empresa);
						}
						listadoEmpresasGris.append("#\n");
					}
				}else{
					String[] colores = probabilidadContaminantes(REF, true);
					if(!colores[0].equals("0")){
						for (String empresa : BNx.getEmpresas()) {
							listadoEmpresasSeguras.append("%"+empresa+" "+colores[0]+"\n%"+colores[1]);
							culpablesGreddy.get("s"+colores[0]).add(empresa);
						}
						listadoEmpresasSeguras.append("%#\n");
					}
				}
			}
			for (int i = colector.get(j).getSur().size()-1; i >= 0; i--) {
				BSx = colector.get(j).getSur().get(i);
				if(i < colector.get(j).getSur().size()-1){
					REF = colector.get(j).getSur().get(i+1);
				}else{
					REF = crearDescuento();
				}
				REF = diferencia(BSx, REF);
				if(BSx.getEmpresas().size()>1){
					String[] colores = probabilidadContaminantes(REF, false);
					if(!colores[0].equals("0")){
						for (String empresa : BSx.getEmpresas()) {
							listadoEmpresasGris.append(empresa+" "+colores[0]+"\n"+colores[1]+"\n");
							culpablesGreddy.get("g"+colores[0]).add(empresa);
						}
						listadoEmpresasGris.append("#\n");
					}
				}else{
					String[] colores = probabilidadContaminantes(REF, true);
					if(!colores[0].equals("0")){
						for (String empresa : BSx.getEmpresas()) {
							listadoEmpresasSeguras.append("%"+empresa+" "+colores[0]+"\n%"+colores[1]);
							culpablesGreddy.get("s"+colores[0]).add(empresa);
						}
						listadoEmpresasSeguras.append("%#\n");
					}
				}
			}
			if(j<colector.size()-1){
				APrevio = colector.get(j+1).getPrimeroNorte();
			}else{
				APrevio = crearDescuento();
			}
			REF = diferencia(Axx, BNx);
			REF = diferencia(REF, BSx);
			REF = diferencia(REF, APrevio);
			if(Axx.getEmpresas().size()>1){
				String[] colores = probabilidadContaminantes(REF, false);
				if(!colores[0].equals("0")){
					for (String empresa : Axx.getEmpresas()) {
						listadoEmpresasGris.append(empresa+" "+colores[0]+"\n"+colores[1]+"\n");
						culpablesGreddy.get("g"+colores[0]).add(empresa);
					}
					listadoEmpresasGris.append("#\n");
				}
			}else{
				String[] colores = probabilidadContaminantes(REF, true);
				if(!colores[0].equals("0")){
					for (String empresa : Axx.getEmpresas()) {
						listadoEmpresasSeguras.append("%"+empresa+" "+colores[0]+"\n%"+colores[1]);
						culpablesGreddy.get("s"+colores[0]).add(empresa);
					}
					listadoEmpresasGris.append("#\n");
				}
			}
		}
		System.out.println("%LISTADO EMPRESAS SEGURAS DE INFRACCION");
		System.out.println(listadoEmpresasSeguras.toString());
		System.out.println("%LISTADO EMPRESAS ZONA GRIS");
		System.out.println(listadoEmpresasGris.toString());
	}









	////Este GreedyB  "Este es el original"


//	private void GreedyB(){
//		culpablesGreddy = new TreeMap<String, TreeSet<String>>();
//		culpablesGreddy.put("sR", new TreeSet<String>());
//		culpablesGreddy.put("sN", new TreeSet<String>());
//		culpablesGreddy.put("sA", new TreeSet<String>());
//		culpablesGreddy.put("gR", new TreeSet<String>());
//		culpablesGreddy.put("gN", new TreeSet<String>());
//		culpablesGreddy.put("gA", new TreeSet<String>());
//		Sensor Axx, BNx = null, BSx = null, APrevio, REF;
//		StringBuilder listadoEmpresasSeguras=new StringBuilder();
//		StringBuilder listadoEmpresasGris=new StringBuilder();
//		for (int j = colector.size()-1; j >= 0; j--) {
//			Axx = colector.get(j).getPrimeroNorte();
//			for (int i = colector.get(j).getNorte().size()-1; i >= 1; i--) {
//				BNx = colector.get(j).getNorte().get(i);
//				if(i < colector.get(j).getNorte().size()-1){
//					REF = colector.get(j).getNorte().get(i+1);
//				}else{
//					REF = crearDescuento();
//				}
//				REF = diferencia(BNx, REF);
//				if(BNx.getEmpresas().size()>1){
//					String[] colores = probabilidadContaminantes(REF, false);
//					if(!colores[0].equals("0")){
//						for (String empresa : BNx.getEmpresas()) {
//							listadoEmpresasGris.append(empresa+":"+colores[0]+"\n"+colores[1]);
//
//							culpablesGreddy.get("g"+colores[0]).add(empresa);
//						}
//					}
//				}else{
//					String[] colores = probabilidadContaminantes(REF, true);
//					if(!colores[0].equals("0")){
//						for (String empresa : BNx.getEmpresas()) {
//							listadoEmpresasSeguras.append(empresa+":"+colores[0]+"\n"+colores[1]);
//							culpablesGreddy.get("s"+colores[0]).add(empresa);
//						}
//					}
//				}
//			}
//			for (int i = colector.get(j).getSur().size()-1; i >= 0; i--) {
//				BSx = colector.get(j).getSur().get(i);
//				if(i < colector.get(j).getSur().size()-1){
//					REF = colector.get(j).getSur().get(i+1);
//				}else{
//					REF = crearDescuento();
//				}
//				REF = diferencia(BSx, REF);
//				if(BSx.getEmpresas().size()>1){
//					String[] colores = probabilidadContaminantes(REF, false);
//					if(!colores[0].equals("0")){
//						for (String empresa : BSx.getEmpresas()) {
//							listadoEmpresasGris.append(empresa+":"+colores[0]+"\n"+colores[1]);
//							culpablesGreddy.get("g"+colores[0]).add(empresa);
//						}
//					}
//				}else{
//					String[] colores = probabilidadContaminantes(REF, true);
//					if(!colores[0].equals("0")){
//						for (String empresa : BSx.getEmpresas()) {
//							listadoEmpresasSeguras.append(empresa+":"+colores[0]+"\n"+colores[1]);
//							culpablesGreddy.get("s"+colores[0]).add(empresa);
//						}
//					}
//				}
//			}
//			if(j<colector.size()-1){
//				APrevio = colector.get(j+1).getPrimeroNorte();
//			}else{
//				APrevio = crearDescuento();
//			}
//			REF = diferencia(Axx, BNx);
//			REF = diferencia(REF, BSx);
//			REF = diferencia(REF, APrevio);
//			if(Axx.getEmpresas().size()>1){
//				String[] colores = probabilidadContaminantes(REF, false);
//				if(!colores[0].equals("0")){
//					for (String empresa : Axx.getEmpresas()) {
//						listadoEmpresasGris.append(empresa+":"+colores[0]+"\n"+colores[1]);
//						culpablesGreddy.get("g"+colores[0]).add(empresa);
//					}
//				}
//			}else{
//				String[] colores = probabilidadContaminantes(REF, true);
//				if(!colores[0].equals("0")){
//					for (String empresa : Axx.getEmpresas()) {
//						listadoEmpresasSeguras.append(empresa+":"+colores[0]+"\n"+colores[1]);
//						culpablesGreddy.get("s"+colores[0]).add(empresa);
//					}
//				}
//			}
//		}
//		System.out.println("LISTADO EMPRESAS SEGURAS DE INFRACCION");
//		System.out.println(listadoEmpresasSeguras.toString());
//		System.out.println("LISTADO EMPRESAS ZONA GRIS");
//		System.out.println(listadoEmpresasGris.toString());
//	}

	private String[] probabilidadContaminantes(Sensor rEF, boolean soloUna) {
		StringBuilder sb = new StringBuilder();
		int value = 0;
		String[] salida = new String[]{null,null};
		for (Entry<String, Double> it : rEF.getContaminante().entrySet()) {
			String[] s = colorear(soloUna?1.0:rEF.getFlujo(), it.getKey(), it.getValue());
			value=Integer.parseInt(s[0])>value?Integer.parseInt(s[0]):value;
			sb.append(s[1]);
		}
		salida[0]=value==3?"R":value==2?"N":value==1?"A":"0";
		salida[1]=sb.toString();
		return salida;
	}

	private String[] colorear(double flujo, String contaminante, double cantidad){
		double nivelMaximo = flujo*cantidad;
		switch (verificarNivel(contaminante, nivelMaximo)) {
		case 3: return new String[]{"3",contaminante+": Rojo\n"};
		case 2: return new String[]{"2",contaminante+": Naranja\n"};
		case 1: return new String[]{"1",contaminante+": Amarillo\n"};
		default: return new String[]{"0", ""};
		}
	}

	private Sensor crearDescuento(Sensor s){
		Sensor referencia = new Sensor("referencia");
		referencia.setFlujo(s.getFlujo());
		for (Entry<String, Double[]> it : niveles.entrySet()) {
			if(s.getContaminante().containsKey(it.getKey())){
				referencia.addContaminante(it.getKey(), s.getContaminante().get(it.getKey()));
			}else{
				referencia.addContaminante(it.getKey(), 0.0);
			}
		}
		return referencia;
	}

	private Sensor crearDescuento(){
		Sensor referencia = new Sensor("referencia");
		referencia.setFlujo(0.0);
		for (Entry<String, Double[]> it : niveles.entrySet()) {
			referencia.addContaminante(it.getKey(), 0.0);
		}
		return referencia;
	}

	private Sensor diferencia(Sensor...sensores){
		if(sensores.length<2){
			return sensores[0];
		}
		Sensor diferencia = diferencia(sensores[0], sensores[1]);
		for (int i = 2; i < sensores.length; i++) {
			if(sensores[i]!=null){
				diferencia = diferencia(diferencia, sensores[i]);
			}
		}
		return diferencia;
	}

	private Sensor diferencia(Sensor s, Sensor referencia){
		if(referencia == null){
			return s;
		}
		double f1, c1, f2, c2, m1, m2, dm, df, res;
		Sensor diferencia = new Sensor("diferencia");
		f1 = s.getFlujo();
		f2 = referencia.getFlujo();
		for (Entry<String, Double> q : s.getContaminante().entrySet()) {
			c1 = q.getValue();
			Double valor = referencia.getContaminante().get(q.getKey());
			c2 = valor==null?0:valor;
			m1 = f1*c1;
			m2 = f2*c2;
			dm = m1-m2;
			df = f1-f2;
			res = df==0?0:dm/df;
			diferencia.addContaminante(q.getKey(), res);
		}
		diferencia.setFlujo(f1-f2);
		return diferencia;
	}

	//Carga el sobre el HashMap el archivo de limites
	public void cargarNiveles(String archivo) throws FileNotFoundException {
		CargadorArchivos.cargarNiveles(niveles, archivo);
	}

	//Comprueba si algun algun contaminante supera alguno de los niveles
	// 0=OK , 1=desvio, 2=critico
	private int verificarNivel(Comunes s){
		for (Entry<String, Double> it : s.getContaminante().entrySet()) {
			Double[] nivel = niveles.get(it.getKey());
			if(it.getValue()>=nivel[0]){
				return 2;
			}else if(it.getValue()>=nivel[1]){
				return 1;
			}
		}
		return 0;
	}

	//Comprueba si algun algun contaminante supera alguno de los niveles
		// 0=OK , 1=25%desvio, 2=desvio, 3=critico
	private int verificarNivel(String contaminante, double level){
			Double[] nivel = niveles.get(contaminante);
			if(level>=nivel[0]){
				return 3;
			}else if(level>=nivel[1]){
				return 2;
			}else if(level>=nivel[1]*0.25){
				return 1;
			}
		return 0;
	}


	private void println(String mensaje){
		if(consola) System.out.println(mensaje);
	}



}
