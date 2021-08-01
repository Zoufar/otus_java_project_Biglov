package weathertelegrambot.service;

import reactor.core.publisher.Mono;
import weathertelegrambot.parsers.WeatherJsonParser;

public interface WeatherService {

    Mono<String> getWeatherByCoords(String latitude, String longitude);

    WeatherJsonParser getParser ();

}
