package practica04;

import java.util.ArrayList;
import java.util.List;

public class Permutation {

	//Permutacion de lista iterativa
	public static <T> List<List<T>> permutarIterativa(List<T> list) {
		List<List<T>> result = new ArrayList<List<T>>();

	    result.add(new ArrayList<T>());

	    for (int i = 0; i < list.size(); i++) {
	        List<List<T>> current = new ArrayList<List<T>>();

	        for (List<T> l : result) {
	            for (int j = 0; j < l.size()+1; j++) {
	                l.add(j, list.get(i));

	                List<T> temp = new ArrayList<T>(l);
	                current.add(temp);

	                l.remove(j);
	            }
	        }

	        result = new ArrayList<List<T>>(current);
	    }

	    return result;

	}
}