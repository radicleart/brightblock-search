package org.brightblock.search.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {
	
    @Override
    public void configurePathMatch(PathMatchConfigurer matcher) {
        matcher.setUseRegisteredSuffixPatternMatch(true);
    }

//    @Override
//	public void addCorsMappings(CorsRegistry registry) {
//		// registry.addMapping("/**").allowedMethods("*").allowedHeaders("*").allowedOrigins("http://localhost:8080", "https://staging.transit8.com", "https://www.transit8.com", "https://www.brightblock.org", "https://staging.brightblock.org");
//		// registry.addMapping("/**").allowedMethods("*").allowedHeaders("*").allowedOrigins("*");
//	}
}
