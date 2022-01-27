package com.steelworks.Network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class ParticleMessage {

	public ResourceLocation particle;
	public double x;
	public double y;
	public double z;
	public double mx;
	public double my;
	public double mz;
	public int a;
	public boolean initialized = true;

	/**
	 * Custom particle packet since server particle packets don't carry over correct motion values in the way we use them.
	 */
	public ParticleMessage(ResourceLocation id, double x, double y, double z, double mx, double my, double mz, int a) {
		this.particle = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.mx = mx;
		this.my = my;
		this.mz = mz;
		this.a = a;
		initialized = true;
	}

	public ParticleMessage() {
		initialized = false;
	}

	public static ParticleMessage decode(PacketBuffer buf) {
		ParticleMessage retval = new ParticleMessage();
		try {
			retval.particle = buf.readResourceLocation();
			retval.x = buf.readDouble();
			retval.y = buf.readDouble();
			retval.z = buf.readDouble();
			retval.mx = buf.readDouble();
			retval.my = buf.readDouble();
			retval.mz = buf.readDouble();
			retval.a = buf.readInt();
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
		buf.writeResourceLocation(particle);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeDouble(mx);
		buf.writeDouble(my);
		buf.writeDouble(mz);
		buf.writeInt(a);
	}

}
