package se.generaliobot.copter.config;

import java.util.function.BiConsumer;
import java.util.function.Function;

enum ConfigModifier {
    EMPTY_PENALTY(Config::getEmptyScore, Config::setEmptyScore),
    ENEMY_PENALTY(Config::getEnemyScore, Config::setEnemyScore),
    OBSTACLE_PENALTY(Config::getObstacleScore, Config::setCityScore),
    OWN_PENALTY(Config::getOwnScore, Config::setOwnScore),
    OWN_CITY_PENALTY(Config::getOwnCityScore, Config::setOwnCityScore),
    CITY_PENALTY(Config::getCityScore, Config::setCityScore),
    ENEMY_CITY_PENALTY(Config::getEnemyCityScore, Config::setEnemyCityScore),
    FOG_PENALTY(Config::getFogScore, Config::setFogScore),
    OWN_CROWN_PENALTY(Config::getOwnCrownScore, Config::setOwnCrownScore),
    ENEMY_CROWN_PENALTY(Config::getEnemyCrownScore, Config::setEnemyCrownScore);


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
