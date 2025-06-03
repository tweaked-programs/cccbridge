package cc.tweaked_programs.cccbridge.client.minecraft.screen;

import cc.tweaked_programs.cccbridge.client.CCConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {
    private final CCConfig config;
    private final Screen parent;

    OptionsList options;
    Button closeBtn;

    public ConfigScreen(CCConfig config, @Nullable Screen parent) {
        super(Component.translatable("cccbridge.screen.config_screen"));
        this.config = config;
        this.parent = parent;
    }

    @Override
    protected void init() {
        assert this.minecraft != null;
        options = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
        options.setRenderBackground(false);
        options.setRenderTopAndBottom(false);

        options.addBig(new OptionInstance<>(
                "cccbridge.options.option.flickering",
                (option) -> Tooltip.create(Component.translatable("cccbridge.options.tooltip.flickering")),
                (component, toggle) -> (toggle ? Component.translatable("options.on") : Component.translatable("options.off")),
                OptionInstance.BOOLEAN_VALUES,
                config.flickering.value(),
                (value) -> config.flickering.setValue(value)
        ));

        closeBtn = Button.builder(Component.translatable("cccbridge.options.button.close"), (button) -> onClose())
                .bounds(width-80-((32-20)/2), height-(32/2)-(20/2), 80, 20)
                .build();

        addWidget(options);
        addRenderableWidget(closeBtn);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);

        options.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 32/2-4, 16777215);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        config.save();

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
}