package practica01;

public class Embalse {

	private double capacidad;
	private double ocupado;

	public Embalse(double capacidad, double ocupado) {
		this.capacidad = capacidad;
		this.ocupado = ocupado>capacidad?capacidad:ocupado;
	}

	public void setCapacidad(double capacidad) {
		this.capacidad = capacidad;
	}

	public void setOcupado(double ocupado) {
		this.ocupado = ocupado;
	}

	public long calcularTiempoLlenarse(Sensor sensor){
		return (long) (((capacidad-ocupado)/sensor.getFlujo())*1000);
	}

	public long calcularTiempoLimite(Sensor sensor){
		return System.currentTimeMillis()+calcularTiempoLlenarse(sensor);
	}


}
