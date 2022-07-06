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
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onChat(PlayerChatEvent e) {
		e.setCancelled(true);
		String message = ColorManager.getColoredText(e.getPlayer(), e.getMessage());
		for(Player p : getServer().getOnlinePlayers()) {
			p.sendMessage(message);
		}
	}
	
}
