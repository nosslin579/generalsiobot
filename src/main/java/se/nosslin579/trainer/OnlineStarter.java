package se.nosslin579.trainer;

import pl.joegreen.sergeants.framework.Games;
import pl.joegreen.sergeants.framework.queue.QueueConfiguration;
import pl.joegreen.sergeants.framework.user.UserConfiguration;
import se.nosslin579.aardvark.Aardvark;

public class OnlineStarter {
    public static void main(String[] args) {
        Games.play(1, Aardvark.provider(new se.nosslin579.aardvark.config.Config()),
                QueueConfiguration.oneVsOne(), UserConfiguration.random())
                .forEach(System.out::println);

    }
}
