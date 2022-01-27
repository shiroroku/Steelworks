package com.steelworks.Data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.steelworks.Steelworks;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class DataConfigJsonReloader extends JsonReloadListener {

	private static Map<ResourceLocation, Float> heatMultipliers = new HashMap<>();
	private static Map<ResourceLocation, Integer> bleedInflictors = new HashMap<>();

	public DataConfigJsonReloader() {
		super((new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create(), "config");
	}

	@Override
	public void apply(Map<ResourceLocation, JsonElement> map, IResourceManager manager, IProfiler profiler) {
		heatMultipliers.clear();
		bleedInflictors.clear();
		loadHeatMultipliers(map);
		loadBleedInflictions(map);
	}

	private static void loadBleedInflictions(Map<ResourceLocation, JsonElement> map) {
		map.forEach((file, jsonElement) -> {
			try {
				if (file.equals(new ResourceLocation(Steelworks.MODID, "inflicts_bleeding"))) {
					jsonElement.getAsJsonObject().getAsJsonObject("items").entrySet().forEach((entry) -> bleedInflictors.put(ResourceLocation.tryParse(entry.getKey()), entry.getValue().getAsInt()));
				}
			} catch (Exception e) {
				Steelworks.LOGGER.error("Error when loading config/inflicts_bleeding from data!: " + e.getMessage());
			}
		});
	}

	private static void loadHeatMultipliers(Map<ResourceLocation, JsonElement> map) {
		map.forEach((file, jsonElement) -> {
			try {
				if (file.equals(new ResourceLocation(Steelworks.MODID, "forge_furnace_heat_multipliers"))) {
					jsonElement.getAsJsonObject().getAsJsonObject("multipliers").entrySet().forEach((entry) -> heatMultipliers.put(ResourceLocation.tryParse(entry.getKey()), entry.getValue().getAsFloat()));
				}
			} catch (Exception e) {
				Steelworks.LOGGER.error("Error when loading config/forge_furnace_heat_multipliers from data!: " + e.getMessage());
			}
		});
	}

	public static Float getHeatMultiplierFromBlock(ResourceLocation block) {
		return heatMultipliers.getOrDefault(block, 0.0f);
	}
	public static int getBleedAmountFromItem(ResourceLocation item) {
		return bleedInflictors.getOrDefault(item, 0);
	}

}
