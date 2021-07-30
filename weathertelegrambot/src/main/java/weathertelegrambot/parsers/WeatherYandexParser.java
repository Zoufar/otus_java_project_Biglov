package weathertelegrambot.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeatherYandexParser implements WeatherJsonParser {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public String parseWeatherJson(String json){
        String forecast = "Прогноз температуры Yandex (градусы Цельсия): \n";
        try{
            JsonNode forecastsArrNode = objectMapper.readTree(json).get("forecasts");

            if (forecastsArrNode.isArray()) {
                for (final JsonNode objNode : forecastsArrNode) {

            forecast += objNode.get("date").toString()
                       + ":   "
                       + objNode.get("parts")
                         .get("day").get("temp_avg").toString() + "\n";
            }
            }
            }
        catch (JsonProcessingException e ) {
            e.printStackTrace();
        }
        return forecast;
    }
}
