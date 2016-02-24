//Rerdar cambiar la ruta absoluta de la base de datos.
//Si la ruta absoluta no funcionase, meter la base de datos en el directorio del proyecto y poner "jdbc:ucanaccess://Carreras.accdb"
//Para introducir una fecha por comando SQL: "#AAAA-MM-DD#"

import java.sql.*; 
import net.ucanaccess.*;

public class TestClass {
	public static void main(String[] args){
		try{
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			//Ruta absoluta o relativa como parámetro de getConnection
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://Carreras.accdb");
			Statement s = conn.createStatement();
			// Crea una tabla con dos campos: un id y un nombre
			String tableName = "myTable" + String.valueOf((int)(Math.random() * 1000.0));
			String createTable = "CREATE TABLE " + tableName + 
					" (id Integer, name Text(32))";
			s.execute(createTable); 

			// Añade valores a la tabla
			for(int i=0; i<25; i++)
			{
				String addRow = "INSERT INTO " + tableName + " VALUES ( " + 
						String.valueOf((int) (Math.random() * 32767)) + ", 'Text Value " + 
						String.valueOf(Math.random()) + "')";
				s.execute(addRow);
			}

			// Consulta que selecciona todos los valores y los imprime por pantalla
			String selTable = "SELECT * FROM " + tableName;
			s.execute(selTable);
			ResultSet rs = s.getResultSet();
			while((rs!=null) && (rs.next()))
			{
				System.out.println(rs.getString(1) + " : " + rs.getString(2));
			}

			s.execute("INSERT INTO Plazos VALUES (1, #2016-02-10#, #2016-02-20#, 10, 2)");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
