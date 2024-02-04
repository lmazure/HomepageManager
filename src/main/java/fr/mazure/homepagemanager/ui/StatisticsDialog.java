package fr.mazure.homepagemanager.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import fr.mazure.homepagemanager.utils.ExitHelper;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

/**
 * Dialog displaying statistics
 */
public class StatisticsDialog extends Dialog<Void> {

    private static final int s_numberOfBuckets = 25;
    private static final int s_bucketSize = 20;
    private static final String s_linksDirectoryFileName = "links"; // TODO should not appear in the UI code!

    /**
     * Constructor
     *
     * @param homepage directory where the XML files are located
     * @param files list of the paths of all files
     */
    public StatisticsDialog(final Path homepage,
                            final List<Path> files) {
        // Create a BarChart
        final BarChart<String, Number> barChart = new BarChart<>(new CategoryAxis(), new NumberAxis());

        // Adding data to the BarChart
        final Series<String, Number> series = new Series<>();
        series.setName("Number of articles");
        List<HistogramData> histogramData = generateHistogram(homepage, files);
        for (final HistogramData data: histogramData) {
            series.getData().add(new Data<>(data.name(), Integer.valueOf(data.count())));
        }

        barChart.getData().add(series);

        // Setting the title and other properties
        barChart.setTitle("Link statistics");
        barChart.setPrefSize(1000, 600);

        getDialogPane().setContent(barChart);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    }

    record HistogramData(String name, int count) {}

    static List<HistogramData> generateHistogram(final Path homepage,
                                                 final List<Path> files) {

        final int[] counts = new int[s_numberOfBuckets];

        for (final Path file: files) {
            if (!file.startsWith(homepage.resolve(s_linksDirectoryFileName))) {
                continue;
            }
            final int count = getNumberOfArticles(file);
            final int bucket = Math.min(count / s_bucketSize, s_numberOfBuckets - 1);
            counts[bucket]++;
        }

        final List<HistogramData> histogram = new ArrayList<>(s_numberOfBuckets);
        for (int i = 0; i < s_numberOfBuckets - 1; i++) {
            histogram.add(new HistogramData(Integer.toString(i * s_bucketSize + s_bucketSize / 2), counts[i]));
        }
        histogram.add(new HistogramData(Integer.toString((s_numberOfBuckets - 1) * s_bucketSize + s_bucketSize / 2) + "+", counts[s_numberOfBuckets - 1]));

        return histogram;
    }

    /**
     * compute the number of occurrences of the string "<ARTICLE>" in the file
     *
     * @param file file
     * @return number of occurrences
     */
    static int getNumberOfArticles(final Path file) {

        int count = 0;

        try (final FileReader fileReader = new FileReader(file.toFile());
             final BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("<ARTICLE")) {
                    count++;
                }
            }

        } catch (final IOException e) {
            ExitHelper.exit(e);
        }

        return count;
    }
}
