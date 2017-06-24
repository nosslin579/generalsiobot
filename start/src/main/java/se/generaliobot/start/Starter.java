package se.generaliobot.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.Games;
import pl.joegreen.sergeants.framework.model.GameResult;
import pl.joegreen.sergeants.framework.queue.QueueConfiguration;
import pl.joegreen.sergeants.framework.user.UserConfiguration;
import se.generaliobot.aardvark.Aardvark;
import se.generaliobot.bamse.Bamse;
import se.generaliobot.bamse.config.Config;

import java.util.Objects;

public class Starter {
    private final static Logger log = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 1 && Objects.equals(args[0], "Aardvark")) {
            log.info("Starting Aardvark");
            UserConfiguration userConfiguration = UserConfiguration.onlyUserId("dc42d51f-5b72-4611-9e39-302962b72b4f");
            se.generaliobot.aardvark.config.Config config = new se.generaliobot.aardvark.config.Config();
            GameResult gameResult = Games.play(1, Aardvark.provider(config), QueueConfiguration.oneVsOne(), userConfiguration).get(0);
            log.info("Done! {}", gameResult);
        } else {
            log.info("Starting Bamse");
            UserConfiguration userConfiguration = UserConfiguration.onlyUserId("dc42d51f5b7246119e39302962b72b4f001");
            GameResult gameResult = Games.play(1, Bamse.provider(new Config()), QueueConfiguration.oneVsOne(), userConfiguration).get(0);
            log.info("Done! {}", gameResult);
        }

    }
}