package com.natamus.firespreadtweaks.forge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.firespreadtweaks.events.FireSpreadEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import java.lang.invoke.MethodHandles;

public class ForgeFireSpreadEvent {
	public static void registerEventsInBus() {
		BusGroup.DEFAULT.register(MethodHandles.lookup(), ForgeFireSpreadEvent.class);
	}

	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post e) {
		Level level = e.level;
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
