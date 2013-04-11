/*
 * This file is part of ForceTools.
 *
 * © 2013 AlmuraDev <http://www.almuradev.com/>
 * ForceTools is licensed under the GNU General Public License.
 *
 * ForceTools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ForceTools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License. If not,
 * see <http://www.gnu.org/licenses/> for the GNU General Public License.
 */
package com.almuradev.forcetools;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ForceToolsPlugin extends JavaPlugin implements Listener {

	public void onEnable() {			
		PluginManager pm = this.getServer().getPluginManager();
		getServer().getPluginManager().registerEvents(this, this);		
		FileConfiguration config = this.getConfig();

        // Read in default config.yml
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
        }
	}

	@EventHandler
	public void blockBreak(BlockBreakEvent event) {
		if (event.isCancelled()  || event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}		
		if (!event.getPlayer().hasPermission("ForceTool.ignore")) {				
			if (getConfig().contains("forcetool." + event.getBlock().getTypeId())) {
				String raw = getConfig().getString("forcetool." + event.getBlock().getTypeId());
				String[] split = raw.split(",");
				int i = 0;
				String required = "toolString";
				boolean allowed = false;
				for (String s : split) {					
					if (i == 0)	{
						required = s;
					} else if (event.getPlayer().getItemInHand() != null) {
						if (event.getPlayer().getItemInHand().getTypeId() == Integer.parseInt(s)) {
							allowed = true;
						}
					}
					i++;
				}

				if (!allowed) {						
					event.getPlayer().sendMessage(ChatColor.WHITE + "[ForceTool] " + ChatColor.RED + "You must use [ " + ChatColor.WHITE + required + ChatColor.RED + " ] to break this block.");
					event.setCancelled(true);
				}
			}
		}
	}
}

