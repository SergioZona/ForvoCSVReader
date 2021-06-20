package reader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import org.openqa.selenium.JavascriptExecutor;

/**
 * Clase que abre las páginas web a partir de un archivo .CSV en Forvo.
 * @author Sergio Julian Zona Moreno
 *
 * Consejos útiles y guía para Selenium: 
 * https://www.browserstack.com/guide/selenium-with-java-for-automated-test
 *
 */
public class ForvoPageOpen {

	/**
	 * Arreglo de Strings que guardará las palabras.
	 */
	private static ArrayList<String>arreglo;
	
	/**
	 * Arreglo de audios que será escrito en el .CSV.
	 */
	private static ArrayList<String>arregloAudios;
	
	/**
	 * Separador del sistema
	 */
	private static CsvFormat format;

	/**
	 * Método constructor de la clase que inicializa un arreglo de Strings y lo llena con los datos del CSV.
	 * @param columna Columna donde se encuentran los archivos buscados.
	 * @param archivo Archivo que será ejecutado.
	 * @param delimitador Delimitador predeterminado.
	 * @param idioma Idioma de las expresiones que serán descargadas.
	 * @param correo Correo electrónico de inicio de sesión.
	 * @param contrasena Contraseña de inicio de sesión.
	 * @param pathDownloads Directorio donde serán descargados los audios.
	 * @throws Exception Excepción en caso de que ocurra algún error.
	 */
	public ForvoPageOpen(int columna, File archivo, String idioma, String correo, String contrasena, String pathDownloads) throws Exception
	{
		arreglo = new ArrayList<String>();
		arregloAudios = new ArrayList<String>();
		leerCSV(columna, archivo);
		abrirPaginasNavegador(idioma, correo, contrasena, pathDownloads);
		escribirCSV(archivo);
	}	


	/**
	 * Método que lee el archivo .CSV. Note que se encuentra en UTF-8.
	 * @param pColumna Columna que será leída.
	 * @param archivo Archivo que será ejecutado.
	 * @throws Exception Excepción en caso de que ocurra algún error
	 */
	public void leerCSV(int pColumna, File archivo) throws Exception 
	{
		try 
		{
			FileInputStream fis = new FileInputStream(archivo);
			InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
			BufferedReader reader = new BufferedReader(isr);
			
			CsvParserSettings settings = new CsvParserSettings();
			settings.detectFormatAutomatically();

			CsvParser parser = new CsvParser(settings);
			List<String[]> rows = parser.parseAll(reader);
			Iterator<String[]> iterator = rows.iterator();
	        while (iterator.hasNext()) 
	        {
	            arreglo.add(iterator.next()[pColumna]);
	        }
			
	        format = parser.getDetectedFormat();
			
			reader.close();
			isr.close();
			fis.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que abre las páginas del navegador por defecto del sistema y descarga los audios en la ruta determinada.
	 * @param idioma Idioma de los audios que serán descargados.
	 * @param correo Correo electrónico de inicio de sesión.
	 * @param contrasena Contraseña de inicio de sesión.
	 * @param pathDownloads Directorio donde serán descargados los audios.
	 * @throws Exception En caso de que ocurra un error de ejecución.
	 */
	public void abrirPaginasNavegador(String idioma, String correo, String contrasena, String pathDownloads) throws Exception
	{
		WebDriver driver = configurarDriver(idioma, pathDownloads);        
		JavascriptExecutor js = (JavascriptExecutor) driver;  

		//Login: 
		login(correo, contrasena, driver, js);

		//Descargar audios:       
		for(int i=0; i<arreglo.size();++i)
		{
			String expresion = arreglo.get(i);
			driver.get("https://forvo.com/word/"+expresion+"/#"+idioma);
			Thread.sleep(5000);
			try
			{
				WebElement elemento = driver.findElements(By.xpath("//span[@class='ofLink statusGrey' and contains(@title, 'Download')]")).get(0);
				Thread.sleep(5000);

				//Se da click al elemento seleccionado. Debe implementarse el JavascriptExecutor, puesto que no funciona con .click() predeterminado de Selenium.
				js.executeScript("arguments[0].click()", elemento);
				Thread.sleep(5000);
				
				arregloAudios.add("[sound:pronunciation_"+idioma+"_"+expresion.toLowerCase()+".mp3]");
			}
			catch(Exception e)
			{
				System.out.println("La expresión: "+expresion+ " no fue encontrada en el buscador. Fila en el .CSV: "+Integer.toString(i+1));
				arregloAudios.add("");
			}
		}
		driver.close();
	} 

	/**
	 * Método que configura el WebDriver para la ejecución del programa. 
	 * @return WebDriver configurado.
	 */
	public WebDriver configurarDriver(String idioma, String pathDownloads)
	{
		LocalDateTime dateTime = LocalDateTime.now(); 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
		String hora = dateTime.format(formatter);
		
		//System.setProperty("webdriver.chrome.driver", "libs/chromedriver.exe");
		WebDriverManager.chromedriver().browserVersion("77.0.3865.40").setup();
		
		String nombreCarpeta = idioma+" "+hora;
		String downloadFilepath = pathDownloads+File.separator+nombreCarpeta;
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);
		chromePrefs.put("safebrowsing.enabled", "false");
		
		ChromeOptions options = new ChromeOptions();	
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("start-maximized");
		options.setExperimentalOption("useAutomationExtension", false);
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		
		WebDriver driver = new ChromeDriver(options);
		return driver;
	}

	/**
	 * Método que efectua login en la página de Forvo.
	 * @param pCorreo Correo de Forvo.
	 * @param pContrasena Contraseña de Forvo.
	 * @param driver Driver donde será ejecutado el loggeo.
	 * @param js Ejecutos de JavaScript que permitirá enviar el click.
	 * @throws InterruptedException En caso de que ocurra un error de ejecución.
	 */
	public void login(String pCorreo, String pContrasena, WebDriver driver, JavascriptExecutor js) throws Exception
	{
		driver.get("https://forvo.com/login/");
		driver.manage().window().maximize();
		WebElement correo = driver.findElements(By.xpath("//input[@name='login']")).get(0);
		WebElement contrasena = driver.findElements(By.xpath("//input[@name='password']")).get(0);
		WebElement confirmar = driver.findElements(By.xpath("//button[@type='submit' and contains(text(),'Enter')]")).get(0);

		Thread.sleep(2000);
		correo.sendKeys(pCorreo);
		Thread.sleep(2000);
		contrasena.sendKeys(pContrasena);
		Thread.sleep(2000);
		js.executeScript("arguments[0].click()", confirmar);		
		
		int intentos = 1;
		while(driver.findElements(By.xpath("//nav[@class='logged']")).isEmpty())
		{ 	
			if(intentos==5)
			{
				throw new Exception("No se pudo efectuar el Login correctamente.");
			}
			Thread.sleep(10000);
			driver.navigate().refresh();
			++intentos;			
		}
	}
	
	public void escribirCSV(File archivo)
	{
		char delimitador = format.getDelimiter();
		String saltoLinea = format.getLineSeparatorString();
		
	    String output = "";
	    try{
			FileInputStream fis = new FileInputStream(archivo);
			InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
	        BufferedReader br = new BufferedReader(isr);
	        String line = null;
	        int i = 0;
	        while ((line = br.readLine()) != null) {
	            output += line+delimitador+String.valueOf(arregloAudios.get(i))+saltoLinea;
	            i++;
	        }
	        br.close();
	        isr.close();
	        fis.close();
	        
	      //Parámetro falso para no realizar append, sino sobrescribir.
	        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(archivo, false), StandardCharsets.UTF_8); 
	                
	        writer.write(output);
	        writer.flush();
	        writer.close();
	    } 
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    } 
	}
}