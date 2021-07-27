package com.steelworks.Block;

import com.steelworks.Registry.BlockRegistry;
import com.steelworks.Registry.ContainerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ForgeFurnaceContainer extends Container {

	private final ForgeFurnaceTileEntity tileEntity;
	private final IIntArray data;

	public ForgeFurnaceContainer(int ID, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerIn) {
		this(ID, world, pos, playerInventory, playerIn, new IntArray(2));
	}

	public ForgeFurnaceContainer(int ID, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerIn, IIntArray tileData) {
		super(ContainerRegistry.FORGE_FURNACE.get(), ID);
		checkContainerDataCount(tileData, 2);
		IItemHandler playerInv = new InvWrapper(playerInventory);
		tileEntity = (ForgeFurnaceTileEntity) world.getBlockEntity(pos);
		data = tileData;

		if (tileEntity != null) {
			tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
				addSlot(new SlotItemHandler(handler, tileEntity.SLOT_INPUT_1, 56, 17));
				addSlot(new SlotItemHandler(handler, tileEntity.SLOT_INPUT_2, 80, 17));
				addSlot(new SlotItemHandler(handler, tileEntity.SLOT_INPUT_3, 104, 17));
				addSlot(new SlotItemHandler(handler, tileEntity.SLOT_OUTPUT_1, 80, 54) {
					@Override
					public boolean mayPlace(ItemStack stack) {
						return false;
					}
				});
			});
		}
		this.addDataSlots(tileData);

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlot(new SlotItemHandler(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int k = 0; k < 9; ++k) {
			addSlot(new SlotItemHandler(playerInv, k, 8 + k * 18, 142));
		}
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		return stillValid(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()), player, BlockRegistry.FORGE_FURNACE.get());
	}

	public ForgeFurnaceTileEntity getTile() {
		return this.tileEntity;
	}

	public int getCookTime() {
		return this.data.get(0);
	}

	public int getCookLength() {
		return this.data.get(1);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int slotid) {
		ItemStack newitem = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotid);
		if (slot != null && slot.hasItem()) {
			ItemStack iteminslot = slot.getItem();
			newitem = iteminslot.copy();

			int slotcount = 4;

			if (slotid < slotcount) {
				if (!this.moveItemStackTo(iteminslot, slotcount, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!this.moveItemStackTo(iteminslot, 0, slotcount, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (iteminslot.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return newitem;
	}
}
