package com.x.noreport;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		// Initialize the color manager
		ColorManager.init();
		
		// Register chat events
		getServer().getPluginManager().registerEvents(this, this);
		
		// Set soft depended plugins for plugin integration
		if(ColorManager.getPlugin("Essentials")) ColorManager.setActive(ColorManager.ESSX, true);
		if(ColorManager.getPlugin("Luckperms")) ColorManager.setActive(ColorManager.LP, true);
	}
	
	// All chat events will be cancelled to prevent message association with the player
	@EventHandler
	public void onChat(PlayerChatEvent e) {
		e.setCancelled(true);
		// Format text based on active plugins
		String message = ColorManager.getColoredText(e.getPlayer(), e.getMessage());
		// Send message from the server to prevent any reports
		for(Player p : getServer().getOnlinePlayers()) {
			p.sendMessage(message);
		}
	}
	
}
