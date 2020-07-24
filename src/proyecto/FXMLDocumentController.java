package proyecto;

import clases.DB;
import clases.Sensor;
import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class FXMLDocumentController implements Initializable {

   @FXML
   private Button inicioBoton;
   @FXML
   private Button graficasBoton;
   @FXML
   private Button reportesBoton;
   @FXML
   private Button salirBoton;
   @FXML
   private VBox principal;
   @FXML
   private VBox sensores;
   @FXML
   private Label humedadLabel;
   @FXML
   private Label humedadValorBajo;
   @FXML
   private Label humedadValorAlto;
   @FXML
   private Label temperaturaLabel;
   @FXML
   private Label temperaturaValorBajo;
   @FXML
   private Label temperaturaValorAlto;
   @FXML
   private Label indiceCalorLabel;
   @FXML
   private Label indiceCalorValorBajo;
   @FXML
   private Label indiceCalorValorAlto;
   @FXML
   private Label humedadTierraLabel;
   @FXML
   private Label humedadTierraValorBajo;
   @FXML
   private Label humedadTierraValorAlto;
   @FXML
   private VBox graficas;
   @FXML
   private LineChart<String, Float> grafica;
   @FXML
   private NumberAxis numberY;
   @FXML
   private CategoryAxis categoryX;
   @FXML
   private Label graficaTitulo;
   @FXML
   private Button humedadGraficaBoton;
   @FXML
   private Button temperaturaGraficaBoton;
   @FXML
   private Button indiceCalorGraficaBoton;
   @FXML
   private Button humedadTierraGraficaBoton;
   @FXML
   private VBox reportes;
   @FXML
   private TableView<Sensor> tablaReporte;
   @FXML
   private TableColumn<Sensor, String> fecha;
   @FXML
   private TableColumn<Sensor, String> valorMinimo;
   @FXML
   private TableColumn<Sensor, String> promedio;
   @FXML
   private TableColumn<Sensor, String> valorMáximo;
   @FXML
   private Button humedadReporteBoton;
   @FXML
   private Button temperaturaReporteBoton;
   @FXML
   private Button indiceCalorReporteBoton;
   @FXML
   private Button humedadTierraReporteBoton;

   PanamaHitek_Arduino arduino = new PanamaHitek_Arduino();

   @Override
   public void initialize(URL url, ResourceBundle rb) {

      setLabelValuesMinMax();
      inicioBoton.setDisable(true);
      principal.getChildren().removeAll(graficas, reportes);
      humedadGraficaBoton.setDisable(true);
      humedadReporteBoton.setDisable(true);

      SerialPortEventListener listener = new SerialPortEventListener() {
         @Override
         public void serialEvent(SerialPortEvent event) {
            try {
               if (arduino.isMessageAvailable()) {
                  String valores = arduino.printMessage();

                  Platform.runLater(new Runnable() {
                     @Override
                     public void run() {
                        procesarMensaje(valores);
                     }
                  });
               }
            } catch (SerialPortException e) {
               System.out.println(e);
            } catch (ArduinoException e) {
               System.out.println(e);
            }
         }
      };

      // Conexion Arduino
      try {
         arduino.arduinoRX("COM3", 9600, listener);   // Reemplazar COM3 por el puerto COM del arduino
         System.out.println("conectado");
      } catch (ArduinoException e) {
         Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, e);
      } catch (SerialPortException ex) {
         Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
      } catch (Exception ex) {
         Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public void procesarMensaje(String datosSensores) {
      //humedad, temperatura, índice de calor, humedad de tierra, nivel de luz, nivel de humo o gas
      System.out.println(datosSensores);
      String[] datos = datosSensores.split("-");

      DB db = new DB();
      db.guardarDB(datos);
      db.cerrarDB();

      setLabelValues(datos);
      setLabelValuesMinMax();
   }

   // Actualizar labels
   public void setLabelValues(String[] datos) {
      humedadLabel.setText(datos[0] + " %");
      temperaturaLabel.setText(datos[1] + " °C");
      indiceCalorLabel.setText(datos[2] + " °C");
      humedadTierraLabel.setText(datos[3] + " %");
   }

   public void setLabelValuesMinMax() {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            DB db = new DB();
            String valoresMinMax = "";
            valoresMinMax += db.obetenerDatosMinimosMaximos("humedad");
            valoresMinMax += db.obetenerDatosMinimosMaximos("temperatura");
            valoresMinMax += db.obetenerDatosMinimosMaximos("indiceCalor");
            valoresMinMax += db.obetenerDatosMinimosMaximos("humedadTierra");

            String[] valores = valoresMinMax.split("-");
            humedadValorBajo.setText("🡇" + valores[0] + " %");
            humedadValorAlto.setText("🡅" + valores[1] + " %");
            temperaturaValorBajo.setText("🡇" + valores[2] + " °C");
            temperaturaValorAlto.setText("🡅" + valores[3] + " °C");
            indiceCalorValorBajo.setText("🡇" + valores[4] + " °C");
            indiceCalorValorAlto.setText("🡅" + valores[5] + " °C");
            humedadTierraValorBajo.setText("🡇" + valores[6] + " %");
            humedadTierraValorAlto.setText("🡅" + valores[7] + " %");

            db.cerrarDB();
         }
      });
   }

   // Generar graficas
   public void generarGrafica(String sensores) {
      graficaTitulo.setText("Gráfica: " + sensores);

      DB datosDB = new DB();
      String datos = datosDB.obtenerDatosGrafica(sensores);
      datosDB.cerrarDB();
      String[] datosGrafica = datos.split("/");

      XYChart.Series series = new XYChart.Series();
      series.getData().add(new XYChart.Data("Minimo", Float.parseFloat(datosGrafica[0])));
      series.getData().add(new XYChart.Data("Promedio", Float.parseFloat(datosGrafica[1])));
      series.getData().add(new XYChart.Data("Maximo", Float.parseFloat(datosGrafica[2])));

      grafica.getData().addAll(series);
   }

   // Generar reportes
   public void generarReportes(String sensor) {
      tablaReporte.getItems().clear();
      DB reporteDB = new DB();
      String datos = reporteDB.obtenerDatosReporte(sensor);
      reporteDB.cerrarDB();
      String[] datosReporte = datos.split("/");

      fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
      valorMinimo.setCellValueFactory(new PropertyValueFactory<>("valorMinimo"));
      promedio.setCellValueFactory(new PropertyValueFactory<>("valorMaximo"));
      valorMáximo.setCellValueFactory(new PropertyValueFactory<>("promedio"));

      for (int i = 0; i < datosReporte.length; i++) {
         Sensor sensorDatos = new Sensor(datosReporte[i], datosReporte[i + 1], datosReporte[i + 2], datosReporte[i + 3]);
         i += 3;

         tablaReporte.getItems().addAll(sensorDatos);
      }
   }

   // Funciones botones del menu
   @FXML
   private void inicio(ActionEvent event) {
      principal.getChildren().removeAll(graficas, reportes);
      principal.getChildren().add(sensores);
      inicioBoton.setDisable(true);
      graficasBoton.setDisable(false);
      reportesBoton.setDisable(false);
      grafica.getData().remove(0);
   }

   @FXML
   private void graficos(ActionEvent event) {
      principal.getChildren().removeAll(sensores, reportes);
      principal.getChildren().add(graficas);
      generarGrafica("humedad");

      graficasBoton.setDisable(true);
      inicioBoton.setDisable(false);
      reportesBoton.setDisable(false);
   }

   @FXML
   private void reportes(ActionEvent event) {
      principal.getChildren().removeAll(graficas, sensores);
      principal.getChildren().add(reportes);
      generarReportes("humedad");
      inicioBoton.setDisable(false);
      graficasBoton.setDisable(false);
      reportesBoton.setDisable(true);
      grafica.getData().remove(0);
   }

   // Funciones para los botones de las graficas
   @FXML
   private void graficaHumedad(ActionEvent event) {
      grafica.getData().remove(0);
      generarGrafica("humedad");
      humedadGraficaBoton.setDisable(true);
      temperaturaGraficaBoton.setDisable(false);
      indiceCalorGraficaBoton.setDisable(false);
      humedadTierraGraficaBoton.setDisable(false);
   }

   @FXML
   private void temperaturaGrafica(ActionEvent event) {
      grafica.getData().remove(0);
      generarGrafica("temperatura");
      humedadGraficaBoton.setDisable(false);
      temperaturaGraficaBoton.setDisable(true);
      indiceCalorGraficaBoton.setDisable(false);
      humedadTierraGraficaBoton.setDisable(false);
   }

   @FXML
   private void indiceCalorGrafica(ActionEvent event) {
      grafica.getData().remove(0);
      generarGrafica("indiceCalor");
      humedadGraficaBoton.setDisable(false);
      temperaturaGraficaBoton.setDisable(false);
      indiceCalorGraficaBoton.setDisable(true);
      humedadTierraGraficaBoton.setDisable(false);
   }

   @FXML
   private void humedadTierraGrafica(ActionEvent event) {
      grafica.getData().remove(0);
      generarGrafica("humedadTierra");
      humedadGraficaBoton.setDisable(false);
      temperaturaGraficaBoton.setDisable(false);
      indiceCalorGraficaBoton.setDisable(false);
      humedadTierraGraficaBoton.setDisable(true);
   }

   // Funciones reportes
   @FXML
   private void reporteHumedad(ActionEvent event) {
      generarReportes("humedad");
      humedadReporteBoton.setDisable(true);
      temperaturaReporteBoton.setDisable(false);
      indiceCalorReporteBoton.setDisable(false);
      humedadTierraReporteBoton.setDisable(false);
   }

   @FXML
   private void temperaturaReporte(ActionEvent event) {
      generarReportes("temperatura");
      humedadReporteBoton.setDisable(false);
      temperaturaReporteBoton.setDisable(true);
      indiceCalorReporteBoton.setDisable(false);
      humedadTierraReporteBoton.setDisable(false);
   }

   @FXML
   private void indiceCalorReporte(ActionEvent event) {
      generarReportes("indiceCalor");
      humedadReporteBoton.setDisable(false);
      temperaturaReporteBoton.setDisable(false);
      indiceCalorReporteBoton.setDisable(true);
      humedadTierraReporteBoton.setDisable(false);
   }

   @FXML
   private void humedadTierraReporte(ActionEvent event) {
      generarReportes("humedadTierra");
      humedadReporteBoton.setDisable(false);
      temperaturaReporteBoton.setDisable(false);
      indiceCalorReporteBoton.setDisable(false);
      humedadTierraReporteBoton.setDisable(true);
   }

   @FXML
   private void salir(ActionEvent event) {
      Stage stage = (Stage) salirBoton.getScene().getWindow();
      try {
         arduino.killArduinoConnection();
      } catch (ArduinoException ex) {
         Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
      }
      stage.close();
   }
}
