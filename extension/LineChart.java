import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LineChart extends JFrame {
    private String title, xAxisLabel, yAxisLabel;
    private List<List<Double>> datasets;
    private String[] seriesLabels;
    private int labelPadding;

    public LineChart(String title, String xAxisLabel, String yAxisLabel, List<List<Double>> datasets, String[] seriesLabels, int padding) {
        this.title = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.datasets = datasets;
        this.seriesLabels = seriesLabels;
        this.labelPadding = padding;
        initUI();
    }

    private void initUI() {
        add(new LineChartPanel(datasets, seriesLabels, xAxisLabel, yAxisLabel, labelPadding));
        setTitle(title);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static class LineChartPanel extends JPanel {
        private final List<List<Double>> datasets;
        private final String[] seriesLabels;
        private final String xAxisLabel, yAxisLabel;

        private final List<Double> xValues;

        public LineChartPanel(List<List<Double>> datasets, String[] seriesLabels, String xAxisLabel, String yAxisLabel, int padding) {
            this.datasets = datasets;
            this.seriesLabels = seriesLabels;
            this.xAxisLabel = xAxisLabel;
            this.yAxisLabel = yAxisLabel;
            this.xValues = new ArrayList<>();
            for (int i = 0; i < datasets.get(0).size(); i++) {
                xValues.add((double) i); // Dynamic X-axis values based on locked cells
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 50;
            int pointWidth = 6;
            Color[] seriesColors = {Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.YELLOW};

            double xScale = ((double) width - 2 * padding) / (xValues.size() - 1);
            double yScale = ((double) height - 2 * padding) / getMaxValue(datasets);

            // Draw all series
            for (int i = 0; i < datasets.size(); i++) {
                g2d.setColor(seriesColors[i % seriesColors.length]);
                List<Point> points = new ArrayList<>();

                for (int j = 0; j < xValues.size(); j++) {
                    double x = xValues.get(j);
                    double y = datasets.get(i).get(j);
                    int xPixel = (int) (x * xScale + padding);
                    int yPixel = (int) (height - padding - y * yScale);
                    points.add(new Point(xPixel, yPixel));
                }

                // Draw lines between points
                for (int j = 0; j < points.size() - 1; j++) {
                    g2d.drawLine(points.get(j).x, points.get(j).y, points.get(j + 1).x, points.get(j + 1).y);
                }

                // Draw data points
                for (Point p : points) {
                    g2d.fillOval(p.x - pointWidth / 2, p.y - pointWidth / 2, pointWidth, pointWidth);
                }
            }

            // Draw axes
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawLine(padding, padding, padding, height - padding); // Y-axis
            g2d.drawLine(padding, height - padding, width - padding, height - padding); // X-axis

            // Draw axis labels
            g2d.drawString(xAxisLabel, (width - g2d.getFontMetrics().stringWidth(xAxisLabel)) / 2, height - padding + 20);
            g2d.rotate(-Math.PI / 2, 0, 0);
            g2d.drawString(yAxisLabel, -(height / 2), padding / 2);
            g2d.rotate(Math.PI / 2, 0, 0);

            // Draw legend
            int legendX = padding + 20;
            int legendY = padding + 20;
            for (int i = 0; i < datasets.size(); i++) {
                g2d.setColor(seriesColors[i % seriesColors.length]);
                g2d.fillRect(legendX, legendY, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.drawString(seriesLabels[i], legendX + 15, legendY + 10);
                legendY += 20;
            }
        }

        private double getMaxValue(List<List<Double>> datasets) {
            double max = Double.MIN_VALUE;
            for (List<Double> series : datasets) {
                for (Double val : series) {
                    if (val > max) max = val;
                }
            }
            return max == 0 ? 1 : max; // Avoid division by zero
        }
    }
}