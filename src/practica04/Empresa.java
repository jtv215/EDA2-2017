package practica04;

import java.util.List;

public class Empresa {
	private String nombre;
	private String vierte;
	private List<String> contaminantes;

	public Empresa(String nombre, String vierte, List<String> contaminantes) {
		super();
		this.nombre = nombre;
		this.vierte = vierte;
		this.contaminantes = contaminantes;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getVierte() {
		return vierte;
	}

	public void setVierte(String vierte) {
		this.vierte = vierte;
	}

	public List<String> getContaminantes() {
		return contaminantes;
	}

	public void setContaminantes(List<String> contaminantes) {
		this.contaminantes = contaminantes;
	}

	@Override
	public String toString() {
//		return "Empresa [" + nombre + " " + vierte + " " + contaminantes + "]";
		return nombre;
	}

}
