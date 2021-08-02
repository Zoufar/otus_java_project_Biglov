package weathertelegrambot.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import weathertelegrambot.config.TelegramBotProps;
import weathertelegrambot.parsers.WeatherJsonParser;
import weathertelegrambot.parsers.WeatherYandexParser;

@Service
public class WeatherYandexService implements WeatherService {

    private final WebClient weatherYandexClient;
    private final TelegramBotProps props;
    private final String API_KEY_YA;
    private final WeatherJsonParser parser;

    public WeatherYandexService (TelegramBotProps props, WeatherYandexParser parser) {
        this.weatherYandexClient = WebClient.create("https://api.weather.yandex.ru/v2/forecast");
        this.props = props;
        this.parser = parser;
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

    @Override
    public WeatherJsonParser getParser () { return parser;}


}