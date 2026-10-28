package com.serilum.firespreadtweaks.util;

import com.natamus.collective.data.GlobalVariables;
import com.serilum.firespreadtweaks.config.ConfigHandler;

public class Util {
	public static int getFireBurnDurationInTicks() {
		int duration = ConfigHandler.timeFireBurnsInTicks;
		if (ConfigHandler.enableRandomizedFireDuration) {
			int min = ConfigHandler.MinRandomExtraBurnTicks;
			int max = ConfigHandler.MaxRandomExtraBurnTicks;
			
			int randomized = GlobalVariables.random.nextInt(max-min) + min;
			duration += randomized;
		}
		
		return duration;
	}
}
