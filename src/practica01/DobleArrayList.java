package practica01;
import java.util.ArrayList;

public class DobleArrayList<T> {

	private ArrayList<T> norte;
	private ArrayList<T> sur;

	public DobleArrayList() {
		this.norte = new ArrayList<T>();
		this.sur = new ArrayList<T>();
	}

	public ArrayList<T> getNorte() {
		return norte;
	}

	public ArrayList<T> getSur() {
		return sur;
	}

	public T getPrimeroNorte(){
		if(norte.size()==0){
			throw new IndexOutOfBoundsException("Lista vacia");
		}
		return norte.get(0);
	}

	public T getLastNorte(){
		if(norte.size()==0){
			throw new IndexOutOfBoundsException("Lista vacia");
		}
		return norte.get(norte.size()-1);
	}

	public T getPrimeroSur(){
		if(sur.size()==0){
			throw new IndexOutOfBoundsException("Lista vacia");
		}
		return sur.get(0);
	}

	public T getLastSur(){
		if(sur.size()==0){
			throw new IndexOutOfBoundsException("Lista vacia");
		}
		return sur.get(sur.size()-1);
	}

	public void addNorte(T t){
		norte.add(t);
	}

	public void addSur(T t){
		sur.add(t);
	}

	public String toString(){
		return "NORTE: "+norte.toString()+"\nSUR: "+sur.toString()+"\n";
	}



}
