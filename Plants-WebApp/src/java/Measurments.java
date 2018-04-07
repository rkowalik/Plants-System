import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//
//@ManagedBean(name="measurments")
//@SessionScoped


public class Measurments implements Serializable {
    
//    private Measurments measurments = new Measurments();

    private String time;
    private float soilTemperature;
    private float soilMoisture;
    private float airTemperature;
    private float humidity;
    private float heatIndex;
    private int lightStrength;
 
    public Measurments() {
    }
    
    public void requestLastMeasurments() {
        try {
            SQLiteManager db = new SQLiteManager();
            db.connect();
            
            ResultSet result = db.select("SELECT * FROM data ORDER BY time DESC");
            if(result.next()) {
                time = result.getString("time");
                soilTemperature = result.getFloat("soilTemperature");
                soilMoisture = result.getFloat("soilMoisture");
                airTemperature = result.getFloat("airTemperature");
                humidity = result.getFloat("humidity");
                heatIndex = result.getFloat("heatIndex");
                lightStrength = result.getInt("lightStrength");
            }
            
            db.close();
        } catch (SQLException ex) {
            Logger.getLogger(Measurments.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void test() {
      time = String.valueOf(System.currentTimeMillis());
    }
    
    
    

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getSoilTemperature() {
        return soilTemperature;
    }

    public void setSoilTemperature(float soilTemperature) {
        this.soilTemperature = soilTemperature;
    }

    public float getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(float soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public float getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(float airTemperature) {
        this.airTemperature = airTemperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getHeatIndex() {
        return heatIndex;
    }

    public void setHeatIndex(float heatIndex) {
        this.heatIndex = heatIndex;
    }

    public int getLightStrength() {
        return lightStrength;
    }

    public void setLightStrength(int lightStrength) {
        this.lightStrength = lightStrength;
    }
    
    
    
    
}
