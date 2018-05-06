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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class ChartsGenerator implements Serializable {
    SQLiteManager dbManager;
    Map<String, Chart> charts;
    String day;
    String errorMessage;
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
        errorMessage = "";
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
        soilTemperature = new PointsData("line", "Temperatura Gleby", "##,##°C");
        soilMoisture = new PointsData("line", "Wilgotność Gleby", "##,##%");
        airTemperature = new PointsData("line", "Temperatura Powietrza", "##,##°C");
        humidity = new PointsData("line", "Wilgotność Powietrza", "##,##%");
        heatIndex = new PointsData("line", "Temperatura Odczuwalna", "##,##°C");
        lightStrength = new PointsData("line", "Natężenie światła", "##,##%");
    }
    
    private void getPointsDataFromDB() throws Exception {
        initPointsData();
        
        dbManager.connect();
        
        ResultSet result = dbManager.select
        ("SELECT * FROM data WHERE DATE(time) = DATE('" + day + "');");
        try {
            if(!result.next()) {
                throw new Exception("Brak danych z podanego dnia.");
            }
            
            while(result.next()) {
                String XValue = getJSTime(result.getString("time"));
                
                soilTemperature.addPoint(XValue, result.getFloat("soilTemperature"));
                soilMoisture.addPoint(XValue, result.getFloat("soilMoisture") / 1023 * 100);
                airTemperature.addPoint(XValue, result.getFloat("airTemperature"));
                humidity.addPoint(XValue, result.getFloat("humidity"));
                heatIndex.addPoint(XValue, result.getFloat("heatIndex"));
                lightStrength.addPoint(XValue, ((float) result.getInt("lightStrength") / 1023 * 100));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChartsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            dbManager.close();
        }
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
    
    private Chart getOthersChart() {
        Chart chart = new Chart();
        chart.setTitle("Pomiary na dzień " + day);
        chart.setAxisXValueFormat("HH:mm DD-MM-YYYY");
        chart.setAxisYTitle("Stosunek procentowy");
        chart.setAxisYSuffix("%");
        chart.addPointsData(soilMoisture);
        chart.addPointsData(humidity);
        chart.addPointsData(lightStrength);
        chart.generateDataScript();
        return chart;
    }
    
    public void request() {
        String value = FacesContext.getCurrentInstance().
		getExternalContext().getRequestParameterMap().get("dateInput");
        try {
            getPointsDataFromDB();
            charts.put("temperature", getTemperatureChart());
            charts.put("others", getOthersChart());
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
            Logger.getLogger(ChartsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map<String, Chart> getCharts() {
        return charts;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    
}
