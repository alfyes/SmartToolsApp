package com.smarttoolsapp.video;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BDPostgres implements IBDApp{
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
	
	@Override
	public List<Video> consultarVideosXConvertir(){
		
		String consulta = "select * from videos where state = false and (convirtiendo <> true or convirtiendo is null) limit 1";
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
	
	@Override
	public boolean actualizarEstadoVideo(Video video){
		
		String consulta = "update videos set state = ?, convertido_file_name = ?, convertido_file_size = ?, convertido_content_type = ?, convertido_updated_at = sin_convertir_updated_at, worker = ? where id = ? ";
		
		int affectedRows = 0;
		
		try(Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(consulta)){
			
			pstmt.setBoolean(1, video.state);
			pstmt.setString(2, video.convertido_file_name);
			pstmt.setInt(3, video.convertido_file_size);
			pstmt.setString(4, video.convertido_content_type);
			pstmt.setInt(5, Parametros.getWorker(0));
			pstmt.setLong(6, video.id);
			
			affectedRows = pstmt.executeUpdate();
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return(affectedRows > 0);
	}
	
	@Override
	public boolean CambiarVideoAConvirtiendo(Video video){
		
		String consulta = "update videos set convirtiendo = ?, worker = ? where id = ? and (convirtiendo <> true or convirtiendo is null)";
		
		int affectedRows = 0;
		
		try(Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(consulta)){
			
			pstmt.setBoolean(1, true);
			pstmt.setInt(2, Parametros.getWorker(0));
			pstmt.setLong(3, video.id);
			
			affectedRows = pstmt.executeUpdate();
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return(affectedRows > 0);
	}
}
