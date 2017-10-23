package com.smarttoolsapp.video;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ALFONSODEJESUS on 15/05/2015.
 */
public class Archivos {
	/**
	 * Leer completamente un archivo de texto.
	 * 
	 * @param rutaNombre
	 *            Ruta completa del archivo a leer.
	 * @return Una lista de string con cada linea leida.
	 */
	public static List<String> leerArchivoTexto(String rutaNombre) {
		File arch = new File(rutaNombre);
		FileReader archEst;
		String linea;
		List<String> listLineas = new ArrayList<>();
		try {
			archEst = new FileReader(arch);
			BufferedReader br = new BufferedReader(archEst);
			while (true) {
				linea = br.readLine();
				if (linea == null)
					break;
				listLineas.add(linea);
			}

			br.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			return (null);
		} catch (IOException e) {

			e.printStackTrace();
			return (null);
		}
		try {

			archEst.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (listLineas);
	}

	public static void escribirArchivoTexto(String rutaNombre,
			List<String> lineas)

	{
		File arch = new File(rutaNombre);
		FileWriter archEst = null;
		try {
			archEst = new FileWriter(arch, false);
			PrintWriter pw = new PrintWriter(archEst);
			for (String linea : lineas)
				pw.println(linea);
			pw.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		try {
			if (archEst != null)
				archEst.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
