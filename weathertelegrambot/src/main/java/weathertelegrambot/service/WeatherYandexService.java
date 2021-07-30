package weathertelegrambot.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import weathertelegrambot.config.TelegramBotProps;

@Service
public class WeatherYandexService {

    private final WebClient weatherYandexClient;
    private final TelegramBotProps props;
    private final String API_KEY_YA;

    public WeatherYandexService (TelegramBotProps props) {
        this.weatherYandexClient = WebClient.create("https://api.weather.yandex.ru/v2/forecast");
        this.props = props;
        this.API_KEY_YA= props.getYandexapikey();
    }

    public Mono<String> getWeatherByCoords(String latitude, String longitude) {
        return weatherYandexClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .build())
                .header( "X-Yandex-API-Key",API_KEY_YA)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

}