package com.steelworks.Block;

import com.steelworks.Data.DataConfigJsonReloader;
import com.steelworks.Recipe.ForgeFurnaceRecipe;
import com.steelworks.Registry.RecipeRegistry;
import com.steelworks.Registry.TileEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ForgeFurnaceTileEntity extends TileEntity implements ITickableTileEntity {

	public final int SLOT_INPUT_1 = 0;
	public final int SLOT_INPUT_2 = 1;
	public final int SLOT_INPUT_3 = 2;
	public final int SLOT_OUTPUT_1 = 3;
	private int cookTime = -1;
	private int cookLength = -1;
	private float xp = 0f;

	public ForgeFurnaceTileEntity() {
		super(TileEntityRegistry.FORGE_FURNACE.get());
	}

	protected final IIntArray tileData = new IIntArray() {
		public int get(int id) {
			if (id == 0) {
				return ForgeFurnaceTileEntity.this.cookTime;
			} else {
				return ForgeFurnaceTileEntity.this.cookLength;
			}
		}

		public void set(int id, int value) {
			if (id == 0) {
				ForgeFurnaceTileEntity.this.cookTime = value;
			} else {
				ForgeFurnaceTileEntity.this.cookLength = value;
			}
		}

		public int getCount() {
			return 2;
		}
	};

	@Override
	public void tick() {
		this.setPlayersInside(this.getPlayersInside());
		if (!this.level.isClientSide()) {
			ForgeFurnaceRecipe recipe = canCraft();
			if (recipe != null) {
				if (getCookTime() > 0) {
					setCookTime(getCookTime() - 1);
				} else {
					if (getCookTime() == 0) {
						FinishCraft(recipe);
					}
					if (getCookTime() == -1 && getCookLength() == -1) {
						startCraft(recipe);
					}
				}
			} else {
				stopCrafting();
			}
		}

	}

	/**
	 * Returns how fast the crucible should smelt from heat source.
	 */
	public float getSpeedMultiplier() {
		if (this.level.isEmptyBlock(this.getBlockPos().below())) {
			return 0.0f;
		}
		Block heatSource = this.getLevel().getBlockState(this.getBlockPos().below()).getBlock();
		return DataConfigJsonReloader.getHeatMultiplierFromBlock(heatSource.getRegistryName());
	}

	/**
	 * Returns a recipe that is ready to be crafted, returns null if nothing can be crafted.
	 */
	public ForgeFurnaceRecipe canCraft() {
		if (getSpeedMultiplier() == 0f) {
			return null;
		}
		ForgeFurnaceRecipe recipe = getRecipeFromContents();
		if (recipe != null) {
			if (itemhandler.getStackInSlot(SLOT_OUTPUT_1).isEmpty()) {
				return recipe;
			}

			ItemStack output = itemhandler.getStackInSlot(SLOT_OUTPUT_1);
			if (recipe.getResultItem().getItem() == output.getItem() && output.isStackable() && output.getCount() + recipe.getResultItem().getCount() <= output.getMaxStackSize()) {
				return recipe;
			}
		}
		return null;
	}

	/**
	 * Returns the recipe that can be crafted from this tile's input slots.
	 */
	public ForgeFurnaceRecipe getRecipeFromContents() {
		ForgeFurnaceRecipe tocraft = null;
		for (final ForgeFurnaceRecipe recipe : level.getRecipeManager().getAllRecipesFor(ForgeFurnaceRecipe.TYPE)) {
			if (recipe.matches(itemhandler)) {
				tocraft = recipe;
				break;
			}

		}
		return tocraft;
	}

	/**
	 * Called when the craft progress starts. Updates blockstate and sets cook length from recipe.
	 */
	public void startCraft(ForgeFurnaceRecipe recipe) {
		float speedMulti = getSpeedMultiplier();
		if (speedMulti == 0f) {
			stopCrafting();
		}
		this.getLevel().setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(ForgeFurnaceBlock.IS_ON, true));
		setCookLength((int) (recipe.getCraftTime() / speedMulti));
		setCookTime(getCookLength());
	}

	/**
	 * Called when the craft needs to be halted. Sets values to default.
	 */
	public void stopCrafting() {
		this.getLevel().setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(ForgeFurnaceBlock.IS_ON, false));
		setCookLength(-1);
		setCookTime(-1);
	}

	/**
	 * Called when the craft progress is complete. Shrinks inputs and places output item.
	 */
	private void FinishCraft(ForgeFurnaceRecipe recipe) {
		stopCrafting();

		Map<ItemStack, Boolean> inventoryitems = new HashMap<>();
		for (int slot = 0; slot < 3; slot++) {
			inventoryitems.put(itemhandler.getStackInSlot(slot), false);
		}
		for (Ingredient ingredient : recipe.getIngredients()) {
			for (int slot = 0; slot < 3; slot++) {
				if (ingredient.test(itemhandler.getStackInSlot(slot))) {
					if (!inventoryitems.get(itemhandler.getStackInSlot(slot))) {
						inventoryitems.remove(itemhandler.getStackInSlot(slot));
						inventoryitems.put(itemhandler.getStackInSlot(slot), true);
						itemhandler.getStackInSlot(slot).shrink(ingredient.getItems()[0].getCount());
					}
				}
			}
		}

		ItemStack output = itemhandler.getStackInSlot(SLOT_OUTPUT_1);
		if (output.isEmpty()) {
			itemhandler.setStackInSlot(SLOT_OUTPUT_1, recipe.getOutput());
		}
		if (output.isStackable()) {
			ItemStack newoutput = recipe.getOutput();
			newoutput.grow(output.getCount());
			itemhandler.setStackInSlot(SLOT_OUTPUT_1, newoutput);
		}

		this.setXP(this.getXP() + recipe.getXP());
	}

	/**
	 * Returns the tile's held experience.
	 */
	public float getXP() {
		return this.xp;
	}

	public void setXP(float xp) {
		this.xp = xp;
		this.setChanged();
	}

	/**
	 * Returns the time in ticks of the current crafting progess.
	 */
	public int getCookTime() {
		return this.cookTime;
	}

	public void setCookTime(int tick) {
		this.cookTime = tick;
		this.setChanged();
	}

	/**
	 * Returns the time in ticks needed to craft the current recipe.
	 */
	public int getCookLength() {
		return this.cookLength;
	}

	public void setCookLength(int ticks) {
		this.cookLength = ticks;
		this.setChanged();
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		itemhandler.deserializeNBT(nbt.getCompound("inventory"));
		if (nbt.contains("CookTime", Constants.NBT.TAG_INT)) {
			this.cookTime = nbt.getInt("CookTime");
		}
		if (nbt.contains("CookLength", Constants.NBT.TAG_INT)) {
			this.cookLength = nbt.getInt("CookLength");
		}
		if (nbt.contains("XP", Constants.NBT.TAG_FLOAT)) {
			this.xp = nbt.getFloat("XP");
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		nbt.put("inventory", itemhandler.serializeNBT());
		if (this.getCookTime() != -1) {
			nbt.putInt("CookTime", this.getCookTime());
		}
		if (this.getCookLength() != -1) {
			nbt.putInt("CookLength", this.getCookTime());
		}
		if (this.getXP() != 0) {
			nbt.putFloat("XP", this.getXP());
		}
		return nbt;
	}

	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getBlockPos(), -1, save(new CompoundNBT()));
	}

	@Override
	public void onDataPacket(NetworkManager manager, SUpdateTileEntityPacket packet) {
		load(this.getBlockState(), packet.getTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = super.getUpdateTag();
		this.save(nbt);
		return nbt;
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		items.invalidate();
	}

	private int playersInside;

	/**
	 * Sets how many players have this tile's gui open. Handles swapping blockstate and sound effects.
	 */
	public void setPlayersInside(int amt) {
		if (amt == playersInside) {
			return;
		}

		if (playersInside == 0 && amt > 0) {
			if (!level.isClientSide) {
				this.getLevel().setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(ForgeFurnaceBlock.IS_OPEN, true));
			}
			this.getLevel().playLocalSound(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.GRINDSTONE_USE, SoundCategory.BLOCKS, 0.25f, 0.5f, false);
		}

		this.playersInside = amt;

		if (playersInside == 0) {
			if (!level.isClientSide) {
				this.getLevel().setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(ForgeFurnaceBlock.IS_OPEN, false));
			}
			this.getLevel().playLocalSound(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.GRINDSTONE_USE, SoundCategory.BLOCKS, 0.25f, 0.5f, false);
		}

	}

	/**
	 * Returns how many players have this tile's gui open.
	 */
	public int getPlayersInside() {
		int amt = 0;
		float x = this.getBlockPos().getX();
		float y = this.getBlockPos().getY();
		float z = this.getBlockPos().getZ();
		float distance = 5.0F;
		for (PlayerEntity playerentity : level.getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(x - distance, y - distance, z - distance, x + 1 + distance, y + 1 + distance, z + 1 + distance))) {
			if (playerentity.containerMenu instanceof ForgeFurnaceContainer) {
				if (((ForgeFurnaceContainer) playerentity.containerMenu).getTile().getBlockPos() == this.getBlockPos()) {
					amt++;
				}
			}
		}
		return amt;
	}

	private final ItemStackHandler itemhandler = new ItemStackHandler(4) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return slot < SLOT_OUTPUT_1;
		}

		@Override
		@Nonnull
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (slot == SLOT_OUTPUT_1 && !simulate && (int) xp > 0) {
				getLevel().addFreshEntity(new ExperienceOrbEntity(getLevel(), getBlockPos().getX() + 0.5f, getBlockPos().getY() + 1, getBlockPos().getZ() + 0.5f, (int) getXP()));
				setXP(getXP() - (int) getXP());
			}
			return super.extractItem(slot, amount, simulate);
		}
	};
	private final LazyOptional<IItemHandler> items = LazyOptional.of(() -> itemhandler);

	private final IItemHandler hopperhandler = new IItemHandler() {
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return itemhandler.isItemValid(slot, stack);
		}

		@Override
		public int getSlots() {
			return itemhandler.getSlots();
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			return itemhandler.getStackInSlot(slot);
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			return itemhandler.insertItem(slot, stack, simulate);
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (slot < SLOT_OUTPUT_1) {
				return ItemStack.EMPTY;
			}
			return itemhandler.extractItem(slot, amount, simulate);
		}

		@Override
		public int getSlotLimit(int slot) {
			return itemhandler.getSlotLimit(slot);
		}
	};
	private final LazyOptional<IItemHandler> hopper = LazyOptional.of(() -> hopperhandler);

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == Direction.DOWN) {
				return hopper.cast();
			}
			return items.cast();
		}
		return super.getCapability(cap, side);
	}

}
