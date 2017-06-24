package se.generaliobot.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.api.GeneralsApi;

public class SetUserName {
    private final static Logger log = LoggerFactory.getLogger(SetUserName.class);

    public static void main(String[] args) throws InterruptedException {

        GeneralsApi generalsApi = GeneralsApi.create();
        generalsApi.connect();
        generalsApi.onConnected(() -> {
            log.info("Setting name");
            generalsApi.setUsername("dc42d51f5b7246119e39302962b72b4f004", "[Bot] Copter");
        });
        generalsApi.onSetUsernameError(event -> {
            log.error("Set name failed {}", event);

        });
        Thread.sleep(5000);
        generalsApi.disconnect();

    }
}
