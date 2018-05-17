#include <DHT.h>
#include <DallasTemperature.h>

  // DHT (Temperature and Humidity Sensor)
#define DHTTYPE DHT11
#define DHTPIN 10

  // Waterproof Temperature Sensor
#define TEMPERATURE_PIN 9

  // Photoresistor
#define PHOTORESISTOR_PIN A0

  // Moisture Sensor
#define MOISTURE_PIN A1


class Sensors {
  float humidity;
  float airTemperature;
  float heatIndex;
  float soilTemperature;
  float soilMoisture;
  int lightStrength;

  int lowestAnalogSoilMoisture = 290;

  DHT dht;
  DallasTemperature soilTemperatureSensor;
  OneWire oneWire;

public:
  Sensors() : dht(DHTPIN, DHTTYPE), oneWire(TEMPERATURE_PIN), soilTemperatureSensor(&oneWire) {}

  void init() {
    dht.begin();
    soilTemperatureSensor.begin();
    pinMode(PHOTORESISTOR_PIN, INPUT);
    pinMode(MOISTURE_PIN, INPUT);

//    pinMode(13, OUTPUT);
  }

  void readMeasurments() {
    humidity = dht.readHumidity();
    
    airTemperature = dht.readTemperature();
    
    // Compute heat index in Celsius (isFahreheit = false)
    heatIndex = dht.computeHeatIndex(airTemperature, humidity, false);

    soilTemperatureSensor.requestTemperatures();
    soilTemperature = soilTemperatureSensor.getTempCByIndex(0);

    float soilMoistureAnalog = analogRead(MOISTURE_PIN);
    if(soilMoistureAnalog < lowestAnalogSoilMoisture) {
      soilMoistureAnalog = lowestAnalogSoilMoisture;
    }
    soilMoisture = map(analogRead(MOISTURE_PIN), lowestAnalogSoilMoisture, 1023, 100, 0);
    //soilMoisture = analogRead(MOISTURE_PIN);
  
    //lightStrength = analogRead(PHOTORESISTOR_PIN);
    lightStrength = map(analogRead(PHOTORESISTOR_PIN), 0, 700, 0, 100);
  }

  void sendFrame() {
    Serial.print(soilTemperature);
    Serial.print(":");
    Serial.print(soilMoisture);
    Serial.print(":");
    Serial.print(airTemperature);
    Serial.print(":");
    Serial.print(humidity);
    Serial.print(":");
    Serial.print(heatIndex);
    Serial.print(":");
    Serial.println(lightStrength);
  }
  
};


Sensors sensors;
char serialBuffer;


void setup() {
  Serial.begin(9600);
  sensors.init();
}

void loop() {

  if (serialBuffer == 'r') {
    sensors.readMeasurments();
    sensors.sendFrame();
    serialBuffer = '\0';
  }

  delay(100);

    
  // Print on Serial Monitor
  // Serial.print("Humidity: ");
  // Serial.print(humidity);
  // Serial.print(" %\t");
  // Serial.print("soil Moisture: ");
  // Serial.print(soilMoisture);
  // Serial.print(" %\t");
  // Serial.print("Temperature: ");
  // Serial.print(airTemperature);
  // Serial.print(" *C\t");
  // Serial.print("Heat index: ");
  // Serial.print(heatIndex);
  // Serial.println(" *C ");

  // Serial.print("Soil Temperature: ");
  // Serial.print(soilTemperature);
  // Serial.println(" *C");

  // Serial.print("Light Strength: ");
  // Serial.println(lightStrength);
  // Serial.println("============================");

  
}


void serialEvent() {
  while (Serial.available()) {
    serialBuffer = (char) Serial.read();
  }
}


