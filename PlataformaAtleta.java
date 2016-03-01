import java.sql.*;
import java.util.Scanner; 
import net.ucanaccess.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
				//INSCRIBIRME EN UNA CARRERA
				case "1":
					exito = inscripcion(); 
					if (exito)
						System.out.println("Felicidades se ha inscrito con éxito!");
					else
						System.out.println("Error en la incripción!");
					break;
				//COSULTAR EL ESTADO DE MI INSCRIPCIÓN
				case "2":
					exito = ConsultaInscripcion(); 
					if (exito)
						System.out.println("Datos consultados correctamente!!\n");
					else
						System.out.println("Error al consultar los datos de tus competiciones!!\n");
					break;
				//SALIR
				case "0":
					System.out.println("Hasta pronto!");
					break;
				//OTRO	
				default:
					System.out.println("Opción no válida.");
				}
			} while ( !opcion.equals("0") );
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
	
	
	
	
	/**Método para la inscripción de un atleta en una competición
	 * 
	 * @return true si se ha realizado la inscripción correctamente
	 * 			false si ha habido algún error y no se ha podido completar
	 * 				-no se ha podido identificar/registrar al atleta
	 * 				-no hay plazas disponibles
	 * 				-inscripción fuera de plazo
	 * @throws Exception
	 */
	private static boolean inscripcion() throws Exception {
		String res;
		ResultSet rs;
		do{
			System.out.println("Es la primera vez que se inscribe en una competición? (s/n)");
			res = in.nextLine();
			if ( !(res.equals("s") || res.equals("n")) ) {
				System.out.println("Escriba 's' o 'n'");
			}
		} while ( !(res.equals("s") || res.equals("n")) );

		//Primera vez que se inscribe en una competición
		//Registrar los datos personales del atleta antes
		if ( res.equals("s") ) {
			rs = registro();
			//Registro exitoso
			if (rs!=null){
				System.out.println("Registro exitoso");
				rs.next();
				//rs contiene los datos personales del atleta que se acaba de registrar
				System.out.println(rs.getString(1) + " : " + rs.getString(2));
			}
			//Error en el registro
			else{
				System.out.println("Error en el registro");
				//Volver al flujo principal del programa
				return false;
			}
		}
		//El atleta ya se ha registrado anteriormente en la plataforma
		//Debe identificarse mediante su DNI
		else {
			System.out.println("Introduzca su DNI para indentificarse:");
			res = in.nextLine();
			//Buscar al atleta en la BD
			s.execute("SELECT * FROM Atleta WHERE (DNI='" + res + "')");
			rs = s.getResultSet();
			//no se ha encontrado al atleta en la BD
			if (rs==null) {
				System.out.println("Error en la identificación!");
				//Volver al flujo principal del programa
				return false;
			}
			else {
				rs.next();
				//rs contiene los datos personales del atleta que se va a inscribir
				System.out.println(rs.getString(1) + " : " + rs.getString(2));
			}
		}
		return nuevaInscripción(rs);
	}
	
	
	/**Inscripción de un atleta en una competición
	 * 
	 * @return true true si se ha realizado la inscripción correctamente
	 * 			false si ha habido algún error y no se ha podido completar
	 * 				-no se ha podido identificar/registrar al atleta
	 * 				-no hay plazas disponibles
	 * 				-inscripción fuera de plazo
	 * @throws Exception
	 */
	private static boolean nuevaInscripción (ResultSet atleta) throws Exception{
		String comp;
		s.execute("SELECT Nombre, Id_competición FROM Competición");
		//El ResultSet competicion contiene el nombre e id de todas las competiciones registradas en la BD
		ResultSet competicion = s.getResultSet();
		//Comprobar que hay alguna competición en la BD
		if (competicion.next()!=false) {
			System.out.println("Seleccione la carrera a la que desea inscribirse:");
			//Mostrar todas las competiciones registradas en la BD
			System.out.println("\t" + competicion.getString(2) );
			while((competicion.next()))
			{
				System.out.println("\t" + competicion.getString(2) );
			}
			//El usuario debe introducir por teclado el id de la competición seleccionada!!!!
			comp = in.nextLine();
			s.execute("SELECT * FROM Competición WHERE (Id_competición='" + comp +"')");
			competicion = s.getResultSet();
			competicion.next();
			//competicion contiene los datos de la competición seleccionada
			
			
			
			/* COMPROBAR QUE ESTÁ DENTRO DE PLAZO */
			s.execute("SELECT MIN([Fecha inicio]), MAX([Fecha fin]) FROM Plazos WHERE (Id_competición='" + comp +"')");
			ResultSet fechas = s.getResultSet();
			fechas.next();
			
			//Conseguimos la fecha actual a partir de la hora del sistema
			java.util.Date fechaActual = (Calendar.getInstance()).getTime();
			//Formateador para dar el formato que reguiere sql
			SimpleDateFormat formateador = new SimpleDateFormat("yyy-MM-dd");
	        String fechaSistema=formateador.format(fechaActual);
	        
	        /*Comprobaciones:
	        	System.out.println("Actual: "+fechaSistema);	        
	        	System.out.println("Mínimo: "+formateador.format(fechas.getDate(1)));
	        	System.out.println("Máximo: "+formateador.format(fechas.getDate(2)));
	        */
	        
	        //Pasar las fechas a tipo java.sql.Date para poder compararlas
	        java.sql.Date fechaSQL = new java.sql.Date(fechaActual.getTime());
	        java.sql.Date fechaMin = new java.sql.Date((fechas.getDate(1)).getTime());
	        java.sql.Date fechaMax = new java.sql.Date((fechas.getDate(2)).getTime());
	        //Si la fecha actual no está entre la fecha de comienzo del primer plazo de inscripción 
	        //para esta competición y la de fin del último plazo, no se permite realizar la inscripción
	        if ( !(fechaSQL.after(fechaMin) && fechaSQL.before(fechaMax)) ) {
	        	System.out.println("Fuera de plazo");
	        	return false;
	        }
			
	        
	        
	        /* COMPROBAR QUE HAY PLAZAS LIBRES */
	        s.execute("SELECT COUNT(*) FROM Inscripción WHERE (Id_competición='" + comp +"')");
			ResultSet inscritos = s.getResultSet();
			inscritos.next();
			//Si el número de atletas inscritos es igual (o mayor) al de plazas disponibles es que ya no hay más
				//inscritos.getInt(1)=número de inscripciones registradas en la BD para esa competición
				//competicion.getInt(4)=número máximo de plazas disponibles para la competición seleccionada
			if (inscritos.getInt(1)>=competicion.getInt(4)) {
				System.out.println("Plazas agotadas");
				return false;
			}
	        
			
			
			/* COMPROBAR QUE EL ATLETA NO ESTÁ YA INSCRITO EN LA COMPETICIÓN */
			s.execute("SELECT * FROM Inscripción WHERE (Id_atleta='" + atleta.getString(1) +"')");
	        if ((s.getResultSet()).next()) {
	        	System.out.println("Usted ya está inscrito en esta competición");
	        	return false;
	        }
	        	
	        
			 
	        //Si está dentro de plazo, hay plazas disponibles y el atleta no está ya inscrito, registrar la inscripción
			s.execute("INSERT INTO Inscripción (Id_inscripción, [Fecha inscripción], Id_atleta, Id_competición) VALUES ('"
					+ atleta.getString(1) + "-" + competicion.getString(1) + "', "
					+ "#" + fechaSistema + "#, "
					+ "'" + atleta.getString(1) + "', "
					+ "'" + competicion.getString(1) + "')" );
			return true;
		}
		//No hay ninguna competición registrada en la BD
		else {
			System.out.println("No hay competiciones abiertas en estos momentos.");
			return false;
		}
	}
	
	/**Registro de un nuevo atleta en la BD
	 * 
	 * @return ResultSet con el resultado de la inserción de los datos personales del atleta
	 * 			Contendrá todos los datos del atleta si el registro se completó correctamente
	 * 			Será null si hubo algún problema y no se pudo completar el registro
	 */
	private static ResultSet registro() throws Exception{
		String[] datosAtleta = new String[12];
		String res;

		//Nombre
		System.out.print("Introduzca: (*=campo obligatorio)\n"
				+ "\tSu nombre*: ");
		res = in.nextLine();
		introducirDato(true, res, 1, datosAtleta);

		//Apellidos
		System.out.print("\tSus apellidos*: ");
		res = in.nextLine();
		introducirDato(true, res, 2, datosAtleta);

		//DNI
		System.out.print("\tSu DNI*: ");
		res = in.nextLine();
		introducirDato(true, res, 0, datosAtleta);

		//Sexo
		System.out.print("\tSexo* (M/F): ");
		res = in.nextLine();
		while ( !(res.equals("M") || res.equals("F")) ) {
			System.out.println("Error!! Valores admitidos M (masculino) o F (femenino).");
			res = in.nextLine();
		}
		if (res=="M") {
			introducirDato(true, "Masculino", 3, datosAtleta);
		}
		else { 
			introducirDato(true, "Femenino", 3, datosAtleta);
		}

		//Fecha de nacimiento
		//Suponemos que el usuario introduce una fecha correcta con el formato indicado!!!
		System.out.println("\tSu Fecha de nacimiento*:");
		System.out.print("\t\tDía (dd): ");
		res = in.nextLine();
		while (res.equals("")) {
			System.out.print("Campo obligatorio!");
			res= in.nextLine();
		}
		System.out.print("\t\tMes (mm): ");
		res = in.nextLine()+"-"+res;
		while (res.equals("")) {
			System.out.print("Campo obligatorio! ");
			res= in.nextLine();
		}
		System.out.print("\t\tAño (aaaa): ");
		res = in.nextLine()+"-"+res;
		while (res.equals("")) {
			System.out.print("Campo obligatorio! ");
			res= in.nextLine();
		}
		introducirDato(true, "#"+res+"#", 4, datosAtleta);

		//E-mail
		System.out.print("\tDirección de correo electrónico*: ");
		res = in.nextLine();
		introducirDato(true, res, 5, datosAtleta);

		//Teléfono
		System.out.print("\tNúmero de teléfono: ");
		res = in.nextLine();
		introducirDato(false, res, 6, datosAtleta);

		//Número de federado
		System.out.print("¿Está federado? (s/n) ");
		while ( !(res.equals("s") || res.equals("n")) ) {
			System.out.println("Escriba 's' o 'n'");
			res = in.nextLine();
		}
		if (res.equals("n")) {
			introducirDato(false, "", 7, datosAtleta);
		}
		else {
			System.out.print("\tIntroduzca su número de federado*: ");
			res = in.nextLine();
			introducirDato(true, res, 7, datosAtleta);
		}

		//Club
		System.out.print("\tNombre del club al que pertenece: ");
		res = in.nextLine();
		introducirDato(false, res, 8, datosAtleta);

		//Dirección
		System.out.print("\tDirección*: ");
		res = in.nextLine();
		introducirDato(true, res, 9, datosAtleta);

		//Localidad
		System.out.print("\tLocalidad*: ");
		res = in.nextLine();
		introducirDato(true, res, 10, datosAtleta);

		//Código postal
		System.out.print("\tCódigo postal*: ");
		res = in.nextLine();
		introducirDato(true, res, 11, datosAtleta);

		s.execute("SELECT * FROM Atleta WHERE (DNI='" + datosAtleta[0] + "')");
		//Si no se había registrado antes
		if ((s.getResultSet()).next()==false) {
			String nuevoAtleta = "INSERT INTO Atleta VALUES ( "
					+ "'"+datosAtleta[0]+"', "   //DNI
					+ "'"+datosAtleta[1]+"', "   //Nombre
					+ "'"+datosAtleta[2]+"', "   //Apellidos
					+ "'"+datosAtleta[3]+"', "   //Sexo
					+     datosAtleta[4]+", "    //Fecha de nacimiento
					+ "'"+datosAtleta[5]+"', "   //E-mail
					+     datosAtleta[6]+", "    //Teléfono
					+ "'"+datosAtleta[7]+"', "   //Número de federado
					+ "'"+datosAtleta[8]+"', "   //Club
					+ "'"+datosAtleta[9]+"', "   //Dirección
					+ "'"+datosAtleta[10]+"', "  //Localidad
					+     datosAtleta[11]+" )";  //Código postal
			s.execute(nuevoAtleta);
			s.execute("SELECT * FROM Atleta WHERE (DNI='" + datosAtleta[0] + "')");
			ResultSet atleta = s.getResultSet();
			return atleta;
		}
		//Si ya hay un atleta registrado en la BD con ese DNI
		else {
			System.out.println("Ya hay un atleta registrado con ese DNI!");
			return null;
		}
	}

	/**Método auxiliar para el registro en la BD de los datos de un atleta
	 * 
	 * @param obligatorio: true si el dato a insertar es obligatorio, false si es opcional
	 * @param dato: dato a insertar
	 * @param pos: índice de la columna en la que se insertará el dato
	 * @param datos: array de String donde se almacenan los datos temporalmente antes de hacer la inserción
	 */
	private static void introducirDato (boolean obligatorio, String dato, int pos, String[] datos) {
		if (obligatorio){
			while (dato.equals("")) {
				System.out.println("Campo obligatorio! Por favor introduzca el campo:");
				dato= in.nextLine();
			}
		}
		datos[pos]=dato;
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
								       "\t" + "Fecha inscripción: " + resultado.getDate(4) + " \n" +
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
							+ "WHERE (Inscripción.Id_competición = '" + CompeticionID + "' AND Inscripción.Id_inscripción='" + identificacion +"')");
					resultado = s.getResultSet();
					resultado.next();
					
					//Mostramos al atleta los datos de su consulta
					
					System.out.println("Tus datos para la competción " + CompeticionID.getString(1) + " son los siguientes:");
					System.out.println("\t" + "Nombre: "+ resultado.getString(1) + " \n" +
								       "\t" + "Apellidos: " + resultado.getString(2) + " \n" + 
								       "\t" + "Id_inscripción: " + resultado.getString(3) + " \n" +
								       "\t" + "Fecha inscripción: " + resultado.getDate(4) + " \n" +
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