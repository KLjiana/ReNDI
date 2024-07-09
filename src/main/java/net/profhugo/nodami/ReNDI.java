package net.profhugo.nodami;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ReNDI.MODID)
public class ReNDI
{
	public static final String MODID = "rendi";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static IEventBus bus;

	public ReNDI()
	{
		bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event)
	{
		LOGGER.info("ndiupdated: Serverside operations started.");
		Config.cacheValues();
		bus.register(new Config());
	}
}
