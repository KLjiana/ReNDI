package net.profhugo.nodami;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.profhugo.nodami.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReNDI implements ModInitializer
{
	public static final String MODID = "rendi";
	public static ModConfig Config;
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@Override
	public void onInitialize() {
		Config = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new).getConfig();
	}
}
