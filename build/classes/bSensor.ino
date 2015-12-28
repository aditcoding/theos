int RELAY1 = D0;
int RELAY2 = D1;
int RELAY3 = D2;
int RELAY4 = D3;

void setup() {
   //Initilize the relay control pins as output
   pinMode(RELAY1, OUTPUT);
   pinMode(RELAY2, OUTPUT);
   pinMode(RELAY3, OUTPUT);
   pinMode(RELAY4, OUTPUT);
   // Initialize all relays to an OFF state
   digitalWrite(RELAY1, LOW);
   digitalWrite(RELAY2, LOW);
   digitalWrite(RELAY3, LOW);
   digitalWrite(RELAY4, LOW);
   //On board LED
   pinMode(D7, OUTPUT);
   Spark.function("toggle", toggleLight);
}

// command format r1,HIGH
int toggleLight(String command){
  int relayState = 0;
  // parse the relay number
  int relayNumber = command.charAt(1) - '0';
  // do a sanity check
  if (relayNumber < 1 || relayNumber > 4){
      return -1;
  }
  // find out the state of the relay
  if (command.substring(3,7).equalsIgnoreCase("HIGH")) {
    relayState = 1;
    digitalWrite(D7, HIGH);
  }
  else {
    relayState = 0;
    digitalWrite(D7, LOW);
  }
  // write to the appropriate relay
  digitalWrite(relayNumber-1, relayState);
  return 1;
}
