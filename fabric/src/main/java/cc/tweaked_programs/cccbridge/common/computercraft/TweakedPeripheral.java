package cc.tweaked_programs.cccbridge.common.computercraft;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * I did not steal the name from Create. I swear!
 * No seriously!
 */
public interface TweakedPeripheral<BE extends BlockEntity> extends IPeripheral {
    List<IComputerAccess> computers = new LinkedList<>();

    default void sendEvent(@Nonnull String event, @Nullable Object... arguments) {
        for (IComputerAccess pc : computers)
            pc.queueEvent(event, pc.getAttachmentName(), arguments);
    }

    @Override
    default void attach(@Nonnull IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    default void detach(@Nonnull IComputerAccess computer) {
        computers.removeIf(p -> (p.getID() == computer.getID()));
    }

    @NotNull
    @Override
    String getType();

    @Nullable
    @Override
    BE getTarget();

    static double getVersion() {
        return 0.0D;
    }

    @Override
    default boolean equals(@Nullable IPeripheral other) {
        return other == this && other.getType().equals(getType()) && other.getTarget() == this.getTarget();
    }
}
