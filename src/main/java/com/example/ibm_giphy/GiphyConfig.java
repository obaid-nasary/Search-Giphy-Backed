package com.example.ibm_giphy;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@EnableAutoConfiguration
@Configuration
public class GiphyConfig {

    @Bean
    CommandLineRunner commandLineRunner(GiphyRepository giphyRepository){
        return args -> {
            Giphy one = new Giphy("Obaidullah",
                    "obaidnasary",
                    "khajan",
                    "Crane crush crossroad crap"
            );
            Giphy two = new Giphy(
                    "Waheed",
                    "waheedmajroh",
                    "whaheed12",
                    "Mountain top and dow rolling"
            );
            giphyRepository.saveAll(List.of(one, two));
        };
    }
}
