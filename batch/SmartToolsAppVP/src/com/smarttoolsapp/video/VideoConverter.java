package com.smarttoolsapp.video;

import java.io.*;
import java.io.ObjectInputStream.GetField;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.Clock;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.io.IOUtils;
public class VideoConverter {

	public static void main(String[] args) {
		
		for(int i = 0; i < args.length; i += 2 ){
			
			Parametros.setParametro(args[i], args[i + 1]);
		}
		
		if(Parametros.isAllReady())
		{
			BDPostgres bd = new BDPostgres();
			List<Video> videos = bd.consultarVideosXConvertir();
			CorreoElectronico ce = new CorreoElectronico();
			for(Video video: videos){
				System.out.println("Convirtiendo video :" + video.getSinConvertirNameConId());
				
				// Nombre del archivo convertido
				int posPunto = video.sin_convertir_file_name.lastIndexOf('.');
				video.convertido_file_name = video.sin_convertir_file_name.substring(0, posPunto) + ".mp4";
				
				// Tipo de Contenido
				video.convertido_content_type = "video/mp4";
				
				if(convertirVideo(video))
				{
					// Estado
					video.state = true;
					
					// Tamano
					
					try {
						File este = new File(Parametros.getRutaBaseVideos()+ File.separator +"convertido" + File.separator + video.getConvertidoNameConId());
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
		else
			System.out.println("Se debe especificar todos los parametros:\r\n" + Parametros.getListaParametros());
	}
	public static boolean convertirVideo(Video video){
		
		Process p = null;
		try {
			if(video.sin_convertir_file_name.toLowerCase().endsWith(".mp4"))
			{
				System.out.println("Ya esta en .mp4 ");
				String rutaOrg = Parametros.getRutaBaseVideos()+ File.separator + "original" + File.separator + video.getSinConvertirNameConId();
				File este = new File(rutaOrg);
				System.out.println(este.getPath());
				if(este.exists())
				{
					System.out.println("Copiando video a convertido");
					String rutaDest = Parametros.getRutaBaseVideos()+ File.separator + "convertido" + File.separator + video.getSinConvertirNameConId();
					Files.copy(Paths.get(rutaOrg), Paths.get(rutaDest), StandardCopyOption.REPLACE_EXISTING);
					System.out.println("Copiado exitoso.");
					return(true);
				}
				return(false);
			}
			else
			{
				String comando = String.format("ffmpeg -y -i %1$s%3$soriginal%3$s%2$s -vcodec libx264 -acodec aac %1$s%3$sconvertido%3$s%4$s"
						, Parametros.getRutaBaseVideos(), video.getSinConvertirNameConId(), File.separator
						, video.getConvertidoNameConId());
				System.out.println("Comando -> " + comando);
				//p = Runtime.getRuntime().exec(comando);
				
				ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-y", "-i"
						, String.format("%1$s%3$soriginal%3$s%2$s", Parametros.getRutaBaseVideos(), video.getSinConvertirNameConId(), File.separator)
						, "-vcodec", "libx264", "-acodec", "aac"
						, String.format("%1$s%3$sconvertido%3$s%2$s", Parametros.getRutaBaseVideos(), video.getConvertidoNameConId(), File.separator) );
				
				//String comando = String.format("-y -i %1$s\\original\\%2$s -vcodec libopenh264 -acodec aac %1$s\\convertido\\%2$s"
					//	, Parametros.getRutaBaseVideos(), video.sin_convertir_file_name);
				//String[] command = {"ffmpeg", comando};
				//ProcessBuilder builder = new ProcessBuilder(command);
				p = pb.start();
				
				//SSwatchProcess(p, Parametros.getMostrarSalida().equals("1"));
				//BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			     //String line = null;
				//int aca;
				//while(p.isAlive())
				//{
					//try {
				       // while ((line = input.read()) != null)
				        //{
				        	//if(mostrarSalida)
					//try {
						//Thread.sleep(100);
					//} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
					//}
				      //  		System.out.print("paso");
				        		 //aca = p.getInputStream().;
				        //}
				     //} catch (IOException e) {
				       //     e.printStackTrace();
				     //}
				//}
				final StringWriter writer = new StringWriter();
				final Process p2 = p;
			    new Thread(new Runnable() {
			        @SuppressWarnings("deprecation")
					public void run() {
			            try {
							IOUtils.copy(p2.getInputStream(), writer);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
			    }).start();
	
			    final int exitValue = p.waitFor();
			    final String processOutput = writer.toString();
				//p.waitFor();
				
				if(p.exitValue()== 0)
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return(false);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return(false);
		}
		finally
		{
			try {
				if (p != null)
					p.destroy();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public static void watchProcess(final Process p, final boolean mostrarSalida)
	{
		new Thread(new Runnable() {
		    public void run() {
		     BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		     String line = null; 
		     try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		     try {
		        while ((line = input.readLine()) != null)
		        {
		        	//if(mostrarSalida)
		        		System.out.println(line);
		        }
		     } catch (IOException e) {
		            e.printStackTrace();
		     }
		    }
		}).start();
	}
}
