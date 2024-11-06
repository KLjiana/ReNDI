package net.profhugo.nodami;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ReNDI.MODID)
public class ReNDI
{
	public static final String MODID = "rendi";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static IEventBus bus;

	public ReNDI(IEventBus modEventBus, ModContainer modContainer)
	{
		bus = modEventBus;
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
		NeoForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event)
	{
		LOGGER.info("ndiupdated: Serverside operations started.");
		Config.cacheValues();
		bus.register(new Config());
	}
}
