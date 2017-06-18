package se.nosslin579.trainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.Games;
import pl.joegreen.sergeants.framework.model.GameResult;
import pl.joegreen.sergeants.framework.queue.QueueConfiguration;
import pl.joegreen.sergeants.framework.user.UserConfiguration;
import se.nosslin579.aardvark.Aardvark;
import se.nosslin579.aardvark.config.Config;

import java.util.concurrent.TimeUnit;

public class OnlineStarter {
    private final static Logger log = LoggerFactory.getLogger(OnlineStarter.class);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            UserConfiguration userConfiguration = UserConfiguration.onlyUserId("dc42d51f-5b72-4611-9e39-302962b72b4f");
            GameResult gameResult = Games.play(1, Aardvark.provider(new Config()),
                    QueueConfiguration.oneVsOne(), userConfiguration).get(0);
            log.info("##### {}", gameResult);
            Thread.sleep(TimeUnit.MINUTES.toMillis(1));
        }
        log.info("No more games");
    }
}
