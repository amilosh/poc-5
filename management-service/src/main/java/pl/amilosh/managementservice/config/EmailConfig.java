package pl.amilosh.managementservice.config;

import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

@Configuration
public class EmailConfig {

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        var freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates/");
        return freeMarkerConfigurer;
    }

    @Bean
    public freemarker.template.Configuration freeMarkerConfigurationBean() throws TemplateException, IOException {
        return freeMarkerConfigurer().createConfiguration();
    }
}
