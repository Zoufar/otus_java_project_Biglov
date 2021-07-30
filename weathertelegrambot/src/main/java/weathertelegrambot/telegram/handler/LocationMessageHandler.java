package weathertelegrambot.telegram.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import weathertelegrambot.parsers.WeatherJsonParser;
import weathertelegrambot.parsers.WeatherOpenMapParser;
import weathertelegrambot.parsers.WeatherYandexParser;
import weathertelegrambot.service.WeatherOpenMapService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weathertelegrambot.service.WeatherYandexService;
import reactor.core.scheduler.Schedulers;
import weathertelegrambot.telegram.WeatherTelegramBot;


@Component
@RequiredArgsConstructor
public class LocationMessageHandler implements MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(LocationMessageHandler.class);

    private final WeatherOpenMapService weatherOpenMapService;
    private final WeatherYandexService weatherYandexService;
    private final WeatherOpenMapParser weatherOpenMapParser;
    private final WeatherYandexParser weatherYandexParser;
    private final WeatherTelegramBot weatherTelegramBot;



    @Override
    public String handle(Message message) {
        if (message.hasLocation()) {
            String userLon = message.getLocation().getLongitude().toString();
            String userLat = message.getLocation().getLatitude().toString();
            logger.info("handle() - trace: location: longitude = {}, latitude = {}", userLon, userLat);

            weatherOpenMapService.getWeatherByCoords(userLat, userLon).
                     subscribeOn(Schedulers.boundedElastic()).
                     subscribe(s -> sendMessage(s, message, weatherOpenMapParser));
//            System.out.println("weatherJson: \n" + weatherJsonOMP+"\n\n");

            weatherYandexService.getWeatherByCoords(userLat, userLon).
                    subscribeOn(Schedulers.boundedElastic()).
                    subscribe(s -> sendMessage(s, message, weatherYandexParser));
//            System.out.println("weatherJsonYandex: \n" + weatherJsonYa+"\n\n");

            return "Получил координаты: "+ userLat+ ", " + userLon;


        }

        return StringUtils.EMPTY;
    }

    private void sendMessage(String s, Message message, WeatherJsonParser parser ){
        String weatherJson = parser.parseWeatherJson(s);

        logger.info("Json Received() - {}", weatherJson);

        String chatId = message.getChatId().toString();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(weatherJson);
        sendMessage.setChatId(chatId);

        weatherTelegramBot.sendQueue.add(sendMessage);

    }


}
