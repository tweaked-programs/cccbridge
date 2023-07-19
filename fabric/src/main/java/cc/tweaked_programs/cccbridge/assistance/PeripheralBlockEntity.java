package cc.tweaked_programs.cccbridge.assistance;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface is used to give TileEntities the ability to provide its Peripheral.
 * By implementing this, it can be made sure that the object has this ability for sure.
 *
 * @version 1.0
 */
public interface PeripheralBlockEntity {
    /**
     * Returns a peripheral. Can be null, if the TileEntity rejects the request.
     *
     * @param side From which side is the block called?
     * @return null or Peripheral for the TileEntity.
     */
    @Nullable
    IPeripheral getPeripheral(@NotNull Direction side);
}
