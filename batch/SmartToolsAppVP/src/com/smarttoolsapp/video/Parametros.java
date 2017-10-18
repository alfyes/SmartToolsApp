package com.smarttoolsapp.video;

public class Parametros {
	private static String nombreBD;
	private static String usuarioBD;
	private static String claveBD;
	private static String nombreServidorBD;
	private static String puertoServidorBD;
	private static String rutaBaseVideos;
	private static String mostrarSalida = "0";
	private static int puertoCorreo = 0;
	private static String usuarioCorreo;
	private static String claveCorreo;
	private static String hostCorreo;
	private static String fromCorreo;
	
	private static int controlParametros = 0;
	
	public static String getNombreBD() {
		return nombreBD;
	}
	public static String getUsuarioBD() {
		return usuarioBD;
	}
	public static String getClaveBD() {
		return claveBD;
	}
	
	public static String getNombreServidorBD() {
		return nombreServidorBD;
	}
	public static String getPuertoServidorBD() {
		return puertoServidorBD;
	}
	public static String getRutaBaseVideos() {
		return rutaBaseVideos;
	}
	
	public static String getMostrarSalida() {
		return mostrarSalida;
	}
	
	public static int getPuertoCorreo(int puerto) {
		if(puertoCorreo != 0)
			return puertoCorreo;
		else
			return puerto;
	}
	public static String getUsuarioCorreo(String usuario) {
		if(usuarioCorreo != null)
			return usuarioCorreo;
		else
			return(usuario);
	}
	public static String getClaveCorreo(String clave) {
		if(claveCorreo != null)
			return claveCorreo;
		else
			return(clave);
	}
	public static String getHostCorreo(String host) {
		if(hostCorreo != null)
			return hostCorreo;
		else
			return(host);
	}
	
	public static String getFromCorreo() {
			return fromCorreo;
	}
	
	public static void setParametro(String parametro, String valor)
	{
		switch (parametro) {
		case "-h":
			nombreServidorBD = valor;
			controlParametros |= 0x01;
			break;
		case "-ph": 
			puertoServidorBD = valor;
			controlParametros |= 0x02;
			break;
		case "-u": 
			usuarioBD = valor;
			controlParametros |= 0x04;
			break;
		case "-p": 
			claveBD = valor;
			controlParametros |= 0x08;
			break;
		case "-nb": 
			nombreBD = valor;
			controlParametros |= 0x10;
			break;
		case "-rt": 
			rutaBaseVideos = valor;
			controlParametros |= 0x11;
			break;
		case "-ms": 
			mostrarSalida = valor;
			break;
		case "-cp": 
			 try {
				puertoCorreo= Integer.valueOf(valor);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				puertoCorreo = 0;
			}
			break;
		case "-ch":
			hostCorreo = valor;
			break;
		case "-cu":
			usuarioCorreo = valor;
			break;
		case "-cc":
			claveCorreo = valor;
			break;
		case "-cf":
			fromCorreo = valor;
			break;
		default:
			break;
		}
	}
	
	public static boolean isAllReady()
	{
		return(controlParametros == 0x1F);
	}
	
	public static String getListaParametros()
	{
		return("-h -> Servidor BD\r\n -ph -> Puerto servidor BD\r\n -u usuario BD\r\n -p clave BD"
				+ "\r\n -nb Nombre BD\r\n -rt Ruta base videos");
	}
}
