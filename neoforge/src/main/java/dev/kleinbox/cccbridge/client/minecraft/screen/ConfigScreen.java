package dev.kleinbox.cccbridge.client.minecraft.screen;

import dev.kleinbox.cccbridge.client.CCConfig;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends OptionsSubScreen {
    private final CCConfig config;

    public ConfigScreen(CCConfig config, @Nullable Screen parent) {
        super(parent, null, Component.translatable("cccbridge.screen.config_screen"));
        this.config = config;
    }

    @Override
    protected void addContents() {
        this.list = this.layout.addToContents(new OptionsList(this.minecraft, this.width, this));
        this.addOptions();
    }

    @Override
    protected void addOptions() {
        list.addBig(new OptionInstance<>(
                "cccbridge.options.option.flickering",
                (option) -> Tooltip.create(Component.translatable("cccbridge.options.tooltip.flickering")),
                (component, toggle) -> (toggle ? Component.translatable("options.on") : Component.translatable("options.off")),
                OptionInstance.BOOLEAN_VALUES,
                config.flickering.value(),
                config.flickering::setValue
        ));
    }

    /*@Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        list.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 32/2-4, 16777215);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }*/

    @Override
    public void onClose() {
        config.save();

        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}