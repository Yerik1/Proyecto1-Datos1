int upPin = 10;
int downPin = 7;
int leftPin = 9;
int rightPin = 8;
int clickPin = 6;
int rClickPin=5;
int ledPin = 4;
int buzzer=3; 

void setup() {
  Serial.begin(9600);
  pinMode(upPin, INPUT_PULLUP);
  pinMode(downPin, INPUT_PULLUP);
  pinMode(leftPin, INPUT_PULLUP);
  pinMode(rightPin, INPUT_PULLUP);
  pinMode(clickPin, INPUT_PULLUP);
  pinMode(rClickPin, INPUT_PULLUP);
  pinMode(ledPin, OUTPUT);
  pinMode(buzzer,OUTPUT);
}

void loop() {
  while (Serial.available() > 0) {
    char serialChar = Serial.read();
    if (serialChar == 'B') {
      digitalWrite(ledPin, HIGH);
      delay(200);
    }
    if (serialChar=='N'){
      digitalWrite(ledPin, LOW);
      delay(200);      
    }
    if (serialChar=='S'){
      tone(buzzer,2500,150);
      delay(50);
      tone(buzzer,4000,150);
      delay(300);      
    }
    if (serialChar=='M'){
      tone(buzzer,500,1000);
      delay(200);      
    }    
    if(serialChar=='W'){
      tone(buzzer,1500,500);
      delay(100);
      tone(buzzer,1000,500);
      delay(100);
      tone(buzzer,1500,500);
      delay(100);
      tone(buzzer,1000,500);
      delay(100);
      tone(buzzer,2000,500);            
    }
        
  }

  if (!digitalRead(upPin)) {
    Serial.write('U');
    delay(200);
  }
  if (!digitalRead(downPin)) {
    Serial.write('D');
    delay(200);
  }
  if (!digitalRead(leftPin)) {
    Serial.write('L');
    delay(200);
  }
  if (!digitalRead(rightPin)) {
    Serial.write('R');
    delay(200);
  }
  if (!digitalRead(clickPin)) {
    Serial.write('C');
    delay(200);
  }
  if (!digitalRead(rClickPin)) {
    Serial.write('K');
    delay(200);
  }
}


