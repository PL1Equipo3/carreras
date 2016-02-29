import java.sql.*;
import java.util.Scanner; 
import net.ucanaccess.*;

public class PlataformaAtleta {	
	static Scanner in = new Scanner(System.in);
    static Statement s;
    
	public static void main(String[] args){
		try{
			boolean exito;
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			//Ruta absoluta o relativa como parámetro de getConnection
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://F:/3ºuniversidad/2cuatrimestre/sistemas de infomacion/Proyecto carreras/Carreras 2ª versión.accdb");
			s = conn.createStatement();
			
			String opcion;	
			do{
				System.out.println("Bienvenido!");
				System.out.println("¿Qué desea hacer?\n"
						+ "\t(1) INSCRIBIME EN UNA CARRERA"
						+ "\t(2) COSULTAR EL ESTADO DE MI INSCRIPCIÓN"
						+ "\t(0) SALIR");
				opcion = in.nextLine();
				switch(opcion) {
				case "1":
					 break;
				case "2":
					exito = ConsultaInscripcion(); 
					if (exito)
						System.out.println("Datos consultados correctamente!!\n");
					else
						System.out.println("Error al consultar los datos de tus competiciones!!\n");
					break;
				case "0":
					break;
				default:
					System.out.println("Opción no válida.");
				}
			} while ( !opcion.equals("0") );
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
	/**Método para consultar los datos de un atleta en una competición, puede introducir el DNI(Id_atleta) o Id_Inscripción
	 *   -DNI: se le muestra al atleta todas las competiciones de las que puede consultar los datos
	 *   -ID_inscripción: se muestra los datos de la competición al que corresponde el código introducido
	 * 
	 * @return true si el atleta esta inscrito en alguna competición y ha podido realizar la consulta correctamente
	 * 		   false si ha habido algún error 
	 * 				-la identificacion introducida no es correcta
	 *              -el atleta no esta inscrito en ninguna competicion
	 * @throws Exception
	 */
	
	private static boolean ConsultaInscripcion() throws Exception {
		
		String identificacion;
		String elegida;
		ResultSet competicion;
		ResultSet resultado;
		ResultSet comprobar;
		ResultSet numcomp;
		
		//El atleta elige la forma de identificacion, con su DNI o Id_inscripcion de la competición
		
		do{
			System.out.println("Cómo desea identificarse, mediante DNI o con Id_inscripción? (DNI/ID)\n");
			identificacion = in.nextLine();
			if ( !(identificacion.equals("DNI") || identificacion.equals("ID")) ) {
				System.out.println("Escriba 'DNI' o 'ID'");
			}
		} while ( !(identificacion.equals("DNI") || identificacion.equals("ID")) );
		
		
		//El atleta introduce el DNI como identificador
		
		if (identificacion.equals("DNI")){
			
			System.out.print("Introduzca el DNI: \n");
			identificacion = in.nextLine();
			
			//Comprobar que el DNI es correcto o incorrecto
			
			s.execute("SELECT * FROM Atleta WHERE( DNI='" + identificacion + "')");
			comprobar = s.getResultSet();
			
			
			//EL DNI es incorrecto 
			
			if (comprobar.next()==false){
				
				System.out.println("Error en el DNI introducido");
				return false;
				
			}
			
			//El DNI es correcto y el atleta existe en la BD
			else{
				
			
			//Se buscan las competiciones en las que esta  inscrito o preinscrito el atleta
				
			s.execute("SELECT Id_competición FROM Inscripción WHERE (Id_atleta='" + identificacion + "')");
			competicion = s.getResultSet();
			
			//Obtener el numero de competiciones en las que esta inscrito o preinscrito un atleta que este en la base de datos
			//si no esta inscrito o preinscrito en ninguna se le informa
			
			s.execute("SELECT COUNT(Id_competición) FROM Inscripción WHERE (Id_atleta='" + identificacion +"')");
		    numcomp = s.getResultSet();
			numcomp.next();
			
			//El atleta esta inscrito o preinscrito en almenos una competición
			
			if (numcomp.getInt(1) != 0) {
				
				
				//Muestra todas las competiciones en las que el atleta está inscrito
				
				System.out.println("Las competiciones de las que puedes consultar tu estado son :\n  ");
				while((competicion.next()))
				{
					System.out.println("\t" + competicion.getString(1) + "\n" );
				}
				
				//El atleta elige de que competición quiere consultar sus datos y estado
				System.out.println("De que competición quieres obtener los datos:\n");
				elegida = in.nextLine();
				
				// Se obtiene todos los datos de dicha competición
				
					s.execute("SELECT Nombre, Apellidos, [Id_inscripción], [Fecha inscripción], Estado, [Cuota abonada], [Forma de pago], Categoría "
							+ "FROM Atleta INNER JOIN Inscripción  "
							+ "ON Inscripción.Id_atleta=Atleta.DNI "
							+ "WHERE (Inscripción.Id_competición = '" + elegida + "' AND Inscripción.Id_atleta='" + identificacion +"')");
					resultado = s.getResultSet();
					resultado.next();
					
					//Mostramos al atleta los datos de su consulta
					
					System.out.println("Tus datos para la competción " + elegida + " son los siguientes:");
					System.out.println("\t" + "Nombre: "+ resultado.getString(1) + " \n" +
								       "\t" + "Apellidos: " + resultado.getString(2) + " \n" + 
								       "\t" + "Id_inscripción: " + resultado.getString(3) + " \n" +
								       "\t" + "Fecha inscripción: " + resultado.getString(4) + " \n" +
								       "\t" + "Estado: " + resultado.getString(5) + " \n"+
								       "\t" + "Cuota abonada: " + resultado.getString(6) + " \n" +
								       "\t" + "Forma de pago: " + resultado.getString(7) + " \n" +
								       "\t" + "Categoría: " + resultado.getString(8) );
				
				
					return true;
			}

			//El atleta no esta inscrito o preinscrito en ninguna competición
			//muestra un mensaje que le dice la opcion para hacerlo
			
			else {
				
				System.out.println("No estas inscrito en ninguna competición. Para hacerlo elige la opción 1 en el menu inicial");
				return false;
			}
			
			}
		}
		
		//El atleta se identifica mediante el Id_inscripción
		else {
			
			System.out.print("Introduzca el Id_inscripción: \n");
			identificacion = in.nextLine();
			
			//Comprobar que el Id_inscripcion existe o no en la BD
			
			s.execute("SELECT Id_atleta FROM Inscripción WHERE( Id_inscripción='" + identificacion + "')"); 
			comprobar = s.getResultSet();
			
			
			//No existe el Id_inscripción en la BD
			
			if (comprobar.next() == false){
				
				System.out.print("El Id_inscripción introducido no existe!!\n");
				return false;
			}
			
			//El Id_inscripción existe, ahora hay que verificar que corresponde al atleta
			
			else {
				
			//Comprobar que el Id_inscripción se corresponde al atleta y no fue una equivocacion
			
			//Le mostramos al atleta su DNI para que verifique que el Id_inscripción que ha etido es correcto
			
			System.out.println("Para verificar que el Id_inscripción introducido te corresponde te mostramos tu DNI: " + comprobar.getString(1));
			System.out.println("El DNI es correcto?? (SI/NO)");
			String seguir;
			
	        //El atleta nos informa de si el DNI que le hemos mostrado es correcto y sigue con la consulta de datos o es incorrecto y salimos
			//Insistimos hasta que nos responda
			
			do{
			   seguir =in.nextLine();
			   if ( !(seguir.equals("SI") || seguir.equals("NO")) ) {
					System.out.println("Escriba 'SI' o 'NO'");
				
			   }
			}while ( !(seguir.equals("SI") || seguir.equals("NO")) );
			
			//El atleta introdujo un Id_inscripcion incorrecto
			
			if (seguir.equals("NO") ){
				
				System.out.println("Introduciste un Id_inscripción que es incorrecto");
				return false;
			}
			
			//El Id_inscripcion es correcto y el atleta existe en la BD y esta inscrito en una competción
			//Enseñamos los datos de la competición 
			
			else{
				
				//Obtenemos la competición de la que se quiere los datos mediante el Id_inscripción
				
				s.execute("SELECT Id_competición FROM Inscripción WHERE( Id_inscripción='" + identificacion + "')"); 
				ResultSet CompeticionID = s.getResultSet(); 
				CompeticionID.next();
				
				
				
				// Se obtiene todos los datos de dicha competición
				
					s.execute("SELECT Nombre, Apellidos, [Id_inscripción], [Fecha inscripción], Estado, [Cuota abonada], [Forma de pago], Categoría "
							+ "FROM Atleta INNER JOIN Inscripción  "
							+ "ON Inscripción.Id_atleta=Atleta.DNI "
							+ "WHERE (Inscripción.Id_competición = '" + CompeticionID + "' AND Inscripción.Id_atleta='" + identificacion +"')");
					resultado = s.getResultSet();
					resultado.next();
					
					//Mostramos al atleta los datos de su consulta
					
					System.out.println("Tus datos para la competción " + CompeticionID.getString(1) + " son los siguientes:");
					System.out.println("\t" + "Nombre: "+ resultado.getString(1) + " \n" +
								       "\t" + "Apellidos: " + resultado.getString(2) + " \n" + 
								       "\t" + "Id_inscripción: " + resultado.getString(3) + " \n" +
								       "\t" + "Fecha inscripción: " + resultado.getString(4) + " \n" +
								       "\t" + "Estado: " + resultado.getString(5) + " \n"+
								       "\t" + "Cuota abonada: " + resultado.getString(6) + " \n" +
								       "\t" + "Forma de pago: " + resultado.getString(7) + " \n" +
								       "\t" + "Categoría: " + resultado.getString(8) );
				
				
					return true;
			

			}
			
		}
		}
		
		
		
	}
	

}