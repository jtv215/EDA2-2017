package practica03;

public class E {

	private String nombre;
	private float flujo;
	private float mg;
	private float ofertado;
	private float precio;

	public E(String nombre){
		this.nombre = nombre;
	}

	public E(String nombre, float flujo, float mg){
		this.nombre = nombre;
		this.flujo = flujo;
		this.mg = mg;
	}

	public E(String nombre, float mg, float ofertado, float precio) {
		this.nombre = nombre;
		this.mg = mg;
		this.ofertado = ofertado;
		this.precio = precio;
	}

	public int beneficio(E o){
		double precio = o.getPrecio();
		double tasa = precio*0.1;
		String letras1 = this.nombre.replaceAll("[^a-zA-Z]", "");
		String letras2 = o.nombre.replaceAll("[^a-zA-Z]", "");
		int x1 = Integer.parseInt(this.nombre.replaceAll("[a-zA-Z]", ""));
		int x2 = Integer.parseInt(o.nombre.replaceAll("[a-zA-Z]", ""));
		int y1 = cambiarLetraNumero(letras1);
		int y2 = cambiarLetraNumero(letras2);
		int tramos = Math.abs(x1-x2)+Math.abs(y1-y2);
		double precioTramos = precio * (tramos*2)/100.0;
		return (int)((precio+tasa+precioTramos)*1000); //En dolares
	}

	public int cambiarLetraNumero(String nombre) {
		//Cambiar letra a numero
		int signo = 1;
		if (nombre.endsWith("N")) {
			nombre = nombre.substring(0, nombre.length()-1);
		}else if(nombre.endsWith("S")){
			nombre = nombre.substring(0, nombre.length()-1);
			signo = -1;
		}
		int k = 0;
		for (int i = 0; i < nombre.length(); i++) {
			k += (nombre.charAt(i)-64)*Math.pow(26, nombre.length()-1-i);
		}
		return k-1;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public float getFlujo() {
		return flujo;
	}

	public void setFlujo(float flujo) {
		this.flujo = flujo;
	}

	public float getMg() {
		return mg;
	}

	public void setMg(float mg) {
		this.mg = mg;
	}

	public float getOfertado() {
		return ofertado;
	}

	public void setOfertado(float ofertado) {
		this.ofertado = ofertado;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public String printE1() {
		return "E [nombre=" + nombre + ", flujo=" + flujo + ", mg=" + mg + "]";
	}

	public String printE2() {
		return "E [nombre=" + nombre + ", mg=" + mg + ", ofertado=" + ofertado + ", precio=" + precio + "]";
	}

}
