package practica04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Backtracking {

	private int MaxDia;
	private List<Empresa> S;
	private int TOTAL;
	private Stack<Nodo> pila;
	private Nodo PS;

	public Backtracking(){
		this(480, 30, 5);
	}

	public Backtracking(int maxDia, int PRT, int CBT) {
		MaxDia = maxDia;
		Calculador.setPRT(PRT);
		Calculador.setCBT(CBT);
		PS = new Nodo(new ArrayList<Empresa>(Arrays.asList(new Empresa("PS", "<--", null))));
	}

	public List<Empresa> getSolucion(){
		return this.S;
	}

	public int getTotal(){
		return this.TOTAL;
	}

	public void algoritmoTSP(List<Nodo> original){
		S = new ArrayList<Empresa>();
		TOTAL = Integer.MAX_VALUE;
		this.pila = new Stack<Nodo>();
		pila.add(PS);
		List<Nodo> copia = clonar(original);
		algoritmoTSP(copia, 0, 0, 1);
	}

	private void algoritmoTSP(List<Nodo> copia, int total, int suma, int dia) {
		if(copia.isEmpty()){//Caso base
			int coste = Calculador.getCBTHastaPS(pila.peek());
			if(coste+suma < TOTAL){
				pila.push(PS);
				TOTAL = suma+coste;
				S.clear();
				for (Nodo nodo : pila) {
					S.addAll(nodo.getEmpresas());
				}
				pila.pop();
			}
		}else{//Caso recursivo
			for (Nodo nodo : copia) {
				List<List<Empresa>> permutacion = Permutation.permutarIterativa(nodo.getEmpresas());
				List<Nodo> nodosFiltrados = removeCBT0_Repeated(permutacion);//Filtra los nodos con CBT interno > 1CBT y los "repetidos"
				for (Nodo gris : nodosFiltrados) {
					int coste = Calculador.getCBTNodoDesdeHasta(pila.peek(), gris) + Calculador.getPRTyCBTInterno(gris);
					if(coste + suma < TOTAL){
						List<Nodo> copia2 = clonar(copia);
						copia2.remove(nodo);
						if(coste + Calculador.getCBTHastaPS(gris) + total <= dia*MaxDia){
							pila.push(gris);
							algoritmoTSP(copia2, total+coste, suma+coste, dia);
							pila.pop();
						}else{
							int temp = dia*MaxDia + Calculador.getCBTDesdePS(gris)+Calculador.getPRTyCBTInterno(gris);
							int temp2 = suma + Calculador.getCBTHastaPS(pila.peek()) + Calculador.getCBTDesdePS(gris) + Calculador.getPRTyCBTInterno(gris);
							pila.push(PS);
							pila.push(gris);
							algoritmoTSP(copia2, temp, temp2, dia+1);
							pila.pop();
							pila.pop();
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<Nodo> clonar(List<Nodo> original){
		return (List<Nodo>) ((ArrayList<Nodo>)(original)).clone();
	}

	private  List<Nodo> removeCBT0_Repeated(List<List<Empresa>> lista){
		for (int i = 0; i < lista.size(); ) {
			Nodo n = new Nodo(lista.get(i));
			if(Calculador.getCBTInterno(n)>Calculador.getCBT()){//Comprueba si su coste Interno es mayor estricto a 1 desplazamiento
				lista.remove(i);
			}else{//Comprueba si están "repetidos"
				boolean removed = false;
				for (int j = i-1; j >= 0; j--) {
					if(Calculador.getCBTDesdeHasta(n.getEmpresas().get(0), lista.get(j).get(0))==0){
						lista.remove(i);
						removed = true;
						break;
					}
				}
				if(!removed){
					i++;
				}
			}
		}

		//A partir de las permutaciones filtradas, se genera la lista de nodos permutados filtrados
		List<Nodo> nodosFiltrados = new ArrayList<Nodo>();
		for (List<Empresa> list : lista) {
			nodosFiltrados.add(new Nodo(list));
		}
		return nodosFiltrados;
	}

}
