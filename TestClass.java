import java.sql.*; 
import net.ucanaccess.*;

public class TestClass {
	public static void main(String[] args){
		try{
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Rodry/Desktop/La Carpeta/Universidad/SINF/Base De Datos/BaseDeDatos/Carreras.accdb");
			Statement s = conn.createStatement();
			// create a table
            String tableName = "myTable" + String.valueOf((int)(Math.random() * 1000.0));
            String createTable = "CREATE TABLE " + tableName + 
                                 " (id Integer, name Text(32))";
            s.execute(createTable); 
            
            // enter value into table
            for(int i=0; i<25; i++)
            {
              String addRow = "INSERT INTO " + tableName + " VALUES ( " + 
                     String.valueOf((int) (Math.random() * 32767)) + ", 'Text Value " + 
                     String.valueOf(Math.random()) + "')";
              s.execute(addRow);
            }
            
            // Fetch table
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
