package com.steelworks.Network;

import net.minecraft.network.PacketBuffer;

public class BleedUpdateMessage {

	public int stacks;
	public boolean initialized = true;

	/**
	 * why do i need a whole packet to sync a single number????
	 */
	public BleedUpdateMessage(int stacks) {
		this.stacks = stacks;
		initialized = true;
	}

	public BleedUpdateMessage() {
		initialized = false;
	}

	public static BleedUpdateMessage decode(PacketBuffer buf) {
		BleedUpdateMessage retval = new BleedUpdateMessage();
		try {
			retval.stacks = buf.readInt();
		} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
			return retval;
		}
		retval.initialized = true;
		return retval;
	}

	public void encode(PacketBuffer buf) {
		if (!initialized) {
			return;
		}
		buf.writeInt(stacks);
	}

}
