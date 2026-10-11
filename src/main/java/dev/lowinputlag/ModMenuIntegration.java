package dev.lowinputlag;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        // Cloth Config is optional: if it is missing, the button simply does nothing.
        if (!FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return parent -> parent;
        }
        return LowInputLagConfigScreen::create;
    }
}
