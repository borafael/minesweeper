package ar.com.rbo.minesweeper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ar.com.rbo.minesweeper.controller.GameMapper;

@Configuration
public class ApplicationConfiguration {
	
    @Bean
    public GameMapper gameMapper() {
        return new GameMapper();
    }
}