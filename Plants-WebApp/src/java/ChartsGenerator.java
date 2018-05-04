import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class ChartsGenerator implements Serializable {
    //@ManagedProperty(value = "#{chart}")
    //Chart chart;
    SQLiteManager dbManager;
    Map<String, Chart> charts;
    String day;
    PointsData soilTemperature;
    PointsData soilMoisture;
    PointsData airTemperature;
    PointsData humidity;
    PointsData heatIndex;
    PointsData lightStrength;

    public ChartsGenerator() {
    }
    
    @PostConstruct
    public void init() {
        charts = new HashMap<>();
        dbManager = new SQLiteManager();
        day = "2018-04-13";
    }
    
    
    private String getJSTime(String time) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("yyyy, MM, dd, HH, mm, ss");
        String outputStr = "";
        try {
            Date date = inputFormat.parse(time);
            outputStr = outputFormat.format(date);
            
        } catch (ParseException ex) {
            Logger.getLogger(ChartsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "new Date(" + outputStr + ")";
    }
    
    private void initPointsData() {
        soilTemperature = new PointsData("line", "Soil Temperature", "##,##°C");
        soilMoisture = new PointsData("line", "Soil Moisture", "##,##%");
        airTemperature = new PointsData("line", "Air Temperature", "##,##°C");
        humidity = new PointsData("line", "Humidity", "###%");
        heatIndex = new PointsData("line", "Heat Index", "##,##°C");
        lightStrength = new PointsData("line", "Light Strength", "####%");
    }
    
    private void getPointsDataFromDB() {
        initPointsData();
        
        dbManager.connect();
        
        ResultSet result = dbManager.select
        ("SELECT * FROM data WHERE DATE(time) = DATE('" + day + "');");
        try {
            while(result.next()) {
                String XValue = getJSTime(result.getString("time"));
                
                soilTemperature.addPoint(XValue, result.getFloat("soilTemperature"));
                soilMoisture.addPoint(XValue, result.getFloat("soilMoisture"));
                airTemperature.addPoint(XValue, result.getFloat("airTemperature"));
                humidity.addPoint(XValue, result.getFloat("humidity"));
                heatIndex.addPoint(XValue, result.getFloat("heatIndex"));
                lightStrength.addPoint(XValue, (float) result.getInt("lightStrength"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChartsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dbManager.close();
    }
    
    private Chart getTemperatureChart() {
        Chart chart = new Chart();
        chart.setTitle("Pomiar temperatury na dzień " + day);
        chart.setAxisXValueFormat("HH:mm DD-MM-YYYY");
        chart.setAxisYTitle("Temperatura");
        chart.setAxisYSuffix("°C");
        chart.addPointsData(airTemperature);
        chart.addPointsData(soilTemperature);
        chart.addPointsData(heatIndex);
        chart.generateDataScript();
        return chart;
    }

    //public void setChart(Chart chart) {
    //    this.chart = chart;
    //}
    
    public void request() {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + ": ==== request method... ====");
        
        getPointsDataFromDB();
        charts.put("temperature", getTemperatureChart());
    }

    public Map<String, Chart> getCharts() {
        return charts;
    }
    
    
}
