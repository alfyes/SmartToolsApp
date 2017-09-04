package com.smarttoolsapp.video;

import java.sql.ResultSet;
import java.sql.SQLException;

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
		int posIns = video.sin_convertir_file_name.lastIndexOf('.');
		video.sin_convertir_file_name = video.sin_convertir_file_name.substring(0, posIns) 
				+ "_" + String.valueOf(video.id)
				+ video.sin_convertir_file_name.substring(posIns, video.sin_convertir_file_name.length());
		return(video);
	}
}
