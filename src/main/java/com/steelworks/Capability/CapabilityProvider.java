package com.steelworks.Capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProvider implements ICapabilitySerializable<INBT> {

	private final Direction NO_SPECIFIC_SIDE = null;

	private Bleed bleed = new Bleed();

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		if (BleedCapability.CAPABILITY == capability) {
			return (LazyOptional<T>) LazyOptional.of(() -> bleed);
		}

		return LazyOptional.empty();
	}

	@Override
	public INBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.put("bleed", BleedCapability.CAPABILITY.writeNBT(bleed, NO_SPECIFIC_SIDE));
		return nbt;
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		CompoundNBT compoundNBT = (CompoundNBT) nbt;
		BleedCapability.CAPABILITY.readNBT(bleed, NO_SPECIFIC_SIDE, compoundNBT.get("bleed"));
	}

}
