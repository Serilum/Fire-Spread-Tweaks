package com.natamus.firespreadtweaks.events;

import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.HashMapFunctions;
import com.natamus.firespreadtweaks.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class FireSpreadEvent {
	private static final HashMap<BlockPos, Integer> ticksleft = new HashMap<BlockPos, Integer>();
	private static final HashMap<Level, CopyOnWriteArrayList<BlockPos>> firepositions = new HashMap<Level, CopyOnWriteArrayList<BlockPos>>();

	private static final List<Block> fireblocks = new ArrayList<Block>(Arrays.asList(Blocks.NETHERRACK, Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND, Blocks.SOUL_SOIL));

	public static void onWorldTick(ServerLevel level) {
		for (BlockPos firepos : HashMapFunctions.computeIfAbsent(firepositions, level, k -> new CopyOnWriteArrayList<BlockPos>())) {
			if (!ticksleft.containsKey(firepos)) {
				ticksleft.put(firepos, Util.getFireBurnDurationInTicks());
				continue;
			}

			int tl = ticksleft.get(firepos) -1;
			if (tl <= 0) {
				ticksleft.remove(firepos);
				firepositions.get(level).remove(firepos);

				BlockState firestate = level.getBlockState(firepos);
				Block fireblock = firestate.getBlock();
				if (fireblock instanceof FireBlock) {
					level.setBlockAndUpdate(firepos, Blocks.AIR.defaultBlockState());
				}
 				continue;
			}

			ticksleft.put(firepos, tl);
		}
	}

	public static void onWorldLoad(ServerLevel level) {
		BooleanValue firetickvalue = level.getGameRules().getRule(GameRules.RULE_DOFIRETICK);
		if (firetickvalue.get()) {
			firetickvalue.set(false, level.getServer());
		}
	}

	public static void onWorldUnload(ServerLevel level) {
		for (BlockPos firepos : HashMapFunctions.computeIfAbsent(firepositions, level, k -> new CopyOnWriteArrayList<BlockPos>())) {
			BlockState firestate = level.getBlockState(firepos);
			Block fireblock = firestate.getBlock();
			if (fireblock instanceof FireBlock) {
				level.setBlockAndUpdate(firepos, Blocks.AIR.defaultBlockState());
			}
		}
	}

	public static void onNeighbourNotice(Level level, BlockPos pos, BlockState state, EnumSet<Direction> notifiedSides, boolean forceRedstoneUpdate) {
		if (level.isClientSide) {
			return;
		}

		Block block = state.getBlock();
		if (!(block instanceof FireBlock)) {
			return;
		}

		Block belowblock = level.getBlockState(pos.below()).getBlock();
		if (BlockFunctions.isOneOfBlocks(fireblocks, belowblock)) {
			return;
		}

		ticksleft.put(pos, Util.getFireBurnDurationInTicks());
		HashMapFunctions.computeIfAbsent(firepositions, level, k -> new CopyOnWriteArrayList<BlockPos>()).add(pos);
	}
}
