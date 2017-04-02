package se.nosslin579.aardvark.config;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ConfigCreator {

    private Random random = new Random();


    public Config randomizeConfig(Config config) {
        int field = random.nextInt(ConfigModifier.values().length - 1);
        ConfigModifier configModifier = ConfigModifier.values()[field];
        Double currentValue = configModifier.getSupplier().apply(config);
        double newValue = currentValue + (random.nextDouble() * .20d) - .10d;
        configModifier.getConsumer().accept(config, newValue);
        config.setId(0);
        return config;
    }

    public static void main(String[] args) {
        ConfigCreator configCreator = new ConfigCreator();
        Config c = Repo.getInstance().getConfig(1);
        Config config = configCreator.randomizeConfig(c);
        System.out.println(config);
    }

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

}
