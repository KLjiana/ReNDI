package net.profhugo.nodami.config.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.profhugo.nodami.config.ModConfig;

public class ModMenuIntegration implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModConfig::getConfigScreen;
    }
}
