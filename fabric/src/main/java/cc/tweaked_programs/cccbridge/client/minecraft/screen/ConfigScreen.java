package cc.tweaked_programs.cccbridge.client.minecraft.screen;

import cc.tweaked_programs.cccbridge.client.CCConfig;
import com.simibubi.create.Create;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {
    public static final ResourceLocation BETTER_BACKGROUND_LOCATION = new ResourceLocation(Create.ID, "textures/block/palettes/stone_types/natural/asurine_3.png");
    public static final ResourceLocation BETTER_BACKGROUND_FOR_TOP_AND_BOTTOM_LOCATION = new ResourceLocation(Create.ID, "textures/block/palettes/stone_types/natural/asurine_0.png");

    private final CCConfig config;
    private final Screen parent;

    OptionsList options;
    Button save;
    Button cancel;

    public ConfigScreen(CCConfig config, @Nullable Screen parent) {
        super(Component.translatable("cccbridge.screen.config_screen"));
        this.config = config;
        this.parent = parent;
    }

    @Override
    protected void init() {
        assert this.minecraft != null;
        options = new OptionsListICreatedJustToHaveMyOwnBackground(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
        options.setRenderBackground(false);
        options.setRenderTopAndBottom(false);

        options.addBig(new OptionInstance<>(
                "cccbridge.options.option.flickering",
                (option) -> Tooltip.create(Component.translatable("cccbridge.options.tooltip.flickering")),
                (component, toggle) -> (toggle ? Component.translatable("options.on") : Component.translatable("options.off")),
                OptionInstance.BOOLEAN_VALUES,
                config.FLICKERING,
                (value) -> {
                    config.FLICKERING = value;
                    setChanged(true);
                }
        ));

        cancel = Button.builder(Component.translatable("cccbridge.options.button.close"), (button) -> {
                    onClose();
                })
                .bounds(width-80-60-((32-20)), height-(32/2)-(20/2), 60, 20)
                .build();
        save = Button.builder(Component.translatable("cccbridge.options.button.saved"), (button) -> {
                    setChanged(false);
                    config.save();
                })
                .bounds(width-80-((32-20)/2), height-(32/2)-(20/2), 80, 20)
                .build();
        save.active = false;

        addWidget(options);
        addRenderableWidget(cancel);
        addRenderableWidget(save);
    }

    public void setChanged(boolean value) {
        save.setMessage( value ?
                  Component.translatable("cccbridge.options.button.save")
                : Component.translatable("cccbridge.options.button.saved")
        );
        save.active = value;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);

        options.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 32/2-4, 16777215);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderDirtBackground(GuiGraphics guiGraphics) {
        guiGraphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
        guiGraphics.blit(BETTER_BACKGROUND_LOCATION, 0, 0, 0, 0.0F, 0.0F, this.width, this.height, 32, 32);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void onClose() {
        config.load();

        if (parent != null) {
            if (this.minecraft != null)
                this.minecraft.setScreen(parent);
        } else
            super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    static class OptionsListICreatedJustToHaveMyOwnBackground extends OptionsList {
        public OptionsListICreatedJustToHaveMyOwnBackground(Minecraft minecraft, int i, int j, int k, int l, int m) {
            super(minecraft, i, j, k, l, m);
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            super.render(guiGraphics, mouseX, mouseY, partialTick);

            // Render Top and Bottom
            guiGraphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
            guiGraphics.blit(BETTER_BACKGROUND_FOR_TOP_AND_BOTTOM_LOCATION, this.x0, 0, 0.0F, 0.0F, this.width, this.y0, 32, 32);
            guiGraphics.blit(BETTER_BACKGROUND_FOR_TOP_AND_BOTTOM_LOCATION, this.x0, this.y1, 0.0F, (float)this.y1, this.width, this.height - this.y1, 32, 32);
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

            guiGraphics.fillGradient(RenderType.guiOverlay(), this.x0, this.y0, this.x1, this.y0 + 4, -16777216, 0, 0);
            guiGraphics.fillGradient(RenderType.guiOverlay(), this.x0, this.y1 - 4, this.x1, this.y1, 0, -16777216, 0);

        }
    }
}