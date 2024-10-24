package com.natamus.firespreadtweaks;

import com.natamus.collective.check.RegisterMod;
import com.natamus.collective.check.ShouldLoadCheck;
import com.natamus.collective.fabric.callbacks.CollectiveBlockEvents;
import com.natamus.collective.fabric.callbacks.CollectiveWorldEvents;
import com.natamus.firespreadtweaks.events.FireSpreadEvent;
import com.natamus.firespreadtweaks.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class ModFabric implements ModInitializer {
	
	@Override
	public void onInitialize() {
		if (!ShouldLoadCheck.shouldLoad(Reference.MOD_ID)) {
			return;
		}

		setGlobalConstants();
		ModCommon.init();

		loadEvents();

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}

	private void loadEvents() {
		ServerTickEvents.START_WORLD_TICK.register((ServerLevel world) -> {
			FireSpreadEvent.onWorldTick(world);
		});

		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel world) -> {
			FireSpreadEvent.onWorldLoad(world);
		});

		CollectiveWorldEvents.WORLD_UNLOAD.register((ServerLevel world) -> {
			FireSpreadEvent.onWorldUnload(world);
		});

		CollectiveBlockEvents.NEIGHBOUR_NOTIFY.register((Level world, BlockPos pos, BlockState state, EnumSet<Direction> notifiedSides, boolean forceRedstoneUpdate) -> {
			FireSpreadEvent.onNeighbourNotice(world, pos, state, notifiedSides, forceRedstoneUpdate);
			return true;
		});
	}

	private static void setGlobalConstants() {

	}
}
