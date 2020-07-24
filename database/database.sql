create database sensores;
use sensores;

create table datos (
   id int auto_increment primary key,
   fecha datetime,
   humedad int,
   temperatura float,
   indiceCalor float,
   humedadTierra int,
   nivelLuz int,
   nivelHumoGas int
);

-- Consultas para obtener datos que se mostrar datos en la grafica
-- Obtener valores máximos
SELECT day(fecha), max(humedad) maxHumedad, max(temperatura) maxTemperatura, max(indiceCalor) maxIndiceCalor, max(humedadTierra) maxHumedadTierra, max(nivelLuz) maxNivelLuz, max(nivelHumoGas) maxHumoGas FROM datos WHERE day(fecha) <= day(sysdate()) GROUP BY day(fecha);

-- Obtener valores mínimos
SELECT day(fecha), min(humedad) minHumedad, min(temperatura) minTemperatura, min(indiceCalor) minIndiceCalor, min(humedadTierra) minHumedadTierra, min(nivelLuz) minNivelLuz, min(nivelHumoGas) minHumoGas FROM datos WHERE day(fecha) <= day(sysdate()) GROUP BY day(fecha);

-- Obtener valor promedio
SELECT day(fecha), avg(humedad) promedioHumedad, avg(temperatura) promedioTemperatura, avg(indiceCalor) promedioIndiceCalor, avg(humedadTierra) promedioHumedadTierra, avg(nivelLuz) promedioNivelLuz, avg(nivelHumoGas) promedioNivelHumoGas FROM datos WHERE day(fecha) <= day(sysdate()) GROUP BY day(fecha);

--Obtener valor max, min y promedio
SELECT day(fecha), avg() promedio, min() minimo, max() maximo FROM datos WHERE id = 1 GROUP BY day(fecha);
