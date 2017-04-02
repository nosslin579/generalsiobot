package se.nosslin579.aardvark;

import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.FieldTerrainType;
import se.nosslin579.aardvark.config.Config;

import java.util.function.Function;

public enum FieldType {
    UNKNOWN('U', 0d, config -> {
        throw new IllegalStateException("Penalty for unknown not allowed");
    }) {
        @Override
        FieldType getByField(Field field) {
            if (!field.isVisible()) {
                return field.getTerrainType() == FieldTerrainType.FOG ? FOG : OBSTACLE;
            } else if (field.asVisibleField().isOwnedByMe()) {
                return OWN_CROWN;
            } else if (field.asVisibleField().isBlank()) {
                return EMPTY;
            } else if (field.asVisibleField().isCity()) {
                return CITY;
            } else if (field.asVisibleField().isObstacle()) {
                return MOUNTAIN;
            }
            throw new IllegalStateException("Unknown field " + field);
        }
    },
    EMPTY(' ', 1d, Config::getEmptyPenalty) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().hasOwner()) {
                return field.asVisibleField().isOwnedByMe() ? OWN : ENEMY;
            }
            return EMPTY;
        }
    },
    ENEMY('-', .9, Config::getEnemyPenalty) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().isOwnedByMe()) {
                return OWN;
            }
            return ENEMY;
        }
    },
    OBSTACLE('M', 100d, Config::getObstaclePenalty) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible()) {
                return field.asVisibleField().isCity() ? CITY : MOUNTAIN;
            }
            return OBSTACLE;
        }
    },
    OWN('+', 1.5, Config::getOwnPenalty) {
        @Override
        FieldType getByField(Field field) {
            if (!field.isVisible() || field.asVisibleField().isOwnedByEnemy()) {
                return ENEMY;
            }
            return OWN;
        }
    },
    OWN_CITY('C', 0.4, Config::getOwnCityPenalty) {
        @Override
        FieldType getByField(Field field) {
            if (field.asVisibleField().isOwnedByEnemy()) {
                return ENEMY_CITY;
            }
            return OWN_CITY;
        }
    },
    CITY('C', 50d, Config::getCityPenalty) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().hasOwner()) {
                return field.asVisibleField().isOwnedByMe() ? OWN_CITY : ENEMY_CITY;
            }
            return CITY;
        }
    },
    ENEMY_CITY('C', 5d, Config::getEnemyCityPenalty) {
        @Override
        FieldType getByField(Field field) {
            if (field.asVisibleField().isOwnedByMe()) {
                return OWN_CITY;
            }
            return ENEMY_CITY;
        }
    },
    FOG('?', 1d, Config::getFogPenalty) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible()) {
                if (field.asVisibleField().isGeneral()) {
                    return ENEMY_CROWN;
                }
                return field.asVisibleField().isOwnedByEnemy() ? ENEMY : EMPTY;
            }
            return FOG;
        }
    },
    MOUNTAIN('M', 666d, config -> 666d),
    OWN_CROWN('X', 2d, Config::getOwnCrownPenalty),
    ENEMY_CROWN('X', -100d, Config::getEnemyCrownPenalty);

    private final char symbol;
    private final Double penalty;
    private final Function<Config, Double> configPenalty;

    FieldType(char symbol, Double penalty, Function<Config, Double> configPenalty) {
        this.symbol = symbol;
        this.penalty = penalty;
        this.configPenalty = configPenalty;
    }

    public Double getPenalty() {
        return penalty;
    }

    public char getSymbol() {
        return symbol;
    }

    FieldType getByField(Field field) {
        return this;
    }

    public Double getPenalty(Config config) {
        Double penalty = configPenalty.apply(config);
        return penalty == null ? this.penalty : penalty;
    }
}