package com.steelworks.Block;

import com.steelworks.Steelworks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class ForgeFurnaceBlock extends Block {

	public static final BooleanProperty IS_OPEN = BooleanProperty.create("is_open");
	public static final BooleanProperty IS_ON = BooleanProperty.create("is_on");

	public ForgeFurnaceBlock() {
		super(AbstractBlock.Properties.of(Material.METAL).strength(8.0f).harvestTool(ToolType.PICKAXE));
		this.registerDefaultState(this.stateDefinition.any().setValue(IS_OPEN, Boolean.FALSE).setValue(IS_ON, Boolean.FALSE));
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		if (!world.isClientSide()) {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof ForgeFurnaceTileEntity) {
				INamedContainerProvider containerProvider = new INamedContainerProvider() {
					@Override
					public ITextComponent getDisplayName() {
						return new TranslationTextComponent("block." + Steelworks.MODID + ".forge_furnace");
					}

					@Override
					public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
						return new ForgeFurnaceContainer(id, world, pos, playerInventory, playerEntity, ((ForgeFurnaceTileEntity) tileEntity).tileData);
					}
				};
				NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getBlockPos());
			}

		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ForgeFurnaceTileEntity();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(IS_OPEN, false).setValue(IS_ON, false);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(IS_OPEN);
		builder.add(IS_ON);
	}
}
