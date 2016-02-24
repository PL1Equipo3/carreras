import java.sql.*;
import java.util.Scanner; 

public class PlataformaAtleta {	
	static Scanner in = new Scanner(System.in);

	public static void main(String[] args){
		try{
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
					inscripcion(); break;
				case "2":
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

	private static void inscripcion(){
		String res;
		do{
			System.out.println("Es la pimera vez que se inscribe en una competición? (s/n)");
			res = in.nextLine();
			if ( !(res.equals("s") || res.equals("n")) ) {
				System.out.println("Escriba 's' o 'n'");
			}
		} while ( !(res.equals("s") || res.equals("n")) );
		if ( res.equals("s") ) {
			
		}
		else {

		}
	}

}
