package br.com.softblue.bluefood.application.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.softblue.bluefood.util.IOUtil;

@Service

public class ImageService {
	@Value("${bluefood.files.logotipo}")
	private String logotiposDir;
	@Value("${bluefood.files.categorias}")
	private String categoriasDir;
	@Value("${bluefood.files.comidas}")
	private String comidasDir;

	public void uploadLogotipo(MultipartFile multiPartFile, String fileName) {

		try {
			IOUtil.copy(multiPartFile.getInputStream(), fileName, logotiposDir);
		} catch (IOException e) {
			throw new ApplicationServiceException(e);
		}
	}

	public void uploadComidas(MultipartFile multiPartFile, String fileName) {

		try {
			IOUtil.copy(multiPartFile.getInputStream(), fileName, comidasDir);
		} catch (IOException e) {
			throw new ApplicationServiceException(e);
		}
	}
	
	
	public byte[] getByte(String type, String imgname) {

		try {
			String dir;
			
			if ("comidas".equals(type)) {
				dir = comidasDir;
			
			} else if ("logotipo".equals(type)) {
				dir = logotiposDir;
			
			} else if ("categoria".equals(type)) {
				dir = categoriasDir;
			
			} else {
				throw new Exception(type + " não é um tipo de imagem válido");
			}
			
			return IOUtil.getBytes(Paths.get(dir, imgname));
		
		} catch (Exception e) {
			throw new ApplicationServiceException(e);
		}
	}

}
