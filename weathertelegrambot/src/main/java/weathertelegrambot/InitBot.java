package weathertelegrambot;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import weathertelegrambot.telegram.WeatherTelegramBot;
import weathertelegrambot.telegram.messageservice.MessageReciever;
import weathertelegrambot.telegram.messageservice.MessageSender;

@RequiredArgsConstructor
@Component("initBot")
public class InitBot {
    private static final Logger logger = LoggerFactory.getLogger(InitBot.class);
    private static final String BOT_ADMIN = "1751261250";
    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 3;

    private final WeatherTelegramBot weatherTelegramBot;
    private final MessageReciever messageReciever;
    private final MessageSender messageSender;

    void start(){

        Thread receiver = new Thread(messageReciever);
        receiver.setDaemon(true);
        receiver.setName("MsgReciever");
        receiver.setPriority(PRIORITY_FOR_RECEIVER);
        receiver.start();

        Thread sender = new Thread(messageSender);
        sender.setDaemon(true);
        sender.setName("MsgSender");
        sender.setPriority(PRIORITY_FOR_SENDER);
        sender.start();

        SendMessage sendMessage = new SendMessage();
        try{
            sendMessage.setChatId(BOT_ADMIN);
            sendMessage.setText("Запустился");
            weatherTelegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
