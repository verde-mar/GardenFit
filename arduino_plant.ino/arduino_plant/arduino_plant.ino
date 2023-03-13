#include <SoftwareSerial.h> 
#define soilDry 750 

// Sensor pins
#define sensorPower 7
#define sensorPin A0

void setup() {
  pinMode(sensorPower, OUTPUT);
  
  //BT module serial port
  //Serial.begin(9600); 
  
  digitalWrite(sensorPower, HIGH);  
  Serial.begin(9600);
}

void loop() {
  int dry = 0;
  // Reads from the sensor if the plant is dry
  if(analogRead(sensorPin) > soilDry){
    dry = 1;
    Serial.print("DRYY");
   }
  // Sending it to the BT module
  //Serial1.println(dry);

  delay(5000);

}
