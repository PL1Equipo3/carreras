import java.sql.*;
import java.text.SimpleDateFormat;

import net.ucanaccess.*;

import java.util.Calendar;
import java.util.Scanner; 

public class PlataformaAtleta {	
	static Scanner in = new Scanner(System.in);
	static Statement s;

	public static void main(String[] args){
		try{
			boolean exito;
			//Conexi�n a la BD
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			//Ruta absoluta o relativa como parámetro de getConnection
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://Carreras.accdb");
			s = conn.createStatement();

			String opcion;	
			do{
				System.out.println("Bienvenido!");
				System.out.println("�Qu� desea hacer?\n"
						+ "\t(1) INSCRIBIRME EN UNA CARRERA\n"
						+ "\t(2) COSULTAR EL ESTADO DE MI INSCRIPCI�N\n"
						+ "\t(0) SALIR");
				opcion = in.nextLine();
				switch(opcion) {
				//INSCRIBIRME EN UNA CARRERA
				case "1":
					exito = inscripcion(); 
					if (exito)
						System.out.println("Felicidades se ha inscrito con �xito!");
					else
						System.out.println("Error en la incripci�n!");
					break;
				//COSULTAR EL ESTADO DE MI INSCRIPCI�N
				case "2":
					
					break;
				//SALIR
				case "0":
					System.out.println("Hasta pronto!");
					break;
				//OTRO
				default:
					System.out.println("Opci�n no v�lida.");
				}
			} while ( !opcion.equals("0") );
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**M�todo para la inscripci�n de un atleta en una competici�n
	 * 
	 * @return true si se ha realizado la inscripci�n correctamente
	 * 			false si ha habido alg�n error y no se ha podido completar
	 * 				-no se ha podido identificar/registrar al atleta
	 * 				-no hay plazas disponibles
	 * 				-inscripci�n fuera de plazo
	 * @throws Exception
	 */
	private static boolean inscripcion() throws Exception {
		String res;
		ResultSet rs;
		do{
			System.out.println("Es la primera vez que se inscribe en una competici�n? (s/n)");
			res = in.nextLine();
			if ( !(res.equals("s") || res.equals("n")) ) {
				System.out.println("Escriba 's' o 'n'");
			}
		} while ( !(res.equals("s") || res.equals("n")) );

		//Primera vez que se inscribe en una competici�n
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
				System.out.println("Error en la identificaci�n!");
				//Volver al flujo principal del programa
				return false;
			}
			else {
				rs.next();
				//rs contiene los datos personales del atleta que se va a inscribir
				System.out.println(rs.getString(1) + " : " + rs.getString(2));
			}
		}
		return nuevaInscripci�n(rs);
	}

	/**Inscripci�n de un atleta en una competici�n
	 * 
	 * @return true true si se ha realizado la inscripci�n correctamente
	 * 			false si ha habido alg�n error y no se ha podido completar
	 * 				-no se ha podido identificar/registrar al atleta
	 * 				-no hay plazas disponibles
	 * 				-inscripci�n fuera de plazo
	 * @throws Exception
	 */
	private static boolean nuevaInscripci�n (ResultSet atleta) throws Exception{
		String comp;
		s.execute("SELECT Nombre, Id_competici�n FROM Competici�n");
		//El ResultSet competicion contiene el nombre e id de todas las competiciones registradas en la BD
		ResultSet competicion = s.getResultSet();
		//Comprobar que hay alguna competici�n en la BD
		if (competicion.next()!=false) {
			System.out.println("Seleccione la carrera a la que desea inscribirse:");
			//Mostrar todas las competiciones registradas en la BD
			System.out.println("\t" + competicion.getString(2) );
			while((competicion.next()))
			{
				System.out.println("\t" + competicion.getString(2) );
			}
			//El usuario debe introducir por teclado el id de la competici�n seleccionada!!!!
			comp = in.nextLine();
			s.execute("SELECT * FROM Competici�n WHERE (Id_competici�n='" + comp +"')");
			competicion = s.getResultSet();
			competicion.next();
			//competicion contiene los datos de la competici�n seleccionada
			
			
			
			/* COMPROBAR QUE EST� DENTRO DE PLAZO */
			s.execute("SELECT MIN([Fecha inicio]), MAX([Fecha fin]) FROM Plazos WHERE (Id_competici�n='" + comp +"')");
			ResultSet fechas = s.getResultSet();
			fechas.next();
			
			//Conseguimos la fecha actual a partir de la hora del sistema
			java.util.Date fechaActual = (Calendar.getInstance()).getTime();
			//Formateador para dar el formato que reguiere sql
			SimpleDateFormat formateador = new SimpleDateFormat("yyy-MM-dd");
	        String fechaSistema=formateador.format(fechaActual);
	        
	        /*Comprobaciones:
	        	System.out.println("Actual: "+fechaSistema);	        
	        	System.out.println("M�nimo: "+formateador.format(fechas.getDate(1)));
	        	System.out.println("M�ximo: "+formateador.format(fechas.getDate(2)));
	        */
	        
	        //Pasar las fechas a tipo java.sql.Date para poder compararlas
	        java.sql.Date fechaSQL = new java.sql.Date(fechaActual.getTime());
	        java.sql.Date fechaMin = new java.sql.Date((fechas.getDate(1)).getTime());
	        java.sql.Date fechaMax = new java.sql.Date((fechas.getDate(2)).getTime());
	        //Si la fecha actual no est� entre la fecha de comienzo del primer plazo de inscripci�n 
	        //para esta competici�n y la de fin del �ltimo plazo, no se permite realizar la inscripci�n
	        if ( !(fechaSQL.after(fechaMin) && fechaSQL.before(fechaMax)) ) {
	        	System.out.println("Fuera de plazo");
	        	return false;
	        }
			
	        
	        
	        /* COMPROBAR QUE HAY PLAZAS LIBRES */
	        s.execute("SELECT COUNT(*) FROM Inscripci�n WHERE (Id_competici�n='" + comp +"')");
			ResultSet inscritos = s.getResultSet();
			inscritos.next();
			//Si el n�mero de atletas inscritos es igual (o mayor) al de plazas disponibles es que ya no hay m�s
				//inscritos.getInt(1)=n�mero de inscripciones registradas en la BD para esa competici�n
				//competicion.getInt(4)=n�mero m�ximo de plazas disponibles para la competici�n seleccionada
			if (inscritos.getInt(1)>=competicion.getInt(4)) {
				System.out.println("Plazas agotadas");
				return false;
			}
	        
			
			
			/* COMPROBAR QUE EL ATLETA NO EST� YA INSCRITO EN LA COMPETICI�N */
			s.execute("SELECT * FROM Inscripci�n WHERE (Id_atleta='" + atleta.getString(1) +"')");
	        if ((s.getResultSet()).next()) {
	        	System.out.println("Usted ya est� inscrito en esta competici�n");
	        	return false;
	        }
	        	
	        
			 
	        //Si est� dentro de plazo, hay plazas disponibles y el atleta no est� ya inscrito, registrar la inscripci�n
			s.execute("INSERT INTO Inscripci�n (Id_inscripci�n, [Fecha inscripci�n], Id_atleta, Id_competici�n) VALUES ('"
					+ atleta.getString(1) + "-" + competicion.getString(1) + "', "
					+ "#" + fechaSistema + "#, "
					+ "'" + atleta.getString(1) + "', "
					+ "'" + competicion.getString(1) + "')" );
			return true;
		}
		//No hay ninguna competici�n registrada en la BD
		else {
			System.out.println("No hay competiciones abiertas en estos momentos.");
			return false;
		}
	}
	
	/**Registro de un nuevo atleta en la BD
	 * 
	 * @return ResultSet con el resultado de la inserci�n de los datos personales del atleta
	 * 			Contendr� todos los datos del atleta si el registro se complet� correctamente
	 * 			Ser� null si hubo alg�n problema y no se pudo completar el registro
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
		System.out.print("\t\tD�a (dd): ");
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
		System.out.print("\t\tA�o (aaaa): ");
		res = in.nextLine()+"-"+res;
		while (res.equals("")) {
			System.out.print("Campo obligatorio! ");
			res= in.nextLine();
		}
		introducirDato(true, "#"+res+"#", 4, datosAtleta);

		//E-mail
		System.out.print("\tDirecci�n de correo electr�nico*: ");
		res = in.nextLine();
		introducirDato(true, res, 5, datosAtleta);

		//Tel�fono
		System.out.print("\tN�mero de tel�fono: ");
		res = in.nextLine();
		introducirDato(false, res, 6, datosAtleta);

		//N�mero de federado
		System.out.print("�Est� federado? (s/n) ");
		while ( !(res.equals("s") || res.equals("n")) ) {
			System.out.println("Escriba 's' o 'n'");
			res = in.nextLine();
		}
		if (res.equals("n")) {
			introducirDato(false, "", 7, datosAtleta);
		}
		else {
			System.out.print("\tIntroduzca su n�mero de federado*: ");
			res = in.nextLine();
			introducirDato(true, res, 7, datosAtleta);
		}

		//Club
		System.out.print("\tNombre del club al que pertenece: ");
		res = in.nextLine();
		introducirDato(false, res, 8, datosAtleta);

		//Direcci�n
		System.out.print("\tDirecci�n*: ");
		res = in.nextLine();
		introducirDato(true, res, 9, datosAtleta);

		//Localidad
		System.out.print("\tLocalidad*: ");
		res = in.nextLine();
		introducirDato(true, res, 10, datosAtleta);

		//C�digo postal
		System.out.print("\tC�digo postal*: ");
		res = in.nextLine();
		introducirDato(true, res, 11, datosAtleta);

		s.execute("SELECT * FROM Atleta WHERE (DNI='" + datosAtleta[0] + "')");
		//Si no se hab�a registrado antes
		if ((s.getResultSet()).next()==false) {
			String nuevoAtleta = "INSERT INTO Atleta VALUES ( "
					+ "'"+datosAtleta[0]+"', "   //DNI
					+ "'"+datosAtleta[1]+"', "   //Nombre
					+ "'"+datosAtleta[2]+"', "   //Apellidos
					+ "'"+datosAtleta[3]+"', "   //Sexo
					+     datosAtleta[4]+", "    //Fecha de nacimiento
					+ "'"+datosAtleta[5]+"', "   //E-mail
					+     datosAtleta[6]+", "    //Tel�fono
					+ "'"+datosAtleta[7]+"', "   //N�mero de federado
					+ "'"+datosAtleta[8]+"', "   //Club
					+ "'"+datosAtleta[9]+"', "   //Direcci�n
					+ "'"+datosAtleta[10]+"', "  //Localidad
					+     datosAtleta[11]+" )";  //C�digo postal
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

	/**M�todo auxiliar para el registro en la BD de los datos de un atleta
	 * 
	 * @param obligatorio: true si el dato a insertar es obligatorio, false si es opcional
	 * @param dato: dato a insertar
	 * @param pos: �ndice de la columna en la que se insertar� el dato
	 * @param datos: array de String donde se almacenan los datos temporalmente antes de hacer la inserci�n
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
}
