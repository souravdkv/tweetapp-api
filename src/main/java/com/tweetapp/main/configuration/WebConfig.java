package com.tweetapp.main.configuration;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		List<MediaType> list = new ArrayList<>();
		list.add(MediaType.APPLICATION_JSON);
		list.add(new MediaType("text", "html", StandardCharsets.UTF_8));
		list.add(new MediaType("application", "*+json", StandardCharsets.UTF_8));
		converter.setSupportedMediaTypes(list);
		converters.add(converter);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("http://localhost:4200", "https://durbaries-web.herokuapp.com")
				.allowedMethods("GET", "POST", "OPTIONS").allowCredentials(true);
	}

}