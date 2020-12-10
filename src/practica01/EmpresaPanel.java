package practica01;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class EmpresaPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public EmpresaPanel(Empresa empresa) {
		setLayout(new GridLayout(4, 1));

		JLabel jl = new JLabel(empresa.getNombre());
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel jl2 = new JLabel(empresa.isDireccionVertido().equals("<--") ? "\u21DA     " : "     \u21DB");
		jl2.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel jl3 = new JLabel(empresa.getFlujo() + "l/s");
		jl3.setHorizontalAlignment(SwingConstants.CENTER);
		JTextArea jl4 = new JTextArea(empresa.getContaminante().toString());
		jl4.setLineWrap(true);
		jl4.setWrapStyleWord(true);
		JScrollPane jsp = new JScrollPane(jl4);
		add(jl);
		add(jl2);
		add(jl3);
		add(jsp);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(100, 100);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.dispose();
	}
}
