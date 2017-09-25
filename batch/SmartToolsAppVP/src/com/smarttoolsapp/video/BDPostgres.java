package com.smarttoolsapp.video;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BDPostgres {
	private String url = "jdbc:postgresql://192.168.1.12:5432/SmartToolsApp_development";
	private String user = "postgres";
	private String password = "postgres";
	
	
	public BDPostgres() {
		url = String.format("jdbc:postgresql://%1$s:%2$s/%3$s", Parametros.getNombreServidorBD()
				, Parametros.getPuertoServidorBD(), Parametros.getNombreBD());
		user = Parametros.getUsuarioBD();
		password = Parametros.getClaveBD();
	}

	public Connection connect() throws SQLException{
		return DriverManager.getConnection(url,user,password);
	}
	
	public List<Video> consultarVideosXConvertir(){
		
		String consulta = "select * from videos where state = false";
		List<Video> videos = new ArrayList<>();
		try(Connection conn = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(consulta)){
			while(rs.next()){
				
				try {
					videos.add(Video.fromResultSet(rs));
				} catch (SQLException e) {

				}
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return(videos);
	}
	
	public boolean actualizarEstadoVideo(Video video){
		
		String consulta = "update videos set state = ?, convertido_file_name = ?, convertido_file_size = ?, convertido_content_type = ?, convertido_updated_at = sin_convertir_updated_at where id = ? ";
		
		int affectedRows = 0;
		
		try(Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(consulta)){
			
			pstmt.setBoolean(1, video.state);
			pstmt.setString(2, video.convertido_file_name);
			pstmt.setInt(3, video.convertido_file_size);
			pstmt.setString(4, video.convertido_content_type);
			pstmt.setLong(5, video.id);
			
			affectedRows = pstmt.executeUpdate();
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return(affectedRows > 0);
	}
	
}
