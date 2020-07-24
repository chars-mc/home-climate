package clases;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {
   private Connection conexion;
   private Statement statement;
   private ResultSet resultSet;

   public DB() {
      try {
         Class.forName("com.mysql.jdbc.Driver").newInstance();
         conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/sensores", "root", "");
         statement = (Statement) conexion.createStatement();
      } catch (ClassNotFoundException ex) {
         Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
      } catch (SQLException ex) {
         Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
      } catch (InstantiationException ex) {
         Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalAccessException ex) {
         Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public void guardarDB(String[] datosSensores) {
      String datos = "insert into datos(fecha, humedad, temperatura, indiceCalor, humedadTierra, nivelLuz, nivelHumoGas) " + "values(sysdate(), "
              + datosSensores[0] + ", " + datosSensores[1] + ", " + datosSensores[2] + ", " + datosSensores[3] + ", " + datosSensores[4] + ", "
              + datosSensores[5] + ")";
      try {
         statement.execute(datos);
      } catch (SQLException ex) {
         Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public String obtenerDatosReporte(String sensor) {
      String consulta = "SELECT fecha, avg(" + sensor + ") promedio, min(" + sensor + ") minimo, max(" + sensor + ") maximo FROM datos " + "GROUP BY day(fecha);";
      resultSet = null;
      String datosObtenidos = "";

      try {
         resultSet = statement.executeQuery(consulta);

         while (resultSet.next()) {
            datosObtenidos += resultSet.getString(1) + "/";
            if (sensor == "temperatura" || sensor == "indiceCalor") {
               datosObtenidos += resultSet.getFloat(2) + "/";
               datosObtenidos += resultSet.getFloat(3) + "/";
               datosObtenidos += resultSet.getFloat(4) + "/";
            } else {
               datosObtenidos += resultSet.getInt(2) + "/";
               datosObtenidos += resultSet.getInt(3) + "/";
               datosObtenidos += resultSet.getInt(4) + "/";
            }
         }
      } catch (SQLException ex) {
         Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
      }
      return datosObtenidos;
   }

   public String obtenerDatosGrafica(String sensor) {
      String consulta = "SELECT avg(" + sensor + ") promedio, min(" + sensor + ") minimo, max(" + sensor + ") maximo FROM datos;";
      resultSet = null;
      String datosObtenidos = "";

      try {
         resultSet = statement.executeQuery(consulta);

         while (resultSet.next()) {
            datosObtenidos += resultSet.getInt(1) + "/";
            if (sensor == "temperatura" || sensor == "indiceCalor") {
               datosObtenidos += resultSet.getInt(2) + "/";
               datosObtenidos += resultSet.getInt(3) + "/";
            } else {
               datosObtenidos += resultSet.getInt(2) + "/";
               datosObtenidos += resultSet.getInt(3) + "/";
            }
         }
      } catch (SQLException ex) {
         Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
      }
      return datosObtenidos;
   }

   public String obetenerDatosMinimosMaximos(String sensor) {
      String consulta = "SELECT min(" + sensor + ") minimo, max(" + sensor + ") maximo FROM datos";
      resultSet = null;
      String datosObtenidos = "";

      try {
         resultSet = statement.executeQuery(consulta);

         while (resultSet.next()) {
            if (sensor == "temperatura" || sensor == "indiceCalor") {
               datosObtenidos += resultSet.getFloat(1) + "-";
               datosObtenidos += resultSet.getFloat(2) + "-";
            } else {
               datosObtenidos += resultSet.getInt(1) + "-";
               datosObtenidos += resultSet.getInt(2) + "-";
            }
         }

      } catch (SQLException ex) {
         Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
      }

      return datosObtenidos;
   }

   public void cerrarDB() {
      try {
         conexion.close();
      } catch (SQLException ex) {
         Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
