package practica01;
import java.util.HashMap;

public class Empresa implements Comunes{

	private String nombreEmpresa;
	private String direccionVertido;
	private double flujoEmpresa;
	private HashMap<String, Double> contaminanteEmpresa;

	public Empresa(String nombreEmpresa, String direccionVertido, double flujoEmpresa,
			HashMap<String, Double> contaminanteEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
		this.direccionVertido = direccionVertido;
		this.flujoEmpresa = flujoEmpresa;
		this.contaminanteEmpresa = contaminanteEmpresa;
	}

	public String getNombre() {
		return nombreEmpresa;
	}

	public String isDireccionVertido() {
		return direccionVertido;
	}

	public double getFlujo() {
		return flujoEmpresa;
	}

	public HashMap<String, Double> getContaminante() {
		return contaminanteEmpresa;
	}

	@Override
	public String toString() {
		return "Empresa [nombreEmpresa=" + nombreEmpresa + ", direccionVertido=" + direccionVertido + ", flujoEmpresa="
				+ flujoEmpresa + ", contaminanteEmpresa=" + contaminanteEmpresa + "]";
	}

}
