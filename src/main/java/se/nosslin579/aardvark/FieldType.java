package se.nosslin579.aardvark;

import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.FieldTerrainType;
import se.nosslin579.aardvark.config.Config;

import java.util.function.Function;

public enum FieldType {
    UNKNOWN('U', config -> {
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
    EMPTY(' ', Config::getEmptyPenalty) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().hasOwner()) {
                return field.asVisibleField().isOwnedByMe() ? OWN : ENEMY;
            }
            return EMPTY;
        }
    },
    ENEMY('-', Config::getEnemyPenalty) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().isOwnedByMe()) {
                return OWN;
            }
            return ENEMY;
        }
    },
    OBSTACLE('M', Config::getObstaclePenalty) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible()) {
                return field.asVisibleField().isCity() ? CITY : MOUNTAIN;
            }
            return OBSTACLE;
        }
    },
    OWN('+', Config::getOwnPenalty) {
        @Override
        FieldType getByField(Field field) {
            if (!field.isVisible() || field.asVisibleField().isOwnedByEnemy()) {
                return ENEMY;
            }
            return OWN;
        }

        @Override
        public Double getPenalty(Config config, int army) {
            if (army == 1) {
                return config.getOwnPenalty();
            }
            return config.getOwnPenalty() + army * -0.1d;
        }
    },
    OWN_CITY('C', Config::getOwnCityPenalty) {
        @Override
        FieldType getByField(Field field) {
            if (field.asVisibleField().isOwnedByEnemy()) {
                return ENEMY_CITY;
            }
            return OWN_CITY;
        }
    },
    CITY('C', Config::getCityPenalty) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().hasOwner()) {
                return field.asVisibleField().isOwnedByMe() ? OWN_CITY : ENEMY_CITY;
            }
            return CITY;
        }
    },
    ENEMY_CITY('C', Config::getEnemyCityPenalty) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().isOwnedByMe()) {
                return OWN_CITY;
            }
            return ENEMY_CITY;
        }
    },
    FOG('?', Config::getFogPenalty) {
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
    MOUNTAIN('M', config -> 666d),
    OWN_CROWN('X', Config::getOwnCrownPenalty),
    ENEMY_CROWN('X', Config::getEnemyCrownPenalty);

    private final char symbol;
    private final Function<Config, Double> configPenalty;

    FieldType(char symbol, Function<Config, Double> configPenalty) {
        this.symbol = symbol;
        this.configPenalty = configPenalty;
    }

    public char getSymbol() {
        return symbol;
    }

    FieldType getByField(Field field) {
        return this;
    }

    public Double getPenalty(Config config) {
        return configPenalty.apply(config);
    }

    public Double getPenalty(Config config, int army) {
        return getPenalty(config);
    }
}