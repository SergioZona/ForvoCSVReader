package Solucion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;

/**
 * Clase que abre las páginas web a partir de un archivo .CSV en Forvo.
 * @author Sergio Julian Zona Moreno
 *
 */
public class ForvoPageOpen {

	/**
	 * Arreglo de Strings que guardará las palabras.
	 */
	private static ArrayList<String>arreglo;

	/**
	 * Método constructor de la clase que inicializa un arreglo de Strings y lo llena con los datos del CSV.
	 * @param columna Columna donde se encuentran los archivos buscados.
	 * @param archivo Archivo que será ejecutado.
	 * @param delimitador Delimitador predeterminado.
	 * @throws Exception Excepción en caso de que ocurra algún error.
	 */
	public ForvoPageOpen(int columna, File archivo, String delimitador) throws Exception
	{
		arreglo = new ArrayList<String>();
		leerCSV(columna, archivo, delimitador);
		abrirPaginasNavegador();
	}	


	/**
	 * Método que lee el archivo .CSV. Note que se encuentra en UTF-8.
	 * @param pColumna Columna que será leída.
	 * @param archivo Archivo que será ejecutado.
	 * @param delimitador Delimitador del sistema.
	 * @throws Exception Excepción en caso de que ocurra algún error
	 */
	public void leerCSV(int pColumna, File archivo, String delimitador) throws Exception 
	{
		try 
		{
			FileInputStream fis = new FileInputStream(archivo);
			InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
			BufferedReader reader = new BufferedReader(isr);
			int columna = pColumna;
			String str;
			while ((str = reader.readLine()) != null) 
			{
				String[] data = str.split(";");
				arreglo.add(data[pColumna]);
			}

		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que abre las páginas del navegador por defecto del sistema. 
	 */
	public void abrirPaginasNavegador()
	{
		if (java.awt.Desktop.isDesktopSupported()) {
			java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

			if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
				try {
					for(int i=0; i<arreglo.size();++i)
					{
						String expresion = arreglo.get(i);
						java.net.URI uri = new java.net.URI("https://forvo.com/word/"+expresion+"/#ja");
						desktop.browse(uri);
					}
					
				} 
				catch (Exception e) 
				{
					
				}
			}
		}
	}
	
	/**
	 * Método que carga la configuración ubicada en la carpeta %appdata% del computador. En caso de que no exista la crea y guarda la configuración utilizada actualmente.
	 */
	public void cargarConfiguracion()
	{

	}
	
}