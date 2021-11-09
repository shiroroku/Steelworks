package com.steelworks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

	public static ForgeConfigSpec COMMON_CONFIG;

	static {
		ForgeConfigSpec.Builder commonBuilder = new ForgeConfigSpec.Builder();

		commonBuilder.comment("Crucible Config").push("Crucible");
		/*HEAT_MULTIPLIERS = commonBuilder.comment("Block registry names that counts as a heat source for the crucible and their speed multipliers. Must follow the specific format: (registryname=multiplier)").define("heat_multipliers", new ArrayList<String>() {{
			add("minecraft:fire=1.0");
			add("minecraft:campfire=1.0");
			add("minecraft:magma_block=2.0");
			add("minecraft:lava=3.0");
			add("minecraft:soul_fire=4.0");
			add("minecraft:soul_campfire=4.0");
		}});*/
		commonBuilder.pop();

		COMMON_CONFIG = commonBuilder.build();
	}
}
