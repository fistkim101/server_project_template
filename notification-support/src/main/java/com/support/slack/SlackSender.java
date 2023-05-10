package com.support.slack;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Component
@Slf4j
public class SlackSender {
    private final Slack slack = Slack.getInstance();

    @Value("${slack.webhook-url}")
    private String webHookUrl;

    public void sendMessage(String message) {
        Payload payload = Payload.builder()
                .text(message)
                .build();

        WebhookResponse response = null;
        try {
            response = slack.send(webHookUrl, payload);
        } catch (IOException ignored) {
            // ignore checked exception
        } catch (Exception exception) {
            StringWriter stringWriter = new StringWriter();
            exception.printStackTrace(new PrintWriter(stringWriter));
            String exceptionContents = stringWriter.toString();
            log.error(exceptionContents);
        }
    }

}
