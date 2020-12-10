package practica01;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map.Entry;
import java.util.TreeSet;

public class MatrizUI extends Frame {

	private static final long serialVersionUID = 1L;

	MatrizUI(String titulo, int ancho, int alto, Empresa[][] matrix, TreeSet<String> culpables) {
		setTitle(titulo);
		setExtendedState(MAXIMIZED_BOTH);
		boolean[][] infractores = new boolean[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				infractores[i][j]=culpables.contains(matrix[i][j].getNombre());
			}
		}

		GridsCanvas xyz = new GridsCanvas(ancho, alto, matrix, infractores);
		add(xyz);

//		addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				setVisible(false);
//				dispose();
//			}
//		});

		pack();
	}

}

//class GridsCanvas extends Canvas {
//	private static final long serialVersionUID = 1L;
//
//	int ancho, alto;
//	int rows;
//	int cols;
//	Empresa[][] matrix;
//	boolean[][] infractores;
//
//	GridsCanvas(int w, int h, Empresa[][] matrix, boolean[][] infractores) {
//		setSize(ancho = w, alto = h);
//		rows = matrix.length;
//		cols = matrix[0].length;
//		this.matrix = matrix;
//		this.infractores = infractores;
//	}
//
//	public void paint(Graphics g) {
//		Graphics2D g2d=(Graphics2D) g;
//		int i;
//		ancho = getSize().width;
//		alto = getSize().height;
//		int rowHt = alto / (rows);
//		int rowWid = ancho / (cols);
//
//		//Dibuja las celdas
//
//		for (i = 0; i < rows; i++){
//			for (int j = 0; j < cols; j++) {
//				if(infractores[i][j]){
//					g2d.setColor(Color.RED);
//				}else{
//					g2d.setColor(Color.LIGHT_GRAY);
//				}
//				g2d.fillRect(j*rowWid, i * rowHt, rowWid, rowHt);
//			}
//		}
//
//		//Dibuja los nombres
//		g2d.setColor(Color.BLACK);
//		for (i = 0; i < rows; i++){
//			for (int j = 0; j < cols; j++) {
//				g2d.drawString(matrix[i][j].getNombre(), (j+1)*rowWid-rowWid/2-8, (i+1) * rowHt-rowHt/2-8);
//				g2d.drawString(matrix[i][j].getFlujo()+"lit/s", (j+1)*rowWid-rowWid/2-14, (i+1) * rowHt-rowHt/2+8);
//				g2d.drawString(matrix[i][j].isDireccionVertido(), (j+1)*rowWid-rowWid/2-8, (i+1) * rowHt-rowHt/2+20);
//				int p=36;
//				for (Entry<String, Double> it : matrix[i][j].getContaminante().entrySet()) {
//					g2d.drawString(it.getKey()+":"+it.getValue()+"mg/l", (j+1)*rowWid-rowWid/2-26, (i+1) * rowHt-rowHt/2+p);
//					p+=12;
//				}
//			}
//		}
//
//		//Dibuja las columnas
//		g2d.setColor(Color.BLACK);
//		for (i = 0; i < cols; i++){
//			g2d.drawLine(i * rowWid, 0, i * rowWid, alto);
//		}
//
//		//Dibuja las filas
//		g2d.setColor(Color.BLACK);
//		for (i = 0; i < rows; i++){
//			g2d.drawLine(0, i * rowHt, ancho, i * rowHt);
//		}
//
//		//Dibuja la avenida
//		g2d.setStroke(new BasicStroke(5.0f));
//		g2d.drawLine(0, rows/2 * rowHt, ancho, rows/2 * rowHt);
//	}
//}

