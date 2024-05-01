package com.natamus.firespreadtweaks.neoforge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.firespreadtweaks.events.FireSpreadEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class NeoForgeFireSpreadEvent {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post e) {
		Level level = e.getLevel();
		if (level.isClientSide) {
			return;
		}

		FireSpreadEvent.onWorldTick((ServerLevel)level);
	}
	
	@SubscribeEvent
	public static void onWorldLoad(LevelEvent.Load e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		FireSpreadEvent.onWorldLoad((ServerLevel)level);
	}
	
	@SubscribeEvent
	public static void onWorldUnload(LevelEvent.Unload e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}		

		FireSpreadEvent.onWorldUnload((ServerLevel)level);
	}
	
	@SubscribeEvent
	public static void onNeighbourNotice(BlockEvent.NeighborNotifyEvent e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		FireSpreadEvent.onNeighbourNotice(level, e.getPos(), e.getState(), e.getNotifiedSides(), e.getForceRedstoneUpdate());
	}
}
