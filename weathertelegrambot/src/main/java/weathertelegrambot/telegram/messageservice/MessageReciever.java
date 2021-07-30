package weathertelegrambot.telegram.messageservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weathertelegrambot.telegram.WeatherTelegramBot;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import weathertelegrambot.telegram.handler.MessageHandler;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageReciever implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MessageReciever.class);
    private final int WAIT_FOR_NEW_MESSAGE_DELAY = 1000;
    private final WeatherTelegramBot weatherTelegramBot;
    private final List<MessageHandler> handlers;


    @Override
    public void run() {
        logger.info("[STARTED] MsgReciever.  " );
        while (true) {
            for (Update update = weatherTelegramBot.receiveQueue.poll();
                        update != null; update = weatherTelegramBot.receiveQueue.poll()) {
                logger.debug("New update to analyze in queue " + update.toString());

                Message receivedMessage = update.getMessage();
                String sendText = findHandler (receivedMessage);
                logger.info("onUpdateReceived() - reciever trace: sendText = {}", sendText);
            }
            try {
                Thread.sleep(WAIT_FOR_NEW_MESSAGE_DELAY);
            } catch (InterruptedException e) {
                logger.error("Catch interrupt. Exit", e);
                return;
            }
        }
    }

    private String findHandler(Message receivedMessage) {
        return handlers.stream()
                .map(handler -> handler.handle(receivedMessage))
                .filter(StringUtils::isNotBlank)
                .findAny()
                .orElse("No matching handler");
    }

}


