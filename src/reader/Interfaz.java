package reader;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Dimension;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;

public class Interfaz {

	/**
	 * Atributos de la clase.
	 */
	private JFrame frmForvotabloader;
	private static File archivo;
	private static File descargas;
	private static int columna;
	private static String idioma;
	private static String correo;
	private static String contrasena;
	private static String pathDownloads;
	private static Preferences configuracion;
	private JLabel lblPath;	
	private JLabel lblPathDownloads;	
	private JTextField textCorreo;
	private JPasswordField passwordContrasena;

	/**
	 * Launch the application.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception 
	{
		configuracion = Preferences.userNodeForPackage(Interfaz.class);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interfaz window = new Interfaz();
					window.frmForvotabloader.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
	}

	/**
	 * Create the application.
	 */
	public Interfaz() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private void initialize() {
		frmForvotabloader = new JFrame();
		frmForvotabloader.setResizable(false);
		frmForvotabloader.setTitle("ForvoCSVReader");
		frmForvotabloader.setBounds(100, 100, 700, 320);
		frmForvotabloader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frmForvotabloader.setLocation(dim.width/2-frmForvotabloader.getSize().width/2, dim.height/2-frmForvotabloader.getSize().height/2);
		frmForvotabloader.getContentPane().setLayout(null);

		//Primera fila
		final JButton btnDireccionArchivo = new JButton("Archivo .CSV");
		btnDireccionArchivo.setBounds(170, 11, 170, 23);
		btnDireccionArchivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser selectorArchivos = new JFileChooser();
				selectorArchivos.setFileSelectionMode(JFileChooser.FILES_ONLY);

				String pathGuardado = configuracion.get("FORVO_PATH", "");
				if(!pathGuardado.equals("")) 
				{
					File workingDirectory = new File(pathGuardado);
					selectorArchivos.setCurrentDirectory(workingDirectory);
					selectorArchivos.setSelectedFile(workingDirectory);
				}

				// Indica cual fue la accion de usuario sobre el JFilechooser
				int resultado = selectorArchivos.showOpenDialog(btnDireccionArchivo);
				if (resultado == JFileChooser.APPROVE_OPTION)
				{
					archivo = selectorArchivos.getSelectedFile();
					configuracion.put("FORVO_PATH", archivo.getAbsolutePath());
					lblPath.setText(archivo.getName());  
				}
			}
		});		
		frmForvotabloader.getContentPane().add(btnDireccionArchivo);

		lblPath = new JLabel("No se ha seleccionado");
		lblPath.setBounds(350, 15, 151, 14);
		lblPath.setHorizontalAlignment(SwingConstants.CENTER);
		frmForvotabloader.getContentPane().add(lblPath);

		//Segunda fila
		JButton btnDescargas = new JButton("Descarga de audios");
		btnDescargas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser selectorArchivos = new JFileChooser();
				selectorArchivos.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				String pathGuardado = configuracion.get("FORVO_PATH_DOWNLOADS", "");
				if(!pathGuardado.equals("")) 
				{
					File workingDirectory = new File(pathGuardado);
					selectorArchivos.setCurrentDirectory(workingDirectory);
					selectorArchivos.setSelectedFile(workingDirectory);
				}

				// Indica cual fue la accion de usuario sobre el JFilechooser
				int resultado = selectorArchivos.showOpenDialog(btnDireccionArchivo);
				if (resultado == JFileChooser.APPROVE_OPTION)
				{
					descargas = selectorArchivos.getSelectedFile();
					configuracion.put("FORVO_PATH_DOWNLOADS", descargas.getAbsolutePath());
					lblPathDownloads.setText(descargas.getName());  
				}
			}
		});
		btnDescargas.setBounds(170, 45, 170, 23);
		frmForvotabloader.getContentPane().add(btnDescargas);

		lblPathDownloads = new JLabel("No se ha seleccionado");
		lblPathDownloads.setHorizontalAlignment(SwingConstants.CENTER);
		lblPathDownloads.setBounds(350, 49, 151, 14);
		
		//Trae configuración del directorio de descargas
		String pathGuardado = configuracion.get("FORVO_PATH_DOWNLOADS", "");
		if(!pathGuardado.equals("")) 
		{
			descargas = new File(pathGuardado);
			lblPathDownloads.setText(descargas.getName());  
		}
		frmForvotabloader.getContentPane().add(lblPathDownloads);

		//Tercera fila
		JLabel ingreseNumeroColumna = new JLabel("Ingrese el n\u00FAmero de la columna donde se encuentra la expresi\u00F3n (por ejemplo: 2):");
		ingreseNumeroColumna.setBounds(10, 85, 517, 14);
		ingreseNumeroColumna.setHorizontalAlignment(SwingConstants.CENTER);
		frmForvotabloader.getContentPane().add(ingreseNumeroColumna);

		SpinnerModel model = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
		JSpinner spinnerColumna = new JSpinner(model);
		spinnerColumna.setBounds(525, 82, 127, 20);
		frmForvotabloader.getContentPane().add(spinnerColumna);

		//Trae configuración del JSpinner.		
		String columnaGuardada = configuracion.get("FORVO_COLUMNA", "");
		if(!columnaGuardada.equals("")) 
		{
			spinnerColumna.setValue(Integer.parseInt(columnaGuardada));
		}

		//Cuarta fila
		JLabel lblIdioma = new JLabel("Por favor seleccione el idioma:");
		lblIdioma.setBounds(170, 125, 257, 14);
		frmForvotabloader.getContentPane().add(lblIdioma);

		JComboBox comboIdioma = new JComboBox();
		comboIdioma.setBounds(370, 122, 144, 20);
		comboIdioma.setModel(new DefaultComboBoxModel(new String[] {"Abaza", "Abkhazian", "Abua", "Adygean", "Afar", "Afrikaans", "Aghul", "Ainu", "Akan", "Albanian", "Aleut", "Algerian Arabic", "Algonquin", "Alutiiq", "Amharic", "Ancient Greek", "Arabic", "Aragonese", "Arapaho", "Arb\u00EBresh", "Armenian", "Aromanian", "Assamese", "Assyrian Neo-Aramaic", "Asturian", "Avaric", "Aymara", "Azerbaijani", "Bakhtiari", "Balochi", "Bambara", "Bardi", "Bashkir", "Basque", "Bavarian", "Belarusian", "Bemba", "Bench", "Bengali", "Bhojpuri", "Biblical Hebrew", "Bihari", "Bislama", "Bitterroot Salish", "Bosnian", "Botlikh", "Bouyei", "Breton", "Bribri", "Bulgarian", "Burmese", "Burushaski", "Buryat", "Campidanese", "Cantonese", "Cape Verdean Creole", "Catalan", "Cayuga", "Cebuano", "Central Atlas Tamazight", "Central Bikol", "Chamorro", "Changzhou", "Chechen", "Cherokee", "Chichewa", "Chickasaw", "Chinook Jargon", "Choctaw", "Chuvash", "Cook Islands Maori", "Coptic", "Cornish", "Corsican", "Cree", "Creek", "Crimean Tatar", "Croatian", "Cypriot Arabic", "Czech", "Dagbani", "Dangme", "Danish", "Dari", "Dinka", "Divehi", "Doumen Hua", "Dusun", "Dutch", "Dzongkha", "Edo", "Egyptian Arabic", "Ekpeye", "Emilian", "English", "Erzya", "Esperanto", "Estonian", "Eton", "Etruscan", "Evenki", "Ewe", "Ewondo", "Faroese", "Fiji Hindi", "Fijian", "Finnish", "Flemish", "Franco-Proven\u00E7al", "French", "Frisian", "Friulan", "Fulah", "Fuzhou", "Ga", "Gagauz", "Galician", "Gan Chinese", "Georgian", "German", "Gilaki", "Gilbertese", "Greek", "Guarani", "Gujarati", "Gulf Arabic", "Gusii", "Haitian Creole", "Hakka", "Hassaniyya", "Hausa", "Hawaiian", "Hebrew", "Herero", "Hiligaynon", "Hindi", "Hiri motu", "Hmong", "Hopi", "Hungarian", "Icelandic", "Ido", "Igbo", "Iloko", "Indonesian", "Ingush", "Interlingua", "Inuktitut", "Inupiaq", "Irish", "Italian", "Iwaidja", "Jamaican Patois", "Japanese", "Javanese", "Jeju", "Jiaoliao Mandarin", "Jilu Mandarin", "Jin Chinese", "Judeo-Spanish", "Kabardian", "Kabyle", "Kalaallisut", "Kalenjin", "Kalmyk", "Kannada", "Kanuri", "Karachay-Balkar", "Karakalpak", "Karelian", "Kashmiri", "Kashubian", "Kazakh", "Khanty", "Khasi", "Khmer", "Kikuyu", "Kimbundu", "Kinyarwanda", "Kirundi", "Klingon", "Komi", "Komi-Permyak", "Kongo", "Konkani", "Korean", "Kotava", "Krio", "Kuanyama", "Kurdish", "Kurmanji", "Kutchi", "Kyrgyz", "K\u02BCiche\u02BC", "Ladin", "Lak", "Laki", "Lakota", "Lao", "Latgalian", "Latin", "Latvian", "Laz", "Lezgian", "Ligurian", "Limburgish", "Lingala", "Lithuanian", "Lojban", "Lombard", "Louisiana Creole", "Low German", "Lower Yangtze Mandarin", "Lozi", "Luba-katanga", "Luganda", "Luo", "Lushootseed", "Luxembourgish", "Macedonian", "Magahi", "Mahasu Pahari", "Mainfr\u00E4nkisch", "Malagasy", "Malay", "Malayalam", "Malaysian", "Maltese", "Manchu", "Mandarin Chinese", "Mandinka", "Mansi", "Manx", "Maore", "M\u0101ori", "Mapudungun", "Marathi", "Mari", "Marshallese", "Masbate\u00F1o", "Mauritian Creole", "Mayan languages", "Mazandarani", "Mbe", "Meitei", "Mennonite Low German", "Mescalero-Chiricahua", "Mesopotamian Arabic", "Michif", "Micmac", "Middle Chinese", "Middle English", "Min Dong", "Min Nan", "Minangkabau", "Mingrelian", "Minjaee Luri", "Mohawk", "Moksha", "Moldovan", "Mongolian", "Moroccan Arabic", "Mossi", "Mwali", "Mwanga", "Nahuatl", "Naskapi", "Nauru", "Navajo", "Naxi", "Ndonga", "Ndzwani", "Neapolitan", "Nepal Bhasa", "Nepali", "Ngazidja", "Nheengatu", "Nogai", "North Levantine Arabic", "North Ndebele", "Northern Sami", "Norwegian", "Norwegian Nynorsk", "Nuosu", "N\u01C0uu", "Obulom", "Occitan", "Ogbia", "Ojibwa", "Okinawan", "Old English", "Old Norse", "Old Turkic", "Oriya", "Oromo", "Osage", "Ossetian", "Ottoman Turkish", "Palauan", "Palenquero", "Pali", "Pampangan", "Pangasinan", "Papiamento", "Pashto", "Pawnee", "Pennsylvania Dutch", "Persian", "Picard", "Piedmontese", "Pitjantjatjara", "Polish", "Portuguese", "Potawatomi", "Pu-Xian Min", "Pulaar", "Punjabi", "Quechua", "Quenya", "Quiatoni Zapotec", "Rapa Nui", "Reunionese Creole", "Rohingya", "Romagnol", "Romani", "Romanian", "Romansh", "Rukiga", "Russian", "Rusyn", "S'gaw Karen", "Samoan", "Sango", "Sanskrit", "Saraiki", "Sardinian", "Scots", "Scottish Gaelic", "Seediq", "Serbian", "Serer", "Shan", "Shanghainese", "Shilha", "Shona", "Shoshone", "Siberian Tatar", "Sicilian", "Silesian", "Silesian German", "Sindarin", "Sindhi", "Sinhalese", "Slovak", "Slovenian", "Soga", "Somali", "Soninke", "Sotho", "South Ndebele", "Southern Luri", "Southwestern Mandarin", "Spanish", "Sranan Tongo", "Sundanese", "Swabian German", "Swahili", "Swati", "Swedish", "Swiss German", "Sylheti", "Syriac", "Tagalog", "Tahitian", "Taihu Wu", "Taivoan", "Tajik", "Talossan", "Talysh", "Tamil", "Tatar", "Telugu", "Tetum", "Thadou", "Thai", "Tianjin", "Tibetan", "Tigrinya", "Tlingit", "Toisanese Cantonese", "Tok Pisin", "Toki Pona", "Tondano", "Tonga", "Tongan", "Tsonga", "Tswana", "Tuareg", "Tundra Nenets", "Tunisian Arabic", "Turkish", "Turkmen", "Tuscarora", "Tuvan", "Twi", "Ubykh", "Udmurt", "Ukrainian", "Upper Saxon", "Upper Sorbian", "Urdu", "Uyghur", "Uzbek", "Venda", "Venetian", "Veps", "Vietnamese", "Volap\u00FCk", "V\u00F5ro", "Walloon", "Welsh", "Wenzhounese", "Western Apache", "Wolof", "Wu Chinese", "Xhosa", "Xiang Chinese", "Yakut", "Yeyi", "Yiddish", "Yoruba", "Yucatec Maya", "Yupik", "Zazaki", "Zhuang", "Zulu"}));
		String [] abreviaturas = {"abq", "ab", "abn", "ady", "aa", "af", "agx", "ain", "ak", "sq", "ale", "arq", "alq", "ems", "am", "grc", "ar", "an", "arp", "aae", "hy", "rup", "as", "aii", "ast", "av", "ay", "az", "bqi", "bal", "bm", "bcj", "ba", "eu", "bar", "be", "bem", "bcq", "bn", "bho", "hbo", "bh", "bi", "fla", "bs", "bph", "pcc", "br", "bzd", "bg", "my", "bsk", "bxr", "sro", "yue", "kea", "ca", "cay", "ceb", "tzm", "bcl", "ch", "plig", "ce", "chr", "ny", "cic", "chn", "cho", "cv", "rar", "cop", "kw", "co", "cr", "mus", "crh", "hr", "acy", "cs", "dag", "ada", "da", "prs", "din", "dv", "siiy", "dtp", "nl", "dz", "bin", "arz", "ekp", "egl", "en", "myv", "eo", "et", "eto", "ett", "evn", "ee", "ewo", "fo", "hif", "fj", "fi", "vls", "frp", "fr", "fy", "fur", "ff", "fzho", "gaa", "gag", "gl", "gan", "ka", "de", "glk", "gil", "el", "gn", "gu", "afb", "guz", "ht", "hak", "mey", "ha", "haw", "he", "hz", "hil", "hi", "ho", "hmn", "hop", "hu", "is", "io", "ig", "ilo", "ind", "inh", "ia", "iu", "ik", "ga", "it", "ibd", "jam", "ja", "jv", "jje", "jliu", "jlua", "cjy", "lad", "kbd", "kab", "kl", "kln", "xal", "kn", "kr", "krc", "kaa", "krl", "ks", "csb", "kk", "kca", "kha", "km", "ki", "kmb", "rw", "rn", "tlh", "kv", "koi", "kg", "gom", "ko", "avk", "kri", "kj", "ku", "kmr", "kfr", "ky", "quc", "lld", "lbe", "lki", "lkt", "lo", "ltg", "la", "lv", "lzz", "lez", "lij", "li", "ln", "lt", "jbo", "lmo", "lou", "nds", "juai", "loz", "lu", "lg", "luo", "lut", "lb", "mk", "mag", "bfz", "vmf", "mg", "ms", "ml", "zsm", "mt", "mnc", "zh", "mnk", "mns", "gv", "swb", "mi", "arn", "mr", "chm", "mh", "msb", "mfe", "myn", "mzn", "mfo", "mni", "pdt", "apm", "acm", "crg", "mic", "ltc", "enm", "cdo", "nan", "min", "xmf", "lrc", "moh", "mdf", "mo", "mn", "ary", "mos", "wlc", "mwn", "nah", "nsk", "na", "nv", "nxq", "ng", "wni", "nap", "new", "ne", "zdj", "yrl", "nog", "apc", "nd", "sme", "no", "nn", "ii", "ngh", "obu", "oc", "ogb", "oj", "ryu", "ang", "non", "otk", "or", "om", "osa", "os", "ota", "pau", "pln", "pi", "pam", "pag", "pap", "ps", "paw", "pdc", "fa", "pcd", "pms", "pjt", "pl", "pt", "pot", "cpx", "fuc", "pa", "qu", "qya", "zpf", "rap", "rcf", "rhg", "rgn", "rom", "ro", "rm", "cgg", "ru", "rue", "ksw", "sm", "sg", "sa", "skr", "sc", "sco", "gd", "trv", "sr", "srr", "shn", "jusi", "shi", "sn", "shh", "sty", "scn", "szl", "sli", "sjn", "sd", "si", "sk", "sl", "xog", "so", "snk", "st", "nr", "luz", "xghu", "es", "srn", "su", "swg", "sw", "ss", "sv", "gsw", "syl", "syc", "tl", "ty", "taiu", "tvx", "tg", "tzl", "tly", "ta", "tt", "te", "tet", "tcz", "th", "tjin", "bo", "ti", "tli", "tisa", "tpi", "x-tp", "tdn", "toi", "to", "ts", "tn", "tmh", "yrk", "aeb", "tr", "tk", "tus", "tyv", "tw", "uby", "udm", "uk", "sxu", "hsb", "ur", "ug", "uz", "ve", "vec", "vep", "vi", "vo", "vro", "wa", "cy", "qjio", "apw", "wo", "wuu", "xh", "hsn", "sah", "yey", "yi", "yo", "yua", "esu", "zza", "za", "zu"};

		//Trae configuración del idioma.		
		String idiomaGuardado = configuracion.get("FORVO_IDIOMA", "");
		if(!idiomaGuardado.equals("")) 
		{
			comboIdioma.setSelectedItem(idiomaGuardado);
		}
		frmForvotabloader.getContentPane().add(comboIdioma);

		//Quinta fila
		JLabel lblCorreo = new JLabel("Correo:");
		lblCorreo.setBounds(221, 155, 46, 14);
		frmForvotabloader.getContentPane().add(lblCorreo);

		textCorreo = new JTextField();
		textCorreo.setBounds(324, 152, 204, 20);
		textCorreo.setColumns(10);
		
		//Trae configuración del email.		
		String correoGuardado = configuracion.get("FORVO_EMAIL", "");
		if(!correoGuardado.equals("")) 
		{
			textCorreo.setText(correoGuardado);
		}
		frmForvotabloader.getContentPane().add(textCorreo);
		

		//Sexta fila
		JLabel lblContrasena = new JLabel("Contrase\u00F1a:");
		lblContrasena.setBounds(221, 187, 92, 14);
		frmForvotabloader.getContentPane().add(lblContrasena);

		passwordContrasena = new JPasswordField();
		passwordContrasena.setBounds(324, 184, 204, 20);
		
		//Trae configuración del email.		
		String contrasenaGuardada = configuracion.get("FORVO_CONTRASENA", "");
		if(!correoGuardado.equals("")) 
		{
			passwordContrasena.setText(contrasenaGuardada);
		}
		frmForvotabloader.getContentPane().add(passwordContrasena);

		//Séptima fila
		JCheckBox chckbxDatosInicio = new JCheckBox("Recordar datos de inicio de sesi\u00F3n");
		chckbxDatosInicio.setBounds(316, 213, 249, 23);
		String checked = configuracion.get("FORVO_LOGIN_CHECKED", "");
		
		//Trae configuración si se guardaron los credenciales.
		if(checked.equals("") || checked.equals("false")) 
		{
			chckbxDatosInicio.setSelected(false);
		}
		else
		{
			chckbxDatosInicio.setSelected(true);
		}
		frmForvotabloader.getContentPane().add(chckbxDatosInicio);

		//Octava fila		
		JButton btnEjecutar = new JButton("Ejecutar");
		btnEjecutar.setBounds(301, 243, 92, 23);
		btnEjecutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				columna = (int) spinnerColumna.getValue();
				idioma = abreviaturas[comboIdioma.getSelectedIndex()];
				correo = textCorreo.getText();
				contrasena = String.valueOf(passwordContrasena.getPassword());
				pathDownloads = configuracion.get("FORVO_PATH_DOWNLOADS", "");
				
				//Actualiza configuración de idioma y columna.
				configuracion.put("FORVO_COLUMNA", Integer.toString(columna));
				configuracion.put("FORVO_IDIOMA", (String) comboIdioma.getSelectedItem());

				//Almacena el usuario y contraseña si el usuario lo desea.
				if(chckbxDatosInicio.isSelected())
				{
					configuracion.put("FORVO_EMAIL", correo);
					configuracion.put("FORVO_CONTRASENA", contrasena);
					configuracion.put("FORVO_LOGIN_CHECKED", "true");
				}
				else
				{
					configuracion.put("FORVO_EMAIL", "");
					configuracion.put("FORVO_CONTRASENA", "");
					configuracion.put("FORVO_LOGIN_CHECKED", "false");
				}

				//Ejecución del programa
				try {
					System.out.println("Se ejecuta el programa.");
					ForvoPageOpen main= new ForvoPageOpen(columna-1, archivo, idioma, correo, contrasena, pathDownloads);
				} catch (Exception e1) {
					JOptionPane optionPane = new JOptionPane("Ha ocurrido un error inesperado:\n"+e1.getMessage(), JOptionPane.ERROR_MESSAGE);    
					JDialog dialog = optionPane.createDialog(e1.getMessage());
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);
					e1.printStackTrace();
				}
			}
		});
		frmForvotabloader.getContentPane().add(btnEjecutar);
	}
}
