package br.com.softblue.bluefood.infraestructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.softblue.bluefood.application.service.ImageService;

@Controller
public class ImageController {
	@Autowired
	private ImageService imageService;
	
	@ResponseBody
	@GetMapping(path="/images/{type}/{imgname}",produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getByte(@PathVariable("type") String type,
			@PathVariable("imgname") String imgname)	{
		
		return imageService.getByte(type, imgname);
		
	}
}
