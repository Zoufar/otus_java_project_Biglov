package weathertelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeatherForecastService {
    public static void main(String[] args) {

        var context = SpringApplication.run(WeatherForecastService.class, args);

        context.getBean("initBot", InitBot.class).start();

    }
}
