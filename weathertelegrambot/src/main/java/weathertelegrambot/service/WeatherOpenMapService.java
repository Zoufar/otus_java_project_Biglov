package weathertelegrambot.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import weathertelegrambot.config.TelegramBotProps;

@Service
public class WeatherOpenMapService {

    private final WebClient weatherOpenMapClient;
    private final TelegramBotProps props;
    private final String API_KEY;

    public WeatherOpenMapService (TelegramBotProps props) {
        this.weatherOpenMapClient = WebClient.create("http://api.openweathermap.org/data/2.5/weather");
        this.props = props;
        this.API_KEY = props.getOmpapikey();
    }

    public Mono<String> getWeatherByCoords(String latitude, String longitude) {
        return weatherOpenMapClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .queryParam("units", "metric")
                        .queryParam("appid", API_KEY)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

}
