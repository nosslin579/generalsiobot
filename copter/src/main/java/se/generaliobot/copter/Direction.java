package se.generaliobot.copter;

public enum Direction {
    RIGHT, LEFT, DOWN, UP;

    public Direction getOpposite() {
        switch (this) {
            case RIGHT:
                return LEFT;
            case LEFT:
                return RIGHT;
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            default:
                throw new IllegalStateException("Wut");
        }
    }
}
