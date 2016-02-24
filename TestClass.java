//Rerdar cambiar la ruta absoluta de la base de datos.
//Para introducir una fecha por comando SQL: "AAAA/MM/DD HH:MM:SS.000000"

import java.sql.*; 
import net.ucanaccess.*;

public class TestClass {
	public static void main(String[] args){
		try{
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			//Cambiar ruta absoluta.
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Rodry/Desktop/La Carpeta/Universidad/SINF/Base De Datos/BaseDeDatos/Carreras.accdb");
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
		}
		catch(Exception ex){
            ex.printStackTrace();
        }
	}

}
