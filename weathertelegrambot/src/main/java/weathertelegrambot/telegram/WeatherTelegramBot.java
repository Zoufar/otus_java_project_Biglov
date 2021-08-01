package weathertelegrambot.telegram;

import org.telegram.telegrambots.meta.api.objects.Message;
import weathertelegrambot.config.TelegramBotProps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service("weatherBot")
@RequiredArgsConstructor
public class WeatherTelegramBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(WeatherTelegramBot.class);

    private final TelegramBotProps props;

    public final Queue<SendMessage> sendQueue = new ConcurrentLinkedQueue<>();
    public final Queue<Update> receiveQueue = new ConcurrentLinkedQueue<>();

    @Override
    public String getBotUsername() {
        return props.getName();
    }

    @Override
    public String getBotToken() { return props.getToken(); }

    @Override
    public void onUpdateReceived(Update update) {
            logger.debug("onUpdateReceived() - start: updateID = {}", update.getUpdateId());

            receiveQueue.add(update);
    }

    public void sendMessage(String sendText, Message message ){

        String chatId = message.getChatId().toString();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(sendText);
        sendMessage.setChatId(chatId);

        this.sendQueue.add(sendMessage);

    }
}
