package uz.khodirjob.meinarzt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import uz.khodirjob.meinarzt.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class MeinArztApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeinArztApplication.class, args);
    }

}
