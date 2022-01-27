package com.steelworks;

import com.steelworks.Registry.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Steelworks.MODID)
public class Steelworks {

	/*
		BUGS:

		- Scythes harvesting do not notify other players in multiplayer
		- Gauntlet offhand armor does not apply to the player
		-

	 */

	public static final String MODID = "steelworks";
	public static final Logger LOGGER = LogManager.getLogger();

	public Steelworks() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);

		ItemRegistry.init();
		BlockRegistry.init();
		TileEntityRegistry.init();
		ContainerRegistry.init();
		RecipeRegistry.init();
		EffectRegistry.init();
		EntityRegistry.init();
		Events.init();
		ParticleRegistry.init();

		FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonSetup::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
	}

	public static final ItemGroup CREATIVETAB = new ItemGroup(MODID) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ItemRegistry.STEEL_INGOT.get());
		}
	};
}
