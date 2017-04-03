package se.nosslin579.aardvark.config;

import java.util.Random;

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

}
