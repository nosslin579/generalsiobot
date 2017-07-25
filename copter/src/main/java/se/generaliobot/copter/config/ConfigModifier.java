package se.generaliobot.copter.config;

import java.util.function.BiConsumer;
import java.util.function.Function;

enum ConfigModifier {
    V1(Config::getSniffEmptyPenalty, Config::setSniffEmptyPenalty),
    V2(Config::getSniffEnemyPenalty, Config::setSniffEnemyPenalty),
    V3(Config::getSniffFogPenalty, Config::setSniffFogPenalty),
    V4(Config::getWinAttemptEmptyPenalty, Config::setWinAttemptEmptyPenalty),
    V5(Config::getWinAttemptFogPenalty, Config::setWinAttemptFogPenalty),
    V6(Config::getWinAttemptOwnPenalty, Config::setWinAttemptOwnPenalty);

    private final Function<Config, Double> supplier;
    private final BiConsumer<Config, Double> consumer;

    ConfigModifier(Function<Config, Double> supplier, BiConsumer<Config, Double> consumer) {
        this.supplier = supplier;
        this.consumer = consumer;
    }

    public BiConsumer<Config, Double> getConsumer() {
        return consumer;
    }

    public Function<Config, Double> getSupplier() {
        return supplier;
    }
}
