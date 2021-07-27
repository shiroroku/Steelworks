package com.steelworks.Registry;

import com.steelworks.Block.ForgeFurnaceContainer;
import com.steelworks.Steelworks;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry {

	private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Steelworks.MODID);

	public static final RegistryObject<ContainerType<ForgeFurnaceContainer>> FORGE_FURNACE = CONTAINERS.register("forge_furnace", () -> IForgeContainerType.create((id, inv, data) -> {
		BlockPos pos = data.readBlockPos();
		World world = inv.player.level;
		return new ForgeFurnaceContainer(id, world, pos, inv, inv.player);
	}));

	public static void init() {
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
