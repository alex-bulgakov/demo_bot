package com.bulgakov.demo_bot.service;

import com.bulgakov.demo_bot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        if (update.hasMessage() && msg.hasText()) {
            String message = msg.getText();
            long chatId = msg.getChatId();

            switch (message) {
                case "/start": startCommandReceived(chatId, msg.getChat().getFirstName());
                    break;
                default: sendMessage(chatId, "Your command is wrong, use /start command");
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Привет " + name;
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
