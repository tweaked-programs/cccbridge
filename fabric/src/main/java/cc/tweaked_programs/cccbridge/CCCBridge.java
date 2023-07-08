package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.block.*;
import cc.tweaked_programs.cccbridge.blockEntity.*;
import cc.tweaked_programs.cccbridge.display.SourceBlockDisplaySource;
import cc.tweaked_programs.cccbridge.display.TargetBlockDisplayTarget;
import cc.tweaked_programs.cccbridge.entity.animatronic.AnimatronicEntity;
import cc.tweaked_programs.cccbridge.entity.animatronic.AnimatronicModel;
import cc.tweaked_programs.cccbridge.entity.animatronic.AnimatronicRenderer;
import com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours;
import dan200.computercraft.api.ComputerCraftAPI;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.Motive;

public class CCCBridge implements ModInitializer {
    public static final String MOD_ID = "cccbridge";
    //public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Block (-entities)
        CCCRegister.registerBlockEntity("source_block", SourceBlockEntity::new, new SourceBlock());
        CCCRegister.registerBlockEntity("target_block", TargetBlockEntity::new, new TargetBlock());

        CCCRegister.registerBlockEntity("redrouter_block", RedRouterBlockEntity::new, new RedRouterBlock());
        CCCRegister.registerBlockEntity("scroller_block", ScrollerBlockEntity::new, new ScrollerBlock());
        CCCRegister.registerBlockEntity("animatronic_block", AnimatronicBlockEntity::new, new AnimatronicBlock());

        // Entities
        CCCRegister.registerEntity("animatronic", FabricEntityTypeBuilder.create(MobCategory.MISC, AnimatronicEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.8F)).build());

        // Create Display Stuff
        AllDisplayBehaviours.assignBlockEntity(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "source_block_display_source"), new SourceBlockDisplaySource()), CCCRegister.getBlockEntityType("source_block"));
        AllDisplayBehaviours.assignBlockEntity(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "target_block_display_target"), new TargetBlockDisplayTarget()), CCCRegister.getBlockEntityType("target_block"));

        // Misc
        Registry.register(Registry.MOTIVE, new ResourceLocation(MOD_ID, "funny_redrouters"), new Motive(32,16));
        CCCSoundEvents.init();
        ComputerCraftAPI.registerPeripheralProvider(new PeripheralProvider());
    }
}
