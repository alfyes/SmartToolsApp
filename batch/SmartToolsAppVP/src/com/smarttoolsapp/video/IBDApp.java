package com.smarttoolsapp.video;

import java.util.List;

public interface IBDApp {
	public List<Video> consultarVideosXConvertir();
	public boolean actualizarEstadoVideo(Video video);
	public boolean CambiarVideoAConvirtiendo(Video video);
}
