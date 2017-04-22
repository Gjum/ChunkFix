package gjum.minecraft.forge.chunkfix.config;


import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.Set;

public class ChunkFixConfig {
    public static final String CATEGORY_MAIN = "Main";

    public static final gjum.minecraft.forge.chunkfix.config.ChunkFixConfig instance = new gjum.minecraft.forge.chunkfix.config.ChunkFixConfig();

    public Configuration config;

    public boolean enabled;

    private Property propEnabled;

    private ChunkFixConfig() {
    }

    public void load(File configFile) {
        config = new Configuration(configFile, gjum.minecraft.forge.chunkfix.ChunkFixMod.VERSION);

        syncProperties();
        final ConfigCategory categoryMain = config.getCategory(CATEGORY_MAIN);
        final Set<String> confKeys = categoryMain.keySet();

        config.load();

        if (!config.getDefinedConfigVersion().equals(config.getLoadedConfigVersion())) {
            // clear config from old entries
            // otherwise they would clutter the gui
            final Set<String> unusedConfKeys = categoryMain.keySet();
            unusedConfKeys.removeAll(confKeys);
            for (String confKey : unusedConfKeys) {
                categoryMain.remove(confKey);
            }
        }

        syncProperties();
        syncValues();
    }

    public void afterGuiSave() {
        syncProperties();
        syncValues();
    }

    public void setEnabled(boolean enabled) {
        syncProperties();
        propEnabled.set(enabled);
        syncValues();
    }

    /**
     * no idea why this has to be called so often, ideally the prop* would stay the same,
     * but it looks like they get disassociated from the config sometimes and setting them no longer has any effect
     */
    private void syncProperties() {
        propEnabled = config.get(CATEGORY_MAIN, "enabled", true, "Enable/disable all overlays");
    }

    /**
     * called every time a prop is changed, to apply the new values to the fields and to save the values to the config file
     */
    private void syncValues() {
        enabled = propEnabled.getBoolean();

        if (config.hasChanged()) {
            config.save();
            syncProperties();
            gjum.minecraft.forge.chunkfix.ChunkFixMod.logger.info("Saved " + gjum.minecraft.forge.chunkfix.ChunkFixMod.MOD_NAME + " config.");
        }
    }

}
