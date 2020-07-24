package clases;

public class Sensor {
   String fecha;
   String valorMinimo;
   String valorMaximo;
   String promedio;
   
   public Sensor(String fecha, String valorMinimo, String valorMaximo, String promedio) {
      this.fecha = fecha;
      this.valorMinimo = valorMinimo;
      this.valorMaximo = valorMaximo;
      this.promedio = promedio;
   }
   
   public String getFecha() {
      return this.fecha;
   }
   public String getValorMinimo() {
      return this.valorMinimo;
   }
   public String getValorMaximo() {
      return this.valorMaximo;
   }
   public String getPromedio() {
      return this.promedio;
   }
}