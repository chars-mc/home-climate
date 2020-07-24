#include <DHT.h>

#define pinDHT A0             // Definir pin del sensor DHT11 humedad y temperatura
#define pinHumedadTierra A1   // Definir pin del sensor de humedad de tierra
#define pinLDR A2             // Definir pin de sensor de luz
#define pinPIR 22             // Definir pin del sensor de movimiento
#define pinZumbador 24
#define pinBombaAgua 53

// Leds de emergencia
#define ledEmergencia1 2
#define ledEmergencia2 3
#define ledEmergencia3 4

#define pinVentilador 5    // Pin del ventilador

// Leds casa
#define ledCasa 6
#define ledCasa2 7

// Leds jardin
#define ledJardin 8
#define ledJardin2 9
#define ledJardin3 10

DHT dht(pinDHT, DHT11); // Cosntructor del objeto DHT (pin, tipoSensor)

const long A = 1000;     //Resistencia en oscuridad en KΩ
const int B = 15;        //Resistencia a la luz (10 Lux) en KΩ
const int Rc = 10;       //Resistencia calibracion en KΩ

int humedad;
float temperatura;
float indiceCalor;
int humedadTierra;
int nivelLuz;
boolean presencia;

void setup() {
	dht.begin();         // Preparala libreria para comunicarse con el sensor

	// Input para la entrada de datos de los sensores
	pinMode(pinHumedadTierra, INPUT);
  	pinMode(pinPIR, INPUT);

	pinMode(ledEmergencia1, OUTPUT);	// Output para la salida
	pinMode(ledEmergencia2, OUTPUT);
  	pinMode(ledEmergencia3, OUTPUT);
  
  	pinMode(pinVentilador, OUTPUT);
  	pinMode(pinBombaAgua, OUTPUT);
  	pinMode(pinZumbador, OUTPUT);

  	pinMode(ledCasa, OUTPUT);
  	pinMode(ledCasa2, OUTPUT);

  	pinMode(ledJardin, OUTPUT);
  	pinMode(ledJardin2, OUTPUT);
  	pinMode(ledJardin3, OUTPUT);

	Serial.begin(9600);
}

void loop() {
	delay(5000);
	leerDHT();
	leerHumedadTierra();
	leerLuz();
  	detectarPresencia();
	
	imprimirDatos();
}

 void leerDHT() {
	 float humedadAuxiliar = dht.readHumidity();          // Leer humedad
	 float temperaturaAuxiliar = dht.readTemperature();   // Leer temperatura
	 
	 // Verificar si los datos son validos
	 if(!isnan(humedadAuxiliar)) humedad = humedadAuxiliar;       // %
	 if(!isnan(temperaturaAuxiliar)) temperatura = temperaturaAuxiliar;   // °C

	 if(temperatura > 28) digitalWrite(pinVentilador, HIGH);
	 else digitalWrite(pinVentilador, LOW);

	 indiceCalor = dht.computeHeatIndex(temperatura, humedad, false);  // Calcular el índice de calor
}

void leerHumedadTierra() {
	// lectura analoga y ajuste de los valores leidos a los porcentajes a utilizar
	humedadTierra = map(analogRead(pinHumedadTierra), 0, 1023, 100, 0);
 	if(humedadTierra <= 5) digitalWrite(pinBombaAgua, HIGH);
 	else digitalWrite(pinBombaAgua, LOW);
}

void leerLuz() {
	int v = analogRead(pinLDR);

	// nivelLuz = ((long) V * A * 10) / ((long) B * Rc * (1024 - v)); // Usar si LDR entre A0 y Vcc (como en el esquema anterior)
	nivelLuz = ((long)(1024 - v) * A *10) / ((long) B * Rc * v);      //usar si LDR entre GND y A0

	if(nivelLuz < 5) encenderLucesJardin();
   	else apagarLucesJardin();
}

void detectarPresencia() {
  presencia = digitalRead(pinPIR);
  if(presencia == HIGH) {
	  digitalWrite(ledCasa, HIGH);
	  digitalWrite(ledCasa2, HIGH);
	  } else {
		  digitalWrite(ledCasa, LOW);
		  digitalWrite(ledCasa2, LOW);
  }
}

void imprimirDatos() {
	Serial.print(humedad);        // %
	Serial.print('-');
	Serial.print(temperatura);    // °C
	Serial.print('-');
	Serial.print(indiceCalor);    // °C
	Serial.print('-');
	Serial.print(humedadTierra);  // %
	Serial.print('-');
	Serial.print(nivelLuz);
	Serial.println();
	// humedad, temperatura, indice de calor, humedad de tierra
}

void encenderLucesJardin() {
  digitalWrite(ledJardin, HIGH);
  digitalWrite(ledJardin2, HIGH);
  digitalWrite(ledJardin3, HIGH);
}

void apagarLucesJardin() {
  digitalWrite(ledJardin, LOW);
  digitalWrite(ledJardin2, LOW);
  digitalWrite(ledJardin3, LOW);
}
