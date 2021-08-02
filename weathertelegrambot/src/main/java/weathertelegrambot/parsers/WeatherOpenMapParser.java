package weathertelegrambot.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeatherOpenMapParser implements WeatherJsonParser {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public String parseWeatherJson(String json) {
        String temp = "локальная температура по OpenWeatherMap, градусы Цельсия: ";
        try{
        JsonNode mainNode = objectMapper.readTree(json).get("main");
        temp += mainNode.get("temp").toString()+"\n";}
        catch (JsonProcessingException e ) {
            e.printStackTrace();
        }
        return temp;
    }
}
