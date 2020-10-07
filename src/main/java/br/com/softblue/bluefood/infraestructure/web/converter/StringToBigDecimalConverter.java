package br.com.softblue.bluefood.infraestructure.web.converter;

import java.math.BigDecimal;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.softblue.bluefood.util.StringUtil;

@Component
public class StringToBigDecimalConverter implements Converter<String, BigDecimal>{

	@Override
	public BigDecimal convert(String source) {

		if(StringUtil.isEmpty(source)) {
			return null;
		}
		
		source = source.replace(",", ".").trim();
		
		return BigDecimal.valueOf(Double.valueOf(source));
		
		
		
		
	}

	

}
