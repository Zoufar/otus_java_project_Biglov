package weathertelegrambot.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import weathertelegrambot.config.TelegramBotProps;
import weathertelegrambot.parsers.WeatherJsonParser;
import weathertelegrambot.parsers.WeatherOpenMapParser;

@Service
public class WeatherOpenMapService implements WeatherService {

    private final WebClient weatherOpenMapClient;
    private final TelegramBotProps props;
    private final WeatherJsonParser parser;
    private final String API_KEY;

    public WeatherOpenMapService (TelegramBotProps props, WeatherOpenMapParser parser) {
        this.weatherOpenMapClient = WebClient.create("http://api.openweathermap.org/data/2.5/weather");
        this.props = props;
        this.parser = parser;
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

    @Override
    public WeatherJsonParser getParser () { return parser;}


}
