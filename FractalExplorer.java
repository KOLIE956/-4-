import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
	private int size;
	private JImageDisplay imageDisplay;
	private FractalGenerator fractalGenerator;
	private Rectangle2D.Double rectRange;

	public FractalExplorer(int size) {
		this.size = size;
		this.fractalGenerator = new Mandelbrot();
		this.rectRange = new Rectangle2D.Double();
		fractalGenerator.getInitialRange(rectRange);
	}

	public void createAndShowGUI() {
		JFrame frame = new JFrame("Fractals generator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		imageDisplay = new JImageDisplay(size, size);
		imageDisplay.addMouseListener(new MouseListener());

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ButtonActionListener());

		JButton button = new JButton("Save");
		button.addActionListener(new ButtonActionListener());

		frame.add(imageDisplay, BorderLayout.CENTER);
		frame.add(button, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}

	private void drawFractal() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				double x = FractalGenerator.getCoord(rectRange.x, rectRange.x + rectRange.width, size, i);
				double y = FractalGenerator.getCoord(rectRange.y, rectRange.y + rectRange.width, size, j);

				int numIters = fractalGenerator.numIterations(x, y);
				if (numIters == -1) {
					imageDisplay.drawPixel(i, j, Color.BLACK.getRGB());
				} else {
					float hue = 0.7f + (float) numIters / 200f;
					int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
					imageDisplay.drawPixel(i, j, rgbColor);
				}
			}
		}

		imageDisplay.repaint();
	}

	public static void main(String[] args) {
		FractalExplorer explorer = new FractalExplorer(800);
		explorer.createAndShowGUI();
		explorer.drawFractal();
	}

	class ButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(e.getActionCommand());
			imageDisplay.clearImage();
			imageDisplay.repaint();
			drawFractal();
		}
	}

	class MouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			double x = FractalGenerator.getCoord(rectRange.x, rectRange.x + rectRange.width, size, e.getX());
			double y = FractalGenerator.getCoord(rectRange.y, rectRange.y + rectRange.width, size, e.getY());
			fractalGenerator.recenterAndZoomRange(rectRange, x, y, 0.5);
			drawFractal();
		}
	}
}
