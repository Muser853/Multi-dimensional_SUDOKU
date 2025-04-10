import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.imageio.ImageIO;

public class LandscapeDisplay {
    JFrame win;
    protected Board scape;
    private int gridScale = 200; // Non-final to allow dynamic adjustment
    private LandscapePanel canvas; // Moved to class member variable

    public LandscapeDisplay(Board scape) {
        this.scape = scape;
        this.win = new JFrame("Sudoku Display");
        this.win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.canvas = new LandscapePanel(9 * this.gridScale, 11 * this.gridScale);
        this.win.add(this.canvas, BorderLayout.CENTER);
        this.win.pack();
        this.win.setVisible(true);
    }

    public void saveImage(String filename) {
        // get the file extension from the filename
        String ext = filename.substring(filename.lastIndexOf('.') + 1, filename.length());

        // create an image buffer to save this component
        Component tosave = this.win.getRootPane();
        BufferedImage image = new BufferedImage(tosave.getWidth(), tosave.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        // paint the component to the image buffer
        Graphics g = image.createGraphics();
        tosave.paint(g);
        g.dispose();

        // save the image
        try {
            ImageIO.write(image, ext, new File(filename));
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    private class LandscapePanel extends JPanel {
        public LandscapePanel(int width, int height) {
            super();
            this.setPreferredSize(new Dimension(width, height));
            this.setBackground(Color.white);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            scape.draw(g, gridScale);
        }
    }

    public void repaint() {
        win.repaint();
    }

    // Setter for gridScale to allow dynamic adjustment
    public void setGridScale(int gridScale) {
        this.gridScale = gridScale;
        // Update canvas size if needed
        canvas.setPreferredSize(new Dimension(scape.getCols() * gridScale, scape.getRows() * gridScale));
        win.pack(); // Re-pack the window to reflect changes
    }
}
