package practica04;

import java.util.List;

public class Nodo {
	private List<Empresa> empresas;
//	private int distanciaPS = 0;

	public Nodo(List<Empresa> empresas) {
		super();
		this.empresas = empresas;
	}

//	public Nodo(int distancia){
//		distanciaPS = distancia;
//	}
	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	@Override
	public String toString() {
		return "Nodo [" + empresas + "]\n";
	}

}
