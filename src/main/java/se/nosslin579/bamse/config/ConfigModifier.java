package se.nosslin579.bamse.config;

import java.util.function.BiConsumer;
import java.util.function.Function;

enum ConfigModifier {
    EMPTY_PENALTY(Config::getEmptyPenalty, Config::setEmptyPenalty),
    ENEMY_PENALTY(Config::getEnemyPenalty, Config::setEnemyPenalty),
    OBSTACLE_PENALTY(Config::getObstaclePenalty, Config::setCityPenalty),
    OWN_PENALTY(Config::getOwnPenalty, Config::setOwnPenalty),
    OWN_CITY_PENALTY(Config::getOwnCityPenalty, Config::setOwnCityPenalty),
    CITY_PENALTY(Config::getCityPenalty, Config::setCityPenalty),
    ENEMY_CITY_PENALTY(Config::getEnemyCityPenalty, Config::setEnemyCityPenalty),
    FOG_PENALTY(Config::getFogPenalty, Config::setFogPenalty),
    OWN_CROWN_PENALTY(Config::getOwnCrownPenalty, Config::setOwnCrownPenalty),
    ENEMY_CROWN_PENALTY(Config::getEnemyCrownPenalty, Config::setEnemyCrownPenalty);


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
