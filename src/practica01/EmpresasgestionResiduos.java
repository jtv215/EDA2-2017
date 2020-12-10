package practica01;

public class EmpresasgestionResiduos {
	private String nombre;
	private double flujo;
	private double contaminante;

	public EmpresasgestionResiduos(String nombre, double flujo, double contaminante) {
		super();
		this.nombre = nombre;
		this.flujo = flujo;
		this.contaminante = contaminante;
	}

	public EmpresasgestionResiduos(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getFlujo() {
		return flujo;
	}

	public void setFlujo(double flujo) {
		this.flujo = flujo;
	}

	public double getContaminante() {
		return contaminante;
	}

	public void setContaminante(double contaminante) {
		this.contaminante = contaminante;
	}

	@Override
	public String toString() {
		return nombre +"\t"+ flujo +"\t"+ contaminante;
	}

}
