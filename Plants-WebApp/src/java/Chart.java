
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;


class PointsData {
    String type, name, YValueFormat;
    List<String> XPoints;
    List<Float> YPoints;
    
    public PointsData() {
        
    }
    
    @PostConstruct
    public void init() {
        XPoints = new LinkedList<>();
        YPoints = new LinkedList<>();
    }

    public PointsData(String type, String name, String YValueFormat) {
        super();
        XPoints = new LinkedList<>();
        YPoints = new LinkedList<>();
        this.type = type;
        this.name = name;
        this.YValueFormat = YValueFormat;
    }
    
    
    
    public void addPoint(String XValue, Float YValue) {
        XPoints.add(XValue);
        YPoints.add(YValue);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYValueFormat() {
        return YValueFormat;
    }

    public void setYValueFormat(String YValueFormat) {
        this.YValueFormat = YValueFormat;
    }

    public List<String> getXPoints() {
        return XPoints;
    }

    public List<Float> getYPoints() {
        return YPoints;
    }
    
    
}

@ManagedBean
@SessionScoped
public class Chart implements Serializable {
    protected String title;
    protected String axisXValueFormat;
    protected String axisYTitle;
    protected String axisYSuffix;
    protected String pointsDataStr;
    protected List<PointsData> pointsData;

    public Chart() {
    }
    
    public void addPointsData(PointsData data) {
        if(pointsData == null) {
            pointsData = new LinkedList<>();
        }
        pointsData.add(data);
    }
    
    public void generateDataScript() {
        StringBuilder sb = new StringBuilder();
        Iterator<PointsData> dataPointsIt = pointsData.iterator();
        while (dataPointsIt.hasNext()) {
            PointsData data = dataPointsIt.next();
            sb.append(
                    "{type: \"" + data.getType() + "\",\n" +
                    "axisYType: \"secondary\",\n" +
                    "name: \"" + data.getName() + "\",\n" +
                    "showInLegend: true,\n" +
                    "markerSize: 0,\n" +
                    "yValueFormatString: \"" + data.getYValueFormat() + "\",\n" +
                    "dataPoints: [\n");
            
            Iterator<String> XPointsIt = data.getXPoints().iterator();
            Iterator<Float> YPointsIt = data.getYPoints().iterator();
            while(XPointsIt.hasNext() && YPointsIt.hasNext()) {
                String x = XPointsIt.next();
                float y = YPointsIt.next();
                sb.append("{ x: ").append(x).append(", y: ").append(y).append(" }");
                if(YPointsIt.hasNext()) {
                    sb.append(",");
                }
                sb.append('\n');
            }
            sb.append("]}");
            
            if(dataPointsIt.hasNext()) {
                sb.append(",\n");
            }            
        }
        
        pointsDataStr = sb.toString();
    }
    
    public String getPointsData() {
        return pointsDataStr;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAxisXValueFormat() {
        return axisXValueFormat;
    }

    public void setAxisXValueFormat(String axisXValueFormat) {
        this.axisXValueFormat = axisXValueFormat;
    }

    public String getAxisYTitle() {
        return axisYTitle;
    }

    public void setAxisYTitle(String axisYTitle) {
        this.axisYTitle = axisYTitle;
    }

    public String getAxisYSuffix() {
        return axisYSuffix;
    }

    public void setAxisYSuffix(String axisYSuffix) {
        this.axisYSuffix = axisYSuffix;
    }
    
}