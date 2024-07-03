package cloud.lemonslice.silveroak.helper;

import net.minecraft.core.Direction;

public class BlockHelper {
    public static Direction getNextHorizontal(Direction facing) {
        return facing.getClockWise();
    }

    public static Direction getPreviousHorizontal(Direction facing) {
        return switch (facing) {
            case NORTH -> Direction.WEST;
            case SOUTH -> Direction.EAST;
            case WEST -> Direction.SOUTH;
            case EAST -> Direction.NORTH;
            default -> throw new IllegalStateException("Unable to get Y-rotated facing of " + facing);
        };
    }
}
