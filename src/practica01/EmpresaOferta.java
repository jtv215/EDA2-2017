package practica01;

public class EmpresaOferta {

	private String nombre;
	private double cantidadContrata;
	private double porcentajeOfrecido;
	private int precio;

	public EmpresaOferta(String nombre, double cantidadContrata, double porcentajeOfrecido, int precio) {
		super();
		this.nombre = nombre;
		this.cantidadContrata = cantidadContrata;
		this.porcentajeOfrecido = porcentajeOfrecido;
		this.precio = precio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getCantidadContrata() {
		return cantidadContrata;
	}

	public void setCantidadContrata(double cantidadContrata) {
		this.cantidadContrata = cantidadContrata;
	}

	public double getPorcentajeOfrecido() {
		return porcentajeOfrecido;
	}

	public void setPorcentajeOfrecido(double porcentajeOfrecido) {
		this.porcentajeOfrecido = porcentajeOfrecido;
	}

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}

	@Override
	public String toString() {
		return nombre +"\t"+cantidadContrata +"\t"+porcentajeOfrecido +"\t"+precio;
	}



}
