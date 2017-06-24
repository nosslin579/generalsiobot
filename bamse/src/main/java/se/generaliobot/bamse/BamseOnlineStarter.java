package se.generaliobot.bamse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.Games;
import pl.joegreen.sergeants.framework.model.GameResult;
import pl.joegreen.sergeants.framework.queue.QueueConfiguration;
import pl.joegreen.sergeants.framework.user.UserConfiguration;
import se.generaliobot.bamse.config.Config;

public class BamseOnlineStarter {
    private final static Logger log = LoggerFactory.getLogger(BamseOnlineStarter.class);

    public static void main(String[] args) throws InterruptedException {
        UserConfiguration userConfiguration = UserConfiguration.onlyUserId("dc42d51f5b7246119e39302962b72b4f001");
        GameResult gameResult = Games.play(1, Bamse.provider(new Config()), QueueConfiguration.oneVsOne(), userConfiguration).get(0);
        log.info("Done! {}", gameResult);
    }
}
