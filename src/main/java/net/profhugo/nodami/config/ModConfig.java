package net.profhugo.nodami.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.client.gui.screens.Screen;
import net.profhugo.nodami.ReNDI;

import java.util.Arrays;
import java.util.List;

@Config(name = ReNDI.MODID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Category("core")
    @ConfigEntry.Gui.Tooltip
    public int iFrameInterval = 0;

    @ConfigEntry.Category("core")
    @ConfigEntry.Gui.Tooltip
    public boolean excludePlayers = false;
    @ConfigEntry.Category("core")
    @ConfigEntry.Gui.Tooltip
    public boolean excludeAllMobs = false;
    @ConfigEntry.Category("thresholds")
    @ConfigEntry.Gui.Tooltip
    public double attackCancelThreshold = 0.1;
    @ConfigEntry.Category("thresholds")
    @ConfigEntry.Gui.Tooltip
    public double knockbackCancelThreshold = 0.75;
    @ConfigEntry.Category("exclusions")
    @ConfigEntry.Gui.Tooltip
    public List<String> attackExcludedEntities = Arrays.asList("minecraft:slime", "minecraft:magma_cube", "tconstruct:earth_slime", "tconstruct:sky_slime", "tconstruct:ender_slime", "tconstruct:terracube", "twilightforest:maze_slime");
    @ConfigEntry.Category("exclusions")
    @ConfigEntry.Gui.Tooltip
    public List<String> dmgReceiveExcludedEntities = List.of();
    @ConfigEntry.Category("exclusions")
    @ConfigEntry.Gui.Tooltip
    public List<String> damageSrcWhitelist = Arrays.asList("inFire", "lava", "sweetBerryBush", "cactus", "lightningBolt", "inWall", "hotFloor", "outOfWorld");

    public static Screen getConfigScreen(Screen parent){
        return AutoConfig.getConfigScreen(ModConfig.class, parent).get();
    }
}
