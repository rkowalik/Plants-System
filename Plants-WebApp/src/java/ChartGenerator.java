
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class ChartGenerator implements Serializable {
    @ManagedProperty(value = "#{chart}")
    Chart chart;

    public ChartGenerator() {
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }
    
    public void request() {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + ": ==== request method... ====");
        
        chart.setTitle("Pomiar temperatury");
        chart.setAxisXValueFormat("HH:mm DD-MM-YYYY");
        chart.setAxisYTitle("Temperatura");
        chart.setAxisYSuffix("°C");
        PointsData p = new PointsData("line", "airTemperature", "##,##");
        p.addPoint("new Date(2014, 12, 10)", new Float(22.20));
        p.addPoint("new Date(2014, 12, 11)", new Float(20.20));
        p.addPoint("new Date(2014, 12, 12)", new Float(27.20));
        chart.addPointsData(p);
        chart.generateDataScript();
        System.out.println(chart.getPointsData());
    }
}
