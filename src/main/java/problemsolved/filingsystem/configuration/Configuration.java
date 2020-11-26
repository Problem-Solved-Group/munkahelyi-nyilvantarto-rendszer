package problemsolved.filingsystem.configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@org.springframework.context.annotation.Configuration
@EnableWebMvc
public class Configuration implements WebMvcConfigurer {
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}
