package com.natamus.firespreadtweaks.forge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.firespreadtweaks.events.FireSpreadEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ForgeFireSpreadEvent {
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		Level level = e.world;
		if (level.isClientSide || !e.phase.equals(Phase.END)) {
			return;
		}

		FireSpreadEvent.onWorldTick((ServerLevel)level);
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (level == null) {
			return;
		}

		FireSpreadEvent.onWorldLoad((ServerLevel)level);
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (level == null) {
			return;
		}		

		FireSpreadEvent.onWorldUnload((ServerLevel)level);
	}
	
	@SubscribeEvent
	public void onNeighbourNotice(BlockEvent.NeighborNotifyEvent e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (level == null) {
			return;
		}

		FireSpreadEvent.onNeighbourNotice(level, e.getPos(), e.getState(), e.getNotifiedSides(), e.getForceRedstoneUpdate());
	}
}
