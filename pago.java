import java.sql.*;
import net.ucanaccess.*;

public class Carrera {

	public static int Pagar(String Id_inscripción, String Id_competición, String Id_atleta){  
	int Cuota_abonada = 0;
	String orden;
	String fechaInscripcion;
	// fechasInicio;
	// fechasFin;
	ResultSet plazos;
	try{
		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		//Ruta absoluta o relativa como parámetro de getConnection
		Connection conn=DriverManager.getConnection("jdbc:ucanaccess://Carreras 2ª versión.accdb");
		Statement s = conn.createStatement();
		
		
		//Actualiza el estado de la inscripción a Preinscrito
		orden = "UPDATE 'Inscripción' SET 'Estado' = 'Preinscrito' WHERE "
				+ "('Id_inscripción' = '"+Id_inscripción+"')";
		
		s.execute(orden);
		
		
		//Actualiza la forma de pago de la inscripción a Transferencia
		orden = "UPDATE 'Inscripción' SET 'Forma de pago' = 'Transferencia' WHERE "
				+ "('Id_inscripción' = '"+Id_inscripción+"')";
				
		s.execute(orden);
		
		//Modificar el precio según el plazo en el que se realiza la inscripción
		
		//Obtener la fecha de inscripción
		orden = "SELECT 'Fecha inscripción' FROM 'Inscripción' WHERE "
				+ "('Id_inscripción' = '"+Id_inscripción+"')";
		fechaInscripcion=s.executeQuery(orden).toString();
		Date Fecha = Date.valueOf(fechaInscripcion);
		
		//Obtener las fechas de los plazos
		orden = "SELECT * FROM 'Plazos' WHERE ('Id_competición' = '"+Id_competición+"')";
		plazos=s.executeQuery(orden);
		/*
		sacar las fechas de inicio y de fin
		comprobar Fecha>fechaInicio && Fecha<fechaFin
		sacar la cuota correspondiente
		*/
		//Modificar el precio según si está federado o no
		orden = "SELECT 'Tipo' FROM 'Competición' WHERE ('Id_competición' = '"+Id_competición+"')";
		if((s.executeQuery(orden).getString(1)) == "Montaña"){
			orden = "SELECT 'Número federado' FROM 'Atleta' WHERE ('DNI = '"+Id_atleta+"')";
			if(!s.executeQuery(orden).wasNull()){
				Cuota_abonada=Cuota_abonada-15;	
			}
		}
		
		//Actualiza el precio en la Cuota abonada
		orden = "UPDATE 'Inscripción' SET 'Cuota abonada' = "+Cuota_abonada+" WHERE ('Id_inscripción' = '"+Id_inscripción+"')";
		
		s.execute(orden);
		
	}
	catch(Exception ex){
		ex.printStackTrace();
	}
	return Cuota_abonada;
	}
	
	public static void main(String[] args) {
		
		Pagar("58785415TTravesera13", "Travesera13", "58785415T");

	}

}
