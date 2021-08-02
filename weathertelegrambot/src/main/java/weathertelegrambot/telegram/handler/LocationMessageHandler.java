package weathertelegrambot.telegram.handler;

import weathertelegrambot.parsers.WeatherJsonParser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weathertelegrambot.service.WeatherService;
import reactor.core.scheduler.Schedulers;
import weathertelegrambot.telegram.WeatherTelegramBot;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class LocationMessageHandler implements MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(LocationMessageHandler.class);

    private final WeatherTelegramBot weatherTelegramBot;
    private final List<WeatherService> weatherServices;

    @Override
    public String handle(Message message) {
        if (message.hasLocation()) {
            String userLat = message.getLocation().getLatitude().toString();
            String userLon = message.getLocation().getLongitude().toString();

            logger.info("handle() - trace: location: latitude = {}, longitude = {}", userLat, userLon);

            weatherServices.stream()
                    .map(weatherService -> weatherService.getWeatherByCoords(userLat, userLon).
                            subscribeOn(Schedulers.boundedElastic()).
                            subscribe(s -> sendMessage(s, message, weatherService.getParser())))
                    .collect(Collectors.toList());

            return "Получил координаты: "+ userLat+ ", " + userLon;
        }
        return StringUtils.EMPTY;
    }

    private void sendMessage(String s, Message message, WeatherJsonParser parser ){
        String weatherJson = parser.parseWeatherJson(s);
        //       logger.info("Json Received() - {}", weatherJson);

        weatherTelegramBot.sendMessage(weatherJson,message);
    }

}
