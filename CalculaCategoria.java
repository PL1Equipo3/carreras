//Programa que coge a una persona de la base de datos y le calcula en que categoría debería de estar


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import net.ucanaccess.*;

public class CalculaCategoria {
	public static void main(String[] args){
		try{
			//Conexión con base de datos
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://Carreras.accdb");
			Statement s = conn.createStatement();
			//Variables y ED
			String SQL, FechaNC = null, FechaN = null, sexo=null;
			ArrayList<String> Categorias = new ArrayList<String>();
			ArrayList<Integer> edadMin = new ArrayList<Integer>();
			ArrayList<Integer> edadMax = new ArrayList<Integer>();
			
			//Objeto SimpleDateFormat para pasar de String a Date
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			//Seleccionar corredorde la BD
			SQL= "SELECT * FROM Atleta";
			s.execute(SQL);
			ResultSet rs = s.getResultSet();
			while((rs!=null) && (rs.next())){
				FechaNC=rs.getString(5);   //El 5 porque es donde esta la fecha de nacimiento
				sexo=rs.getString(4);      //En el 4 está el sexo
			}

			//Cortar String
			FechaN=FechaNC.substring(0, 10);
			
			//Pasar String a Date
			java.util.Date FechaNac = formatter.parse(FechaN);
			
			//Calcular Edad
			int Edad = CalculaEdad(FechaNac);
			
			//Consulta SQL para saber las categorías y guardarlas en las estructuras
			
			//Comprobar Categoría Según la edad (nido de ifs)
			
			//Escribirla en la BD?


		}
		catch(Exception ex){
			ex.printStackTrace();
		}


	}
	public static int CalculaEdad(Date fechaN){
		int Edad = 0;
		Calendar dob = Calendar.getInstance();
		dob.setTime(fechaN);
		Calendar today = Calendar.getInstance();
		Edad = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		if (today.get(Calendar.DAY_OF_YEAR) <= dob.get(Calendar.DAY_OF_YEAR))
			Edad--;
		return Edad;
	}

}
