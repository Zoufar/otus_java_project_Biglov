package weathertelegrambot.telegram.handler;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.scheduler.Schedulers;
import weathertelegrambot.parsers.WeatherJsonParser;
import weathertelegrambot.service.WeatherService;
import weathertelegrambot.telegram.WeatherTelegramBot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Component
@RequiredArgsConstructor
public class CoordsnMessageHandler implements MessageHandler{

    private static final Logger logger = LoggerFactory.getLogger(CoordsnMessageHandler.class);

    private final WeatherTelegramBot weatherTelegramBot;
    private final List<WeatherService> weatherServices;

    @Override
    public String handle(Message message) {
        List <Double> coords = new ArrayList<>();
        if (message.hasText() && message.getText().startsWith("/coord")) {
            coords = getCoords(message);

            if (coords.get(0) < 90.0) {
            String userLat = coords.get(0).toString();
            String userLon = coords.get(1).toString();

            logger.info("handle() - trace: location: latitude = {}, longitude = {}", userLat, userLon);

            weatherServices.stream()
                    .map(weatherService -> weatherService.getWeatherByCoords(userLat, userLon).
                            subscribeOn(Schedulers.boundedElastic()).
                            subscribe(s -> sendMessage(s, message, weatherService.getParser())))
                    .collect(Collectors.toList());

            return "Получил координаты: "+ userLat+ ", " + userLon;
        }
            return "Получил ошибочную строку координат";
        }
        return StringUtils.EMPTY;
    }

    private List<Double> getCoords (Message message) {
        String trimText = message.getText().substring(6).trim().replace("- ", "-");
        List<String> commandText = new ArrayList<>();
        List <Double> coords = new ArrayList<>();

        if (trimText.contains(" ")) {
            int indexOfSpace = trimText.indexOf(" ");
            commandText.add(trimText.substring(0, indexOfSpace));
            commandText.add(trimText.substring(indexOfSpace + 1));
        } else
        {coords.add(1000.0);
         coords.add(0.0);
        sendFailMessage("координаты должны разделяться пробелом", message);
        return coords;
        }
        try{
        coords.add(Double.parseDouble(commandText.get(0)));
        coords.add(Double.parseDouble(commandText.get(1)));
        }
        catch (NumberFormatException e) {
            System.out.println ("COORDS :\n"+ coords);
            coords = asList(1000.0, 0.0);
            sendFailMessage("неправильный формат числа \n"
                           +"используйте точку в качестве разделителя", message);
            return coords;
            }
        if (coords.get(0) > 90.0 || coords.get(0) < -90.0){
            sendFailMessage("величина широты за пределами допустимого диапазона", message);
            coords.set(0, 1000.0);
        }
        if (coords.get(1) > 180.0 || coords.get(1) < -180.0){
            sendFailMessage("величина долготы за пределами допустимого диапазона", message);
            coords.set(0, 1000.0);
        }

        return coords;
    }

    private void sendMessage(String s, Message message, WeatherJsonParser parser ){
        String weatherJson = parser.parseWeatherJson(s);
        //       logger.info("Json Received() - {}", weatherJson);

        weatherTelegramBot.sendMessage(weatherJson,message);
    }

    private void sendFailMessage(String sendText, Message message){

        weatherTelegramBot.sendMessage(sendText,message);
    }

}

