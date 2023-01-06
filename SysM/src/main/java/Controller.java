import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.FormatUtil;

import java.net.URL;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class Controller implements Initializable {

    @FXML
    protected AreaChart<?,?> memUsage;

    private SystemInfo systemInfo;
    private HardwareAbstractionLayer hal;
    private GlobalMemory globalMemory;
    private long usedMemory;
    //private ObservableList<XYChart.Data<String,String>> observableList = FXCollections.observableArrayList();

    private XYChart.Series memSeries = new XYChart.Series();
    int counter = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        systemInfo = new SystemInfo();
        hal = systemInfo.getHardware();
        globalMemory = hal.getMemory();
        usedMemory = globalMemory.getTotal() - globalMemory.getAvailable();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            XYChart.Data data = new XYChart.Data("t-"+counter++*2,100-(100*( hal.getMemory().getAvailable() )/(hal.getMemory().getTotal())));
            memSeries.getData().add(data);
            if(memSeries.getData().size() > 40){
                memSeries.getData().remove(0);
            }
        }));
        timeline.delayProperty().setValue(Duration.seconds(0));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();




        //populateSampleToAreaChart(50);
        memUsage.getData().add(memSeries);

        System.out.println( globalMemory.getTotal() );
        System.out.println( humanReadableByteCountBin( globalMemory.getTotal() ) );
    }

    private void addMemorySampleToAreaChart(){

    }

    private void populateSampleToAreaChart(int initPercentage){
        for (int i = 0; i < 10; i++){
            XYChart.Data data = new XYChart.Data(""+(i-10*30),i);
            //observableList.add(data);
            memSeries.getData().add(data);
        }
        memSeries.setName("Em uso");
    }

    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.2f %cB", value / 1024.00, ci.current());
    }
}
