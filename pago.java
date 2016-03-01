import java.sql.*;
import net.ucanaccess.*;

public class Carrera {

	public static boolean Pagar(String Id_inscripción, String Id_competición, String Id_atleta){  
	
		int Cuota_abonada = 0;
		String orden;
		Date fechaInscripcion, fechaInicio, fechaFin;
		ResultSet rs;
	
		try{
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			//Ruta absoluta o relativa como parámetro de getConnection
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://Carreras 2ª versión.accdb");
			Statement s = conn.createStatement();
		
		
			//Actualiza el estado de la inscripción a Preinscrito
			orden = "UPDATE Inscripción SET Estado = 'Preinscrito' WHERE "
					+ "(Id_inscripción = '"+Id_inscripción+"')";
		
			s.execute(orden);
		
		
			//Actualiza la forma de pago de la inscripción a Transferencia
			orden = "UPDATE Inscripción SET [Forma de pago] = 'Transferencia' WHERE "
					+ "(Id_inscripción = '"+Id_inscripción+"')";
				
			s.execute(orden);
		
		
			//Modificar el precio según el plazo en el que se realiza la inscripción
		    //----------------------------------------------------------------------
			
			//Obtener la fecha de inscripción
			orden = "SELECT [Fecha inscripción] FROM Inscripción WHERE "
					+ "(Id_inscripción = '"+Id_inscripción+"')";
		    
			rs=s.executeQuery(orden);
			rs.next();
		    fechaInscripcion=rs.getDate(1);
			
			
			//Obtener las fechas de los plazos
			orden = "SELECT * FROM Plazos WHERE (Id_competición = '"+Id_competición+"')";
			rs=s.executeQuery(orden);
			rs.next();
		
			fechaInicio=rs.getDate(2);
			fechaFin=rs.getDate(3);
			System.out.println(fechaInicio);
			System.out.println(fechaFin);
			
			//Comprobar en que plazo se realizó la inscripción
			while( !((fechaInicio.before(fechaInscripcion) || fechaInicio.equals(fechaInscripcion)) 
					&& (fechaFin.after(fechaInscripcion) || fechaFin.equals(fechaInscripcion)))
					&& (rs.next()) ){
				
				fechaInicio=rs.getDate(2);
				fechaFin=rs.getDate(3);
				
			}
			
			Cuota_abonada = rs.getInt(4);
			System.out.println(Cuota_abonada);
			
			//Modificar el precio según si está federado o no
			orden = "SELECT Tipo FROM Competición WHERE (Id_competición = '"+Id_competición+"')";
			rs=s.executeQuery(orden);
			rs.next();
			if((rs.getString(1)).equals("Montaña")){
				orden = "SELECT [Número federado] FROM Atleta WHERE (DNI = '"+Id_atleta+"')";
				rs=s.executeQuery(orden);
				rs.next();
				if(!rs.getString(1).equals("")){
					Cuota_abonada=Cuota_abonada-15;	
				}
			}
		
			//Actualiza el precio en la Cuota abonada
			orden = "UPDATE Inscripción SET [Cuota abonada] = "+Cuota_abonada+" WHERE (Id_inscripción = '"+Id_inscripción+"')";
		
			s.execute(orden);
			System.out.println(Cuota_abonada);
		
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		Pagar("58785415TTravesera13", "Travesera13", "58785415T");
	}

}
