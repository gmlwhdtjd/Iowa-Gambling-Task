import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;

import java.awt.*;

public class MoneyChartPanel extends ChartPanel {
    private static final long serialVersionUID = 6081888609132549070L;

    public MoneyChartPanel(int screenDefaultSize, CategoryDataset dataSet) {
        super(ChartFactory.createBarChart(null, null, null, dataSet));

        JFreeChart chart = getChart();
        chart.removeLegend();

        CategoryPlot plot = chart.getCategoryPlot();

        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plot.setOrientation(PlotOrientation.HORIZONTAL);

        Paint[] colors = {new Color(84, 144, 104), new Color(218, 97, 86)};
        plot.setRenderer(new CustomRenderer(colors));

        plot.setOutlinePaint(Color.WHITE);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.BLACK);

        CategoryAxis DomainAxis = plot.getDomainAxis();
        DomainAxis.setTickLabelFont(MainFrame.getInstance().font.deriveFont(Font.BOLD, (int) (screenDefaultSize / 40)));
        DomainAxis.setAxisLinePaint(Color.BLACK);
        DomainAxis.setTickLabelPaint(Color.BLACK);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, 1599999.9);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setTickLabelFont(MainFrame.getInstance().font.deriveFont(Font.BOLD, (int) (screenDefaultSize / 50)));
        rangeAxis.setTickUnit(new NumberTickUnit(400000));
        rangeAxis.setAxisLinePaint(Color.BLACK);
        rangeAxis.setTickLabelPaint(Color.BLACK);

        Dimension chartSize = new Dimension((int) (screenDefaultSize / 0.8), (int) (screenDefaultSize / 4));
        setMaximumDrawHeight(chartSize.height);
        setMinimumDrawHeight(chartSize.height);
        setMaximumDrawWidth(chartSize.width);
        setMinimumDrawWidth(chartSize.width);
        setPreferredSize(chartSize);
        setMaximumSize(chartSize);
        setMinimumSize(chartSize);
    }

    class CustomRenderer extends BarRenderer {
        private static final long serialVersionUID = 3272347002961914415L;

        /**
         * The colors.
         */
        private Paint[] colors;

        /**
         * Creates a new renderer.
         *
         * @param colors the colors.
         */
        public CustomRenderer(final Paint[] colors) {
            this.setBarPainter(new StandardBarPainter());
            this.setShadowVisible(false);

            this.colors = colors;
        }

        /**
         * Returns the paint for an item.  Overrides the default behaviour inherited from
         * AbstractSeriesRenderer.
         *
         * @param row    the series.
         * @param column the category.
         * @return The item color.
         */
        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    }
}
