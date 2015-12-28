double currentReading = 0;

void setup() {
    pinMode(A0, INPUT);
    Spark.variable("pressure", &currentReading, DOUBLE);
}

void loop() {
    currentReading = analogRead(A0);
    if(currentReading > 100){
      Spark.publish("pressureChanged", String(currentReading));
    }
}
