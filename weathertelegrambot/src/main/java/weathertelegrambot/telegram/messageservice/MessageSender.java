package weathertelegrambot.telegram.messageservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import weathertelegrambot.telegram.WeatherTelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageSender implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);
    private final int SENDER_SLEEP_TIME = 1000;
    private final WeatherTelegramBot weatherTelegramBot;

    @Override
    public void run() {
        logger.info("[STARTED] MsgSender. " );
        try {
            while (true) {
                for (SendMessage message = weatherTelegramBot.sendQueue.poll();
                     message != null; message = weatherTelegramBot.sendQueue.poll()) {
                    logger.info("Get new msg to send by sender:" + message);
                    send(message);
                }
                try {
                    Thread.sleep(SENDER_SLEEP_TIME);
                } catch (InterruptedException e) {
                    logger.error("Interrupted while operate msg list", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(SendMessage message) {
        try {
            weatherTelegramBot.execute(message);
            } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
