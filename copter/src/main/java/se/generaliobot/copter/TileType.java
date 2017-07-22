package se.generaliobot.copter;

import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.FieldTerrainType;

public enum TileType {
    UNKNOWN('U') {
        @Override
        TileType getByField(Field field) {
            if (!field.isVisible()) {
                return field.getTerrainType() == FieldTerrainType.FOG ? FOG : OBSTACLE;
            } else if (field.asVisibleField().isOwnedByMe()) {
                return OWN_CROWN;
            } else if (field.asVisibleField().isBlank()) {
                return EMPTY;
            } else if (field.asVisibleField().isCity()) {
                return EMPTY_CITY;
            } else if (field.asVisibleField().isObstacle()) {
                return MOUNTAIN;
            }
            throw new IllegalStateException("Unknown field " + field);
        }
    },
    EMPTY(' ') {
        @Override
        TileType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().hasOwner()) {
                return field.asVisibleField().isOwnedByMe() ? OWN : ENEMY;
            }
            return EMPTY;
        }
    },
    ENEMY('-') {
        @Override
        TileType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().isOwnedByMe()) {
                return OWN;
            }
            return ENEMY;
        }
    },
    OBSTACLE('M') {
        @Override
        TileType getByField(Field field) {
            if (field.isVisible()) {
                return field.asVisibleField().isCity() ? EMPTY_CITY : MOUNTAIN;
            }
            return OBSTACLE;
        }
    },
    OWN('+') {
        @Override
        TileType getByField(Field field) {
            if (!field.isVisible() || field.asVisibleField().isOwnedByEnemy()) {
                return ENEMY;
            }
            return OWN;
        }

    },
    OWN_CITY('C') {
        @Override
        TileType getByField(Field field) {
            if (field.asVisibleField().isOwnedByEnemy()) {
                return ENEMY_CITY;
            }
            return OWN_CITY;
        }
    },
    EMPTY_CITY('C') {
        @Override
        TileType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().hasOwner()) {
                return field.asVisibleField().isOwnedByMe() ? OWN_CITY : ENEMY_CITY;
            }
            return EMPTY_CITY;
        }
    },
    ENEMY_CITY('C') {
        @Override
        TileType getByField(Field field) {
            if (field.isVisible() && field.asVisibleField().isOwnedByMe()) {
                return OWN_CITY;
            }
            return ENEMY_CITY;
        }
    },
    FOG('?') {
        @Override
        TileType getByField(Field field) {
            if (field.isVisible()) {
                if (field.asVisibleField().isGeneral()) {
                    return ENEMY_CROWN;
                }
                return field.asVisibleField().isOwnedByEnemy() ? ENEMY : EMPTY;
            }
            return FOG;
        }
    },
    MOUNTAIN('M'),
    OWN_CROWN('X'),
    ENEMY_CROWN('X');

    private final char symbol;

    TileType(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    TileType getByField(Field field) {
        return this;
    }

}