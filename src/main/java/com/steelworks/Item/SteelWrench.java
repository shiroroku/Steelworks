package com.steelworks.Item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.steelworks.Registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.TieredItem;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawHighlightEvent;

import java.awt.*;

public class SteelWrench extends TieredItem {
	public SteelWrench(IItemTier tier, Properties prop) {
		super(tier, prop);
	}

	@OnlyIn(Dist.CLIENT)
	public static void handleOnBlockOutline(DrawHighlightEvent.HighlightBlock e) {

		PlayerEntity player = (PlayerEntity) e.getInfo().getEntity();
		if (player.getMainHandItem().getItem() == ItemRegistry.STEEL_WRENCH.get()) {

			BlockState clickedBlock = player.level.getBlockState(e.getTarget().getBlockPos());
			if (!clickedBlock.isCollisionShapeFullBlock(e.getInfo().getEntity().level, e.getTarget().getBlockPos())) {
				return;
			}
			if (clickedBlock.getProperties().stream().noneMatch(p -> p instanceof DirectionProperty)) {
				return;
			}

			MatrixStack stack = e.getMatrix();
			IRenderTypeBuffer buf = e.getBuffers();
			IVertexBuilder builder = buf.getBuffer(RenderType.LINES);

			float x = (float) e.getTarget().getBlockPos().getX();
			float y = (float) e.getTarget().getBlockPos().getY();
			float z = (float) e.getTarget().getBlockPos().getZ();

			Color color = new Color(0, 0, 0, 255);
			float r = color.getRed() / 255f;
			float g = color.getGreen() / 255f;
			float b = color.getBlue() / 255f;
			float a = color.getAlpha() / 255f;

			stack.pushPose();

			Vector3d projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
			stack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

			Matrix4f matrix = stack.last().pose();
			builder.vertex(matrix, x, y, z).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x + 1, y + 1, z).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x + 1, y, z).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x, y + 1, z).color(r, g, b, a).endVertex();

			builder.vertex(matrix, x, y, z + 1).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x, y + 1, z).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x, y + 1, z + 1).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x, y, z).color(r, g, b, a).endVertex();

			builder.vertex(matrix, x + 1, y, z + 1).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x + 1, y + 1, z).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x + 1, y + 1, z + 1).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x + 1, y, z).color(r, g, b, a).endVertex();

			builder.vertex(matrix, x, y, z + 1).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x + 1, y + 1, z + 1).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x + 1, y, z + 1).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x, y + 1, z + 1).color(r, g, b, a).endVertex();
			stack.popPose();
		}
	}

	public ActionResultType useOn(ItemUseContext ctx) {
		if (!ctx.getLevel().isClientSide()) {
			BlockState clickedBlock = ctx.getLevel().getBlockState(ctx.getClickedPos());

			if(clickedBlock.getBlock() instanceof ChestBlock){
				return ActionResultType.PASS;
			}

			if (clickedBlock.getProperties().stream().noneMatch(p -> p instanceof DirectionProperty)) {
				return ActionResultType.PASS;
			}

			DirectionProperty direction = null;
			for (Property<?> p : clickedBlock.getProperties()) {
				if (p instanceof DirectionProperty) {
					direction = (DirectionProperty) p;
				}
			}

			Vector3d cp = ctx.getClickLocation();
			BlockPos p = ctx.getClickedPos();
			Vector3d diff = cp.subtract(new Vector3d(p.getX(), p.getY(), p.getZ()));
			float xval = (float) (diff.x * ctx.getClickedFace().getNormal().getZ());
			float zval = (float) (diff.z * -ctx.getClickedFace().getNormal().getX());
			float value = xval + zval;

			int amt;
			int amt2;
			if (ctx.getClickedFace() == Direction.WEST || ctx.getClickedFace() == Direction.SOUTH) {
				amt = 0;
				amt2 = -1;
			} else {
				if (ctx.getClickedFace() == Direction.EAST || ctx.getClickedFace() == Direction.NORTH) {
					amt = 1;
					amt2 = 0;
				} else {
					return ActionResultType.PASS;
				}
			}

			boolean func_1 = value + amt > diff.y;
			boolean func_2 = value + amt2 > -diff.y;

			if (func_1 && func_2) {
				if (clickedBlock.getValue(direction) == Direction.UP || clickedBlock.getValue(direction) == Direction.DOWN) {
					ctx.getLevel().setBlock(p, clickedBlock.setValue(direction, ctx.getClickedFace()), 3);
				} else {
					ctx.getLevel().setBlock(p, clickedBlock.rotate(ctx.getLevel(), p, Rotation.COUNTERCLOCKWISE_90), 3);
				}
			} else {
				if (func_1) {
					try {
						ctx.getLevel().setBlock(p, clickedBlock.setValue(direction, Direction.DOWN), 3);
					} catch (IllegalArgumentException e) {
						ctx.getLevel().setBlock(p, clickedBlock.setValue(direction, ctx.getClickedFace()), 3);
					}
				} else {
					if (func_2) {
						try {
							ctx.getLevel().setBlock(p, clickedBlock.setValue(direction, Direction.UP), 3);
						} catch (IllegalArgumentException e) {
							ctx.getLevel().setBlock(p, clickedBlock.setValue(direction, ctx.getClickedFace().getOpposite()), 3);
						}
					} else {
						if (clickedBlock.getValue(direction) == Direction.UP || clickedBlock.getValue(direction) == Direction.DOWN) {
							ctx.getLevel().setBlock(p, clickedBlock.setValue(direction, ctx.getClickedFace()), 3);
						} else {
							ctx.getLevel().setBlock(p, clickedBlock.rotate(ctx.getLevel(), p, Rotation.CLOCKWISE_90), 3);
						}
					}
				}
			}

			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

}
