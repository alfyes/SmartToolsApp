package com.smarttoolsapp.video;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Video {
	long id;
	String name;
	String message;
	String firstNameUser;
	String lastNameUser;
	String emailUser;
	boolean state;
	long concurso_id;
	String sin_convertir_file_name;
	String convertido_file_name;
	int convertido_file_size;
	String convertido_content_type;
	Timestamp convertido_updated_at;
	
	public static Video fromResultSet(ResultSet rs) throws SQLException
	{
		Video video = new Video();	
		video.id = rs.getLong("id");
		video.name = rs.getString("name");
		video.message = rs.getString("message");
		video.firstNameUser = rs.getString("firstNameUser");
		video.lastNameUser = rs.getString("lastNameUser");
		video.emailUser = rs.getString("emailUser");
		video.state = rs.getBoolean("state");
		video.concurso_id= rs.getLong("concurso_id");
		video.sin_convertir_file_name = rs.getString("sin_convertir_file_name");
		
		return(video);
	}
	
	public String getSinConvertirNameConId()
	{
		int posIns = sin_convertir_file_name.lastIndexOf('.');
		return (sin_convertir_file_name.substring(0, posIns) 
				+ "_" + String.valueOf(id)
				+ sin_convertir_file_name.substring(posIns, sin_convertir_file_name.length()));
	}
	public String getConvertidoNameConId()
	{
		int posIns = convertido_file_name.lastIndexOf('.');
		return (convertido_file_name.substring(0, posIns) 
				+ "_" + String.valueOf(id)
				+ convertido_file_name.substring(posIns, convertido_file_name.length()));
	}
}
