package se.generaliobot.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.Actions;
import pl.joegreen.sergeants.framework.Bot;
import pl.joegreen.sergeants.framework.Games;
import pl.joegreen.sergeants.framework.model.GameResult;
import pl.joegreen.sergeants.framework.queue.QueueConfiguration;
import pl.joegreen.sergeants.framework.user.UserConfiguration;
import se.generaliobot.aardvark.Aardvark;
import se.generaliobot.bamse.Bamse;
import se.generaliobot.copter.Copter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Starter {
    private final static Logger log = LoggerFactory.getLogger(Starter.class);
    private static Map<String, RegisteredBot> registeredBots = new HashMap<>();

    static {
        registeredBots.put("Aardvark", new RegisteredBot("dc42d51f-5b72-4611-9e39-302962b72b4f", Aardvark.provider(new se.generaliobot.aardvark.config.Config())));
        registeredBots.put("Bamse", new RegisteredBot("dc42d51f5b7246119e39302962b72b4f001", Bamse.provider(new se.generaliobot.bamse.config.Config())));
        registeredBots.put("Copter", new RegisteredBot("dc42d51f5b7246119e39302962b72b4f004", Copter.provider(new se.generaliobot.copter.config.Config())));
    }

    public static void main(String[] args) throws InterruptedException {
        log.info("Starting with arg:{}", Arrays.toString(args));
        if (args.length != 1) {
            return;
        }
        String botName = args[0];
        RegisteredBot registeredBot = registeredBots.get(botName);
        UserConfiguration userConfiguration = UserConfiguration.onlyUserId(registeredBot.getUserId());
        GameResult gameResult = Games.play(1, registeredBot.getProvider(), QueueConfiguration.oneVsOne(), userConfiguration).get(0);
        log.info("Done! {}", gameResult);
    }

    private static class RegisteredBot {
        private final String userId;
        private final Function<Actions, Bot> provider;

        public RegisteredBot(String userId, Function<Actions, Bot> provider) {
            this.userId = userId;
            this.provider = provider;
        }

        public Function<Actions, Bot> getProvider() {
            return provider;
        }

        public String getUserId() {
            return userId;
        }

    }
}