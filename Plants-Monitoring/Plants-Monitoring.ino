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


DHT dht(DHTPIN, DHTTYPE);

OneWire oneWire(TEMPERATURE_PIN);
DallasTemperature temperatureSensor(&oneWire);


void setup() {
  Serial.begin(9600);

  dht.begin();
  temperatureSensor.begin();
  pinMode(PHOTORESISTOR_PIN, INPUT);
  pinMode(MOISTURE_PIN, INPUT);
}

void loop() {
  float humidity = dht.readHumidity();
  float airTemperature = dht.readTemperature();
  
  // Compute heat index in Celsius (isFahreheit = false)
  float heatIndex = dht.computeHeatIndex(airTemperature, humidity, false);

  temperatureSensor.requestTemperatures();
  float soilTemperature = temperatureSensor.getTempCByIndex(0);

  //int soilMoisture = analogRead(MOISTURE_PIN);
  float soilMoisture = (float (1023 - analogRead(MOISTURE_PIN)) / 1023) * 100;
  //float soilMoisture = map(analogRead(MOISTURE_PIN), 1023, 0, 0, 100);
  
  int lightStrength = analogRead(PHOTORESISTOR_PIN);

  // Send data
//    Serial.print(soilTemperature);
//    Serial.print(":");
//    Serial.print(soilMoisture);
//    Serial.print(":");
//    Serial.print(airTemperature);
//    Serial.print(":");
//    Serial.print(humidity);
//    Serial.print(":");
//    Serial.print(heatIndex);
//    Serial.print(":");
//    Serial.println(lightStrength);


    
  // Print on Serial Monitor
  Serial.print("Humidity: ");
  Serial.print(humidity);
  Serial.print(" %\t");
  Serial.print("soil Moisture: ");
  Serial.print(soilMoisture);
  Serial.print(" %\t");
  Serial.print("Temperature: ");
  Serial.print(airTemperature);
  Serial.print(" *C\t");
  Serial.print("Heat index: ");
  Serial.print(heatIndex);
  Serial.println(" *C ");

  Serial.print("Soil Temperature: ");
  Serial.print(soilTemperature);
  Serial.println(" *C");

  Serial.print("Light Strength: ");
  Serial.println(lightStrength);
  Serial.println("============================");

  delay(6000);
}
