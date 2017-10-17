package com.smarttoolsapp.video;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;

public class VideoConverter {

	public static void main(String[] args) {
		
		for(int i = 0; i < args.length; i += 2 ){
			
			Parametros.setParametro(args[i], args[i + 1]);
		}
		
		if(Parametros.isNoSql())
			iniciarConversionDynamo();
		else
			iniciarConversionPostgres();
		
	}
	public static boolean convertirVideo(Video video){
		try {
			
			if(video.sin_convertir_file_name.toLowerCase().endsWith(".mp4"))
			{
				System.out.println("Ya esta en .mp4 ");
				String rutaOrg;
				if(Parametros.isNoSql()){
					rutaOrg = Parametros.getRutaBaseVideos()
							+ File.separator + "original" 
							+ File.separator + video.sin_convertir_file_name;
				}
				else
				{
					rutaOrg = Parametros.getRutaBaseVideos()
							+ File.separator + "original" 
							+ File.separator + video.getSinConvertirNameConId();
				}
				File este = new File(rutaOrg);
				System.out.println(este.getPath());
				if(este.exists())
				{
					System.out.println("Copiando video a convertido");
					String rutaDest;
					if(Parametros.isNoSql()){
						rutaDest = Parametros.getRutaBaseVideos()
								+ File.separator + "convertido" 
								+ File.separator + video.sin_convertir_file_name;
					}
					else{
						rutaDest = Parametros.getRutaBaseVideos()
								+ File.separator + "convertido" 
								+ File.separator + video.getSinConvertirNameConId();
					}
					
					Files.copy(Paths.get(rutaOrg), Paths.get(rutaDest),
							StandardCopyOption.REPLACE_EXISTING);
					
					System.out.println("Copiado exitoso.");
					
					return(true);
				}
				return(false);
			}
			else
			{
				String comando;
				if(Parametros.isNoSql()){
					comando = String.format("ffmpeg -y -i %1$s%3$soriginal%3$s%2$s -vcodec libx264 -acodec aac %1$s%3$sconvertido%3$s%4$s"
						, Parametros.getRutaBaseVideos(), video.sin_convertir_file_name
						, File.separator
						, video.convertido_file_name);
				}
				else{
					comando = String.format("ffmpeg -y -i %1$s%3$soriginal%3$s%2$s -vcodec libx264 -acodec aac %1$s%3$sconvertido%3$s%4$s"
							, Parametros.getRutaBaseVideos(), video.getSinConvertirNameConId()
							, File.separator
							, video.getConvertidoNameConId());
				}
					
				System.out.println("Comando -> " + comando);

				Executor exec = new DefaultExecutor();
				CommandLine cl = CommandLine.parse(comando);
				
				int exitvalue = - 100;
				
				exitvalue = exec.execute(cl);		
				
				if(exitvalue == 0)
				{
					System.out.println("Proceso ejecutado correctamente");
					return(true);
				}
				else
				{
					System.out.println("Fallo ejecución del proceso");
					return(false);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return(false);
		}
		
	}
	
	static void iniciarConversionPostgres(){
		
		if(Parametros.isAllReady())
		{
			BDPostgres bd = new BDPostgres();
			CorreoElectronico ce = new CorreoElectronico();
			boolean continuar;
			
			do{
				continuar = false;
				List<Video> videos = bd.consultarVideosXConvertir();
				for(Video video: videos){
					continuar = true;
					if(bd.CambiarVideoAConvirtiendo(video)){
						System.out.println("Convirtiendo video :"
								+ video.getSinConvertirNameConId());
						
						// Nombre del archivo convertido
						int posPunto = video.sin_convertir_file_name.lastIndexOf('.');
						video.convertido_file_name =
								video.sin_convertir_file_name.substring(0, posPunto) + ".mp4";
						
						// Tipo de Contenido
						video.convertido_content_type = "video/mp4";
					
						if(convertirVideo(video))
						{
							// Estado
							video.state = true;
							
							// Tamano
							
							try {
								File este = new File(Parametros.getRutaBaseVideos()
											+ File.separator +"convertido" + File.separator
											+ video.getConvertidoNameConId());
								video.convertido_file_size = (int)este.length();
							} catch (Exception e1) {
								video.convertido_file_size = 0;
							}
							
							
							if(bd.actualizarEstadoVideo(video)){
								try {
									ce.EnviarCorreo(video);
								} catch (AddressException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (MessagingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}while(continuar);
		}
		else
			System.out.println("Se debe especificar todos los parametros:\r\n" +
					Parametros.getListaParametros());
	}
	
	static void iniciarConversionDynamo(){
		
		if(Parametros.isAllReady())
		{
			BDDynamo bd = new BDDynamo();
			CorreoElectronico ce = new CorreoElectronico();
			boolean continuar;
			ArchivosS3 s3 = new ArchivosS3();
			
			do{
				continuar = false;
				List<Video> videos = bd.consultarVideosXConvertir();
				for(Video video: videos){
					continuar = true;
					if(bd.CambiarVideoAConvirtiendo(video)){
						System.out.println("Convirtiendo video :"
								+ video.getSinConvertirNameConId());
						
						// Nombre del archivo convertido
						int posPunto = video.sin_convertir_file_name.lastIndexOf('.');
						video.convertido_file_name =
								video.sin_convertir_file_name.substring(0, posPunto) + ".mp4";
						
						// Tipo de Contenido
						video.convertido_content_type = "video/mp4";
					
						// Descarga del video desde s3
						if(s3.bajarArchivo(video.sin_convertir_file_name, Parametros.getRutaBaseVideos()
								+ File.separator + "original" ))
						{
							if(convertirVideo(video))
							{
								if(s3.subirArchivo(video.convertido_file_name, Parametros.getRutaBaseVideos()
										+ File.separator + "convertido"))
								{
									if(bd.actualizarEstadoVideo(video)){
										try {
											ce.EnviarCorreo(video);
										} catch (AddressException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (MessagingException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									borrarVideos(video);
								}
							}
						}
					}
				}
			}while(continuar);
			bd.Desconectar();
		}
		else
			System.out.println("Se debe especificar todos los parametros:\r\n" +
					Parametros.getListaParametros());
	}
	
	static void borrarVideos(Video video)
	{
		try {
			File f = new File(Parametros.getRutaBaseVideos()
											+ File.separator
											+ "convertido" 
											+ File.separator
											+ video.convertido_file_name);
			if (f.exists())
				f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			File f2 = new File(Parametros.getRutaBaseVideos()
					+ File.separator
					+ "original" 
					+ File.separator
					+ video.sin_convertir_file_name);
			if (f2.exists())
				f2.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
