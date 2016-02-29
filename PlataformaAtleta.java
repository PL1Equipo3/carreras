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
			//Ruta absoluta o relativa como par�metro de getConnection
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://F:/3�universidad/2cuatrimestre/sistemas de infomacion/Proyecto carreras/Carreras 2� versi�n.accdb");
			s = conn.createStatement();
			
			String opcion;	
			do{
				System.out.println("Bienvenido!");
				System.out.println("�Qu� desea hacer?\n"
						+ "\t(1) INSCRIBIME EN UNA CARRERA"
						+ "\t(2) COSULTAR EL ESTADO DE MI INSCRIPCI�N"
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
					System.out.println("Opci�n no v�lida.");
				}
			} while ( !opcion.equals("0") );
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
	/**M�todo para consultar los datos de un atleta en una competici�n, puede introducir el DNI(Id_atleta) o Id_Inscripci�n
	 *   -DNI: se le muestra al atleta todas las competiciones de las que puede consultar los datos
	 *   -ID_inscripci�n: se muestra los datos de la competici�n al que corresponde el c�digo introducido
	 * 
	 * @return true si el atleta esta inscrito en alguna competici�n y ha podido realizar la consulta correctamente
	 * 		   false si ha habido alg�n error 
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
		
		//El atleta elige la forma de identificacion, con su DNI o Id_inscripcion de la competici�n
		
		do{
			System.out.println("C�mo desea identificarse, mediante DNI o con Id_inscripci�n? (DNI/ID)\n");
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
				
			s.execute("SELECT Id_competici�n FROM Inscripci�n WHERE (Id_atleta='" + identificacion + "')");
			competicion = s.getResultSet();
			
			//Obtener el numero de competiciones en las que esta inscrito o preinscrito un atleta que este en la base de datos
			//si no esta inscrito o preinscrito en ninguna se le informa
			
			s.execute("SELECT COUNT(Id_competici�n) FROM Inscripci�n WHERE (Id_atleta='" + identificacion +"')");
		    numcomp = s.getResultSet();
			numcomp.next();
			
			//El atleta esta inscrito o preinscrito en almenos una competici�n
			
			if (numcomp.getInt(1) != 0) {
				
				
				//Muestra todas las competiciones en las que el atleta est� inscrito
				
				System.out.println("Las competiciones de las que puedes consultar tu estado son :\n  ");
				while((competicion.next()))
				{
					System.out.println("\t" + competicion.getString(1) + "\n" );
				}
				
				//El atleta elige de que competici�n quiere consultar sus datos y estado
				System.out.println("De que competici�n quieres obtener los datos:\n");
				elegida = in.nextLine();
				
				// Se obtiene todos los datos de dicha competici�n
				
					s.execute("SELECT Nombre, Apellidos, [Id_inscripci�n], [Fecha inscripci�n], Estado, [Cuota abonada], [Forma de pago], Categor�a "
							+ "FROM Atleta INNER JOIN Inscripci�n  "
							+ "ON Inscripci�n.Id_atleta=Atleta.DNI "
							+ "WHERE (Inscripci�n.Id_competici�n = '" + elegida + "' AND Inscripci�n.Id_atleta='" + identificacion +"')");
					resultado = s.getResultSet();
					resultado.next();
					
					//Mostramos al atleta los datos de su consulta
					
					System.out.println("Tus datos para la competci�n " + elegida + " son los siguientes:");
					System.out.println("\t" + "Nombre: "+ resultado.getString(1) + " \n" +
								       "\t" + "Apellidos: " + resultado.getString(2) + " \n" + 
								       "\t" + "Id_inscripci�n: " + resultado.getString(3) + " \n" +
								       "\t" + "Fecha inscripci�n: " + resultado.getString(4) + " \n" +
								       "\t" + "Estado: " + resultado.getString(5) + " \n"+
								       "\t" + "Cuota abonada: " + resultado.getString(6) + " \n" +
								       "\t" + "Forma de pago: " + resultado.getString(7) + " \n" +
								       "\t" + "Categor�a: " + resultado.getString(8) );
				
				
					return true;
			}

			//El atleta no esta inscrito o preinscrito en ninguna competici�n
			//muestra un mensaje que le dice la opcion para hacerlo
			
			else {
				
				System.out.println("No estas inscrito en ninguna competici�n. Para hacerlo elige la opci�n 1 en el menu inicial");
				return false;
			}
			
			}
		}
		
		//El atleta se identifica mediante el Id_inscripci�n
		else {
			
			System.out.print("Introduzca el Id_inscripci�n: \n");
			identificacion = in.nextLine();
			
			//Comprobar que el Id_inscripcion existe o no en la BD
			
			s.execute("SELECT Id_atleta FROM Inscripci�n WHERE( Id_inscripci�n='" + identificacion + "')"); 
			comprobar = s.getResultSet();
			
			
			//No existe el Id_inscripci�n en la BD
			
			if (comprobar.next() == false){
				
				System.out.print("El Id_inscripci�n introducido no existe!!\n");
				return false;
			}
			
			//El Id_inscripci�n existe, ahora hay que verificar que corresponde al atleta
			
			else {
				
			//Comprobar que el Id_inscripci�n se corresponde al atleta y no fue una equivocacion
			
			//Le mostramos al atleta su DNI para que verifique que el Id_inscripci�n que ha etido es correcto
			
			System.out.println("Para verificar que el Id_inscripci�n introducido te corresponde te mostramos tu DNI: " + comprobar.getString(1));
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
				
				System.out.println("Introduciste un Id_inscripci�n que es incorrecto");
				return false;
			}
			
			//El Id_inscripcion es correcto y el atleta existe en la BD y esta inscrito en una competci�n
			//Ense�amos los datos de la competici�n 
			
			else{
				
				//Obtenemos la competici�n de la que se quiere los datos mediante el Id_inscripci�n
				
				s.execute("SELECT Id_competici�n FROM Inscripci�n WHERE( Id_inscripci�n='" + identificacion + "')"); 
				ResultSet CompeticionID = s.getResultSet(); 
				CompeticionID.next();
				
				
				
				// Se obtiene todos los datos de dicha competici�n
				
					s.execute("SELECT Nombre, Apellidos, [Id_inscripci�n], [Fecha inscripci�n], Estado, [Cuota abonada], [Forma de pago], Categor�a "
							+ "FROM Atleta INNER JOIN Inscripci�n  "
							+ "ON Inscripci�n.Id_atleta=Atleta.DNI "
							+ "WHERE (Inscripci�n.Id_competici�n = '" + CompeticionID + "' AND Inscripci�n.Id_atleta='" + identificacion +"')");
					resultado = s.getResultSet();
					resultado.next();
					
					//Mostramos al atleta los datos de su consulta
					
					System.out.println("Tus datos para la competci�n " + CompeticionID.getString(1) + " son los siguientes:");
					System.out.println("\t" + "Nombre: "+ resultado.getString(1) + " \n" +
								       "\t" + "Apellidos: " + resultado.getString(2) + " \n" + 
								       "\t" + "Id_inscripci�n: " + resultado.getString(3) + " \n" +
								       "\t" + "Fecha inscripci�n: " + resultado.getString(4) + " \n" +
								       "\t" + "Estado: " + resultado.getString(5) + " \n"+
								       "\t" + "Cuota abonada: " + resultado.getString(6) + " \n" +
								       "\t" + "Forma de pago: " + resultado.getString(7) + " \n" +
								       "\t" + "Categor�a: " + resultado.getString(8) );
				
				
					return true;
			

			}
			
		}
		}
		
		
		
	}
	

}