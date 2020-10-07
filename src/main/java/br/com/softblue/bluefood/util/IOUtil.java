package br.com.softblue.bluefood.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class IOUtil {
	
	public static void copy(InputStream in, String filename, String outPutDir) throws IOException {
		Files.copy(in, Paths.get(outPutDir, filename), StandardCopyOption.REPLACE_EXISTING);
	}
	
	public static byte[] getBytes(Path path) throws IOException{
		return Files.readAllBytes(path);
	}

}
