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
			String SQL, FechaNC = null, FechaN = null, sexo=null , C=null;
			ArrayList<String> Categorias = new ArrayList<String>();
			ArrayList<Integer> edadMin = new ArrayList<Integer>();
			ArrayList<Integer> edadMax = new ArrayList<Integer>();
			int Cont=0, varEdadMin, varEdadMax;

			//Variables que han de pasarse como parámetro
			String Id_Inscripcion="1337";
			String comp="Travesera13";
			String DNI="71906024F";
			
			
			
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

			FechaN=FechaNC.substring(0, 10);						//Cortar String			
			java.util.Date FechaNac = formatter.parse(FechaN);		//Pasar String a Date			
			int Edad = CalculaEdad(FechaNac);						//Calcular Edad
		
			//Consulta SQL para saber las categorías 
			SQL="SELECT * FROM Categoría INNER JOIN [Categorías de la competición] ON Categoría.Id_categoría=[Categorías de la competición].Id_categoría WHERE Categoría.Sexo='" + sexo + "' AND [Categorías de la competición].Id_competición='" + comp +"'";
			s.execute(SQL);
			rs = s.getResultSet();
			while((rs!=null) && (rs.next())){			//Almacenar categorías y edades
				Categorias.add(rs.getString(2));
				edadMin.add(rs.getInt(3));
				edadMax.add(rs.getInt(4));
				Cont++;
			}
			
			//Calcular en que competición está
			for (int i=0; i<Cont; i++){
				varEdadMin=edadMin.get(i);
				varEdadMax=edadMax.get(i);
				if (varEdadMax == 0) varEdadMax=200;	//Control del valor máx de la última categoría que es 0
				if ((Edad>=varEdadMin) && (Edad<=varEdadMax)){
					C=Categorias.get(i);
				}
			}
			
			//Escribirla en la BD
			SQL="UPDATE Inscripción SET Categoría='" + C +"' WHERE Id_Inscripción='" + Id_Inscripcion + "'";
			s.execute(SQL);
			


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
