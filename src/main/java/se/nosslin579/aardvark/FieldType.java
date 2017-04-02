package se.nosslin579.aardvark;

import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.FieldTerrainType;

public enum FieldType {
    UNKNOWN('U', 0d) {
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
    EMPTY(' ', 1d) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().hasOwner()) {
                return field.asVisibleField().isOwnedByMe() ? OWN : ENEMY;
            }
            return EMPTY;
        }
    },
    ENEMY('-', .9) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().isOwnedByMe()) {
                return OWN;
            }
            return ENEMY;
        }
    },
    OBSTACLE('M', Double.MAX_VALUE) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible()) {
                return field.asVisibleField().isCity() ? CITY : MOUNTAIN;
            }
            return OBSTACLE;
        }
    },
    OWN('+', 1.5) {
        @Override
        FieldType getByField(Field field) {
            if (field.asVisibleField().isOwnedByEnemy()) {
                return ENEMY;
            }
            return OWN;
        }
    },
    OWN_CITY('C', 0.4) {
        @Override
        FieldType getByField(Field field) {
            if (field.asVisibleField().isOwnedByEnemy()) {
                return ENEMY_CITY;
            }
            return OWN_CITY;
        }
    },
    CITY('C', Double.MAX_VALUE) {
        @Override
        FieldType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().hasOwner()) {
                return field.asVisibleField().isOwnedByMe() ? OWN_CITY : ENEMY_CITY;
            }
            return CITY;
        }
    },
    ENEMY_CITY('C', 5d) {
        @Override
        FieldType getByField(Field field) {
            if (field.asVisibleField().isOwnedByMe()) {
                return OWN_CITY;
            }
            return ENEMY_CITY;
        }
    },
    FOG('?', 1d) {
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
    MOUNTAIN('M', Double.MAX_VALUE),
    OWN_CROWN('X', 2d),
    ENEMY_CROWN('X', -100d);

    private final char symbol;
    private final Double penalty;

    FieldType(char symbol, Double penalty) {
        this.symbol = symbol;
        this.penalty = penalty;
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
}