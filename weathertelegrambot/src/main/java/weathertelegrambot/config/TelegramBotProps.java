package weathertelegrambot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.telegram-bot")
public class TelegramBotProps {

    private final String name;
    private final String token;
    private final String ompapikey;
    private final String yandexapikey;
}
