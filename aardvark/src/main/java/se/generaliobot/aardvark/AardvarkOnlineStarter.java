package se.generaliobot.aardvark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.Games;
import pl.joegreen.sergeants.framework.model.GameResult;
import pl.joegreen.sergeants.framework.queue.QueueConfiguration;
import pl.joegreen.sergeants.framework.user.UserConfiguration;
import se.generaliobot.aardvark.config.Config;

public class AardvarkOnlineStarter {
    private final static Logger log = LoggerFactory.getLogger(AardvarkOnlineStarter.class);

    public static void main(String[] args) throws InterruptedException {
        UserConfiguration userConfiguration = UserConfiguration.onlyUserId("dc42d51f-5b72-4611-9e39-302962b72b4f");
        GameResult gameResult = Games.play(1, Aardvark.provider(new Config()),
                QueueConfiguration.oneVsOne(), userConfiguration).get(0);
        log.info("Done! {}", gameResult);
    }
}