package cc.tweaked_programs.cccbridge.client;

import cc.tweaked_programs.cccbridge.client.minecraft.screen.ConfigScreen;
import cc.tweaked_programs.cccbridge.common.modloader.CCCBridge;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Properties;

@OnlyIn(Dist.CLIENT)
public class CCConfig {
    public static final CCConfig CONFIG = new CCConfig(
            FMLPaths.CONFIGDIR.get().toString()
    );

    private final Properties properties;
    public String config_dir;

    public boolean FLICKERING;

    public CCConfig(String config_dir) {
        this.config_dir = config_dir + "/" + CCCBridge.MOD_ID + ".properties";
        this.properties = new Properties();

        // Defaults
        properties.putIfAbsent("flickering", Boolean.toString(true));

        load();
    }

    public void load() {
        try (Reader reader = new FileReader(config_dir)) {
            properties.load(reader);
        } catch (FileNotFoundException e) {
            CCCBridge.LOGGER.warn("No config found at '{}'. Creating new one with default values.", config_dir);
            save();
        } catch (IOException e) {
            CCCBridge.LOGGER.error("Error while loading config at '{}'. Reason: '{}'", config_dir, e);
        }

        FLICKERING = (properties.get("flickering").toString().equals("true"));
    }

    public void save() {
        properties.put("flickering", Boolean.toString(FLICKERING));

        try (FileWriter writer = new FileWriter(config_dir)){
            properties.store(writer, "Client side configuration for CC:C Bridge");
        } catch(ClassCastException e) {
            CCCBridge.LOGGER.error("Error while casting value for key at config '{}'. Reason: '{}'", config_dir, e);
        } catch (IOException e) {
            CCCBridge.LOGGER.error("Error while saving config at '{}'. Reason: '{}'", config_dir, e);
        }
    }

    public ConfigScreen newScreen(@Nullable Screen parent) {
        return new ConfigScreen(this, parent);
    }
}