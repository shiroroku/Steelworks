package com.steelworks.Registry;

import com.steelworks.Effect.RageEffect;
import com.steelworks.Steelworks;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRegistry {

	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Steelworks.MODID);

	public static final RegistryObject<Effect> RAGE = EFFECTS.register("rage", () -> new RageEffect().addAttributeModifier(Attributes.ATTACK_DAMAGE, "22653B89-116E-49DC-9B6B-9971489B5BE5", 0.0D, AttributeModifier.Operation.ADDITION));

	public static void init() {
		EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
