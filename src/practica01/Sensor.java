package practica01;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.Map.Entry;

public class Sensor implements Comunes{

	private String nombreSensor;
	private double flujoSensor;
	private HashMap<String, Double> contaminanteSensor;
	private TreeSet<String> empresas;//Atributo no usado en el algoritmo, pero si usado para la Interfaz Grafica (mapa de situacion)


	public Sensor(String nombre) {
		this.nombreSensor = nombre;
		this.flujoSensor = 0.0;
		this.contaminanteSensor = new HashMap<String, Double>();
		this.empresas = new TreeSet<String>();
	}

	public String getNombre() {
		return this.nombreSensor;
	}

	public double getFlujo() {
		return this.flujoSensor;
	}

	public void setFlujo(double flujo){
		this.flujoSensor = flujo;
	}

	public HashMap<String, Double> getContaminante() {
		return contaminanteSensor;
	}

	public TreeSet<String> getEmpresas() {
		return empresas;
	}

	public String toString() {
		return "Sensor " + nombreSensor + " ("
				+ flujoSensor + "l/s), contaminantes=" + contaminanteSensor;
	}

	public void addContaminante(String nombre, double concentracion){
		this.contaminanteSensor.put(nombre, concentracion);
	}

	public void agregarFlujoYContaminantes(Empresa empresa) {
		for (Entry<String, Double> it : empresa.getContaminante().entrySet()) {
			if(!contaminanteSensor.containsKey(it.getKey())){
				contaminanteSensor.put(it.getKey(), 0.0);
			}
			double mgAntiguo = contaminanteSensor.get(it.getKey())*flujoSensor; //El que existia hasta el momento
			double mgNuevo = it.getValue()*empresa.getFlujo(); //El que me llega
			double mgActual = mgAntiguo+mgNuevo; //LA suma de los dos

			if(flujoSensor+empresa.getFlujo() == 0){
				contaminanteSensor.put(it.getKey(), 0.0); //No hay caudal
			}else{
				contaminanteSensor.put(it.getKey(), mgActual/(flujoSensor+empresa.getFlujo())); //Contaminantes Actualizado
			}

			this.empresas.add(empresa.getNombre()+" "+empresa.isDireccionVertido()); //Para UI
		}
		flujoSensor+=empresa.getFlujo(); //Caudal actualizado

	}

	public void agregarFlujoYContaminantesSensor(Sensor sensor) {
		for (Entry<String, Double> it : sensor.getContaminante().entrySet()) {
			if(!contaminanteSensor.containsKey(it.getKey())){
				contaminanteSensor.put(it.getKey(), 0.0);
			}
			double mgAntiguo = contaminanteSensor.get(it.getKey())*flujoSensor; //El que existia hasta el momento
			double mgNuevo = it.getValue()*sensor.getFlujo(); //El que me llega
			double mgActual = mgAntiguo+mgNuevo; //LA suma de los dos

			if(flujoSensor+sensor.flujoSensor == 0){
				contaminanteSensor.put(it.getKey(), 0.0); //No hay caudal
			}else{
				contaminanteSensor.put(it.getKey(), mgActual/(flujoSensor+sensor.flujoSensor)); //Contaminantes Actualizado
			}

		}
		flujoSensor+=sensor.getFlujo(); //Caudal actualizado


	}










}
