
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrentMeasurments extends Measurments implements Serializable{

    public CurrentMeasurments() {
    }
    
    public void request() {
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
}