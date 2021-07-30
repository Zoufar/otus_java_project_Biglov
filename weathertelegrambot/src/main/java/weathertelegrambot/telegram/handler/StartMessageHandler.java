package weathertelegrambot.telegram.handler;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import weathertelegrambot.telegram.WeatherTelegramBot;


@Component
@RequiredArgsConstructor
public class StartMessageHandler implements MessageHandler {
    private final WeatherTelegramBot weatherTelegramBot;

    @Override
    public String handle(Message message) {
        if (message.hasText() &&
                (message.getText().startsWith("/start")||message.getText().startsWith("/help"))) {
            String chatId = message.getChatId().toString();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            if (message.getText().startsWith("/start")) {
                String sendMessageText = "Здравствуйте! Отправьте Ваши координаты, плизз ";
                sendMessage.setText(sendMessageText);
                weatherTelegramBot.sendQueue.add(sendMessage);
                return sendMessageText;
            } else {
                String sendMessageText = "Чтобы отправить Ваши координаты, " +
                        "используйте иконку \"прикрепить\" в правом углу строки ввода, " +
                        "далее выберите \"Location\"";
                sendMessage.setText(sendMessageText);
                weatherTelegramBot.sendQueue.add(sendMessage);
                return sendMessageText;
            }
        }
        return StringUtils.EMPTY;
    }
}
