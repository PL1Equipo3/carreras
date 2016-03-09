import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

			System.out.println("Bienvenido!");
			String opcion;	
			do{
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
						System.out.println("Felicidades se ha inscrito con �xito!\n");
					else
						System.out.println("Error en la incripci�n!\n");
					break;
					//COSULTAR EL ESTADO DE MI INSCRIPCI�N
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
					System.out.println("Opci�n no v�lida.\n");
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
			if (rs.next()==false) {
				System.out.println("Error en la identificaci�n!");
				//Volver al flujo principal del programa
				return false;
			}
			else {
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
			if(competicion.next()==false){
				System.out.println("No ha introducido correctamente el ID de la inscripci�n");
				return false;
			}				
			//else
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
			s.execute("SELECT * FROM Inscripci�n WHERE (Id_atleta='" + atleta.getString(1) +"' AND Id_competici�n='" + comp + "')");
			if ((s.getResultSet()).next()) {
				System.out.println("Usted ya est� inscrito en esta competici�n");
				return false;
			}

			//Calcular la categor�a a la que pertenece el atleta
			//Par�metros: String Id_Inscripcion, String Id_competicion, String Id_atleta
			String categoria = CalculaCategoria(atleta.getString(1) + "-" + competicion.getString(1), competicion.getString(1), atleta.getString(1), atleta.getDate(5), atleta.getString(4));

			//Calcular la cuota a pagar
			//Par�metros: String Id_Inscripcion, String Id_competicion, String Id_atleta
			float cuota = Pagar(atleta.getString(1)+"-"+competicion.getString(1), competicion.getString(1), atleta.getString(1), fechaSQL);

			//Si est� dentro de plazo, hay plazas disponibles y el atleta no est� ya inscrito, registrar la inscripci�n
			s.execute("INSERT INTO Inscripci�n VALUES ('"
					+ atleta.getString(1) + "-" + competicion.getString(1) + "', "  //Id_inscripci�n
					+ "#" + fechaSistema + "#, "									//Fecha de inscripci�n
					+ "'Preinscrito', "												//Estado
					+ cuota + ", "													//Cuota
					+ "'Tranferencia', "											//Forma de pago
					+ "'" + categoria + "', "										//Categoria
					+ "'" + atleta.getString(1) + "', "								//Id_atleta
					+ "'" + competicion.getString(1) + "')" );						//Id_competici�n


			s.execute("SELECT * FROM Inscripci�n WHERE Id_inscripci�n='"+atleta.getString(1) + "-" + competicion.getString(1)+"'");
			ResultSet inscripcion = s.getResultSet();
			inscripcion.next();			

			System.out.println("\tId de inscripci�n: " + inscripcion.getString(1) + "\n"
					+"\tCategor�a: " + inscripcion.getString(6) + "\n"
					+"\tCuota a pagar: " + inscripcion.getFloat(4) +"�" );

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaActual); // Configuramos la fecha que se recibe
			calendar.add(Calendar.DAY_OF_YEAR, 2);
			System.out.println("\tTiene 2 d�as para realizar la transferencia (hasta el "+formateador.format(calendar.getTime())+".)");

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
		if (res.equals("M")) {
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
		System.out.print("\tN�mero de tel�fono*: ");
		res = in.nextLine();
		introducirDato(true, res, 6, datosAtleta);

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


	private static float Pagar(String Id_inscripci�n, String Id_competici�n, String Id_atleta, Date fechaInscripcion) throws Exception{  

		float Cuota_abonada = 0;
		String orden;
		Date  fechaInicio, fechaFin;
		ResultSet rs;

		/*
			//Actualiza el estado de la inscripci�n a Preinscrito
			orden = "UPDATE Inscripci�n SET Estado = 'Preinscrito' WHERE "
					+ "(Id_inscripci�n = '"+Id_inscripci�n+"')";

			s.execute(orden);


			//Actualiza la forma de pago de la inscripci�n a Transferencia
			orden = "UPDATE Inscripci�n SET [Forma de pago] = 'Transferencia' WHERE "
					+ "(Id_inscripci�n = '"+Id_inscripci�n+"')";

			s.execute(orden);


			//Obtener la fecha de inscripci�n
			orden = "SELECT [Fecha inscripci�n] FROM Inscripci�n WHERE "
					+ "(Id_inscripci�n = '"+Id_inscripci�n+"')";

			rs=s.executeQuery(orden);
		    fechaInscripcion=rs.getDate(1);
		 */

		//Modificar el precio seg�n el plazo en el que se realiza la inscripci�n
		//----------------------------------------------------------------------

		//Obtener las fechas de los plazos
		orden = "SELECT * FROM Plazos WHERE (Id_competici�n = '"+Id_competici�n+"')";
		rs=s.executeQuery(orden);
		rs.next();

		fechaInicio=rs.getDate(2);
		fechaFin=rs.getDate(3);
		//System.out.println(fechaInicio);
		//System.out.println(fechaFin);
		
		String sInicio = (String) (fechaInicio.toString()).subSequence(0, 9);
		String sFin = (String) (fechaFin.toString()).subSequence(0, 9);
		String sInscripcion = (String) (fechaInscripcion.toString()).subSequence(0, 9);
		

		boolean cond = !( (fechaInicio.before(fechaInscripcion) || sInicio.equals(sInscripcion)) 
				&& (fechaFin.after(fechaInscripcion) || sFin.equals(sInscripcion)) );

		//Comprobar en que plazo se realiz� la inscripci�n
		while( cond ) {
			rs.next();
			fechaInicio=rs.getDate(2);
			fechaFin=rs.getDate(3);

			cond = !( (fechaInicio.before(fechaInscripcion) || sInicio.equals(sInscripcion)) 
					&& (fechaFin.after(fechaInscripcion) || sFin.equals(sInscripcion)) );
		}
		
		Cuota_abonada = rs.getFloat(4);
		//System.out.println(Cuota_abonada);

		//Modificar el precio seg�n si est� federado o no
		orden = "SELECT Tipo FROM Competici�n WHERE (Id_competici�n = '"+Id_competici�n+"')";
		rs=s.executeQuery(orden);
		rs.next();
		if((rs.getString(1)).equals("Monta�a")){
			orden = "SELECT [N�mero federado] FROM Atleta WHERE (DNI = '"+Id_atleta+"')";
			rs=s.executeQuery(orden);
			rs.next();
			if(!rs.getString(1).equals("")){
				Cuota_abonada=Cuota_abonada-15;	
			}
		}

		//Actualiza el precio en la Cuota abonada
		/*orden = "UPDATE Inscripci�n SET [Cuota abonada] = "+Cuota_abonada+" WHERE (Id_inscripci�n = '"+Id_inscripci�n+"')";
			s.execute(orden);
			System.out.println(Cuota_abonada);
		 */
		return Cuota_abonada;
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
							"\t" + "Fecha inscripci�n: " + resultado.getDate(4) + " \n" +
							"\t" + "Estado: " + resultado.getString(5) + " \n"+
							"\t" + "Cuota abonada: " + resultado.getFloat(6) + " \n" +
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
							+ "WHERE (Inscripci�n.Id_competici�n = '" + CompeticionID.getString(1) + "' AND Inscripci�n.Id_inscripci�n='" + identificacion + "')");
					resultado = s.getResultSet();
					resultado.next();

					//Mostramos al atleta los datos de su consulta

					System.out.println("Tus datos para la competci�n " + CompeticionID.getString(1) + " son los siguientes:");
					System.out.println("\t" + "Nombre: "+ resultado.getString(1) + " \n" +
							"\t" + "Apellidos: " + resultado.getString(2) + " \n" + 
							"\t" + "Id_inscripci�n: " + resultado.getString(3) + " \n" +
							"\t" + "Fecha inscripci�n: " + resultado.getDate(4) + " \n" +
							"\t" + "Estado: " + resultado.getString(5) + " \n"+
							"\t" + "Cuota abonada: " + resultado.getFloat(6) + " \n" +
							"\t" + "Forma de pago: " + resultado.getString(7) + " \n" +
							"\t" + "Categor�a: " + resultado.getString(8) );


					return true;


				}

			}
		}

	}


	private static String CalculaCategoria (String Id_Inscripcion, String Id_competicion, String Id_atleta, Date FechaNac, String sexo) throws Exception{			
		//Variables y ED
		String SQL, FechaNC = null, FechaN = null, C=null;
		ArrayList<String> Categorias = new ArrayList<String>();
		ArrayList<Integer> edadMin = new ArrayList<Integer>();
		ArrayList<Integer> edadMax = new ArrayList<Integer>();
		int Cont=0, varEdadMin, varEdadMax;		


		//Objeto SimpleDateFormat para pasar de String a Date
		//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		/*
		//Seleccionar corredorde la BD
		SQL= "SELECT * FROM Atleta";	
		s.execute(SQL);
		ResultSet rs = s.getResultSet();
		while((rs!=null) && (rs.next())){
			FechaNC=rs.getString(5);   //El 5 porque es donde esta la fecha de nacimiento
			sexo=rs.getString(4);      //En el 4 est� el sexo
		}		
		*/

		//FechaN=FechaNC.substring(0, 10);						//Cortar String			
		//java.util.Date FechaNac = formatter.parse(FechaN);		//Pasar String a Date			
		int Edad = CalculaEdad(FechaNac);						//Calcular Edad

		//Consulta SQL para saber las categor�as 
		SQL="SELECT * FROM Categor�a INNER JOIN [Categor�as de la competici�n] ON Categor�a.Id_categor�a=[Categor�as de la competici�n].Id_categor�a WHERE Categor�a.Sexo='" + sexo + "' AND [Categor�as de la competici�n].Id_competici�n='" + Id_competicion +"'";
		s.execute(SQL);
		ResultSet rs = s.getResultSet();
		while((rs!=null) && (rs.next())){			//Almacenar categor�as y edades
			Categorias.add(rs.getString(2));
			edadMin.add(rs.getInt(3));
			edadMax.add(rs.getInt(4));
			Cont++;
		}

		//Calcular en que competici�n est�
		for (int i=0; i<Cont; i++){
			varEdadMin=edadMin.get(i);
			varEdadMax=edadMax.get(i);
			if (varEdadMax == 0) varEdadMax=200;	//Control del valor m�x de la �ltima categor�a que es 0
			if ((Edad>=varEdadMin) && (Edad<=varEdadMax)){
				C=Categorias.get(i);
			}
		}

		//Escribirla en la BD
		/*
			SQL="UPDATE Inscripci�n SET Categor�a='" + C +"' WHERE Id_Inscripci�n='" + Id_Inscripcion + "'";
			s.execute(SQL);
		 */
		return C;

	}

	private static int CalculaEdad(java.util.Date fechaNac){
		int Edad = 0;
		Calendar dob = Calendar.getInstance();
		dob.setTime(fechaNac);
		Calendar today = Calendar.getInstance();
		Edad = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		if (today.get(Calendar.DAY_OF_YEAR) <= dob.get(Calendar.DAY_OF_YEAR))
			Edad--;
		return Edad;
	}
}
