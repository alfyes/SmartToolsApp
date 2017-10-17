package com.smarttoolsapp.video;

import java.nio.file.Paths;

import software.amazon.awssdk.SdkBaseException;
import software.amazon.awssdk.SdkClientException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.sync.StreamingResponseHandler;

public class ArchivosS3 {

	S3Client s3;
	String bucket = "smart-tools-app";
	String baseOriginal = "videos/original/";
	String baseConvertido = "videos/convertido/";
			
	public boolean bajarArchivo(String nombre, String rutaGuardar)
	{
		
		try {
			s3 = S3Client.create();
			s3.getObject(GetObjectRequest.builder()
					.bucket(bucket)
					.key(baseOriginal + nombre)
					.build(),
			StreamingResponseHandler.toFile(Paths.get(rutaGuardar, nombre)));
			
			s3.close();
			return(true);
			
		} catch (NoSuchKeyException e) {
			e.printStackTrace();
		} catch (S3Exception e) {
			e.printStackTrace();
		} catch (SdkClientException e) {
			e.printStackTrace();
		} catch (SdkBaseException e) {
			e.printStackTrace();
		}
		
		if(s3 != null)
			s3.close();
		return(false);
	}
	
	public boolean subirArchivo(String nombre, String rutaArchivo)
	{
		try {
			s3 = S3Client.create();
			s3.putObject(PutObjectRequest.builder()
					.bucket(bucket)
					.key(baseConvertido + nombre)
					.acl("public-read")
					.build(), Paths.get(rutaArchivo, nombre));
			
			return(true);
		} catch (S3Exception e) {
			e.printStackTrace();
		} catch (SdkClientException e) {
			e.printStackTrace();
		} catch (SdkBaseException e) {
			e.printStackTrace();
		}
		if(s3 != null)
			s3.close();
		return(false);
	}
}
