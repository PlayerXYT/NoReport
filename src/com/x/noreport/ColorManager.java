package com.x.noreport;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;

import net.luckperms.api.LuckPerms;

public final class ColorManager {
	private static byte activePlugins = 0;
	
	public static final byte ESSX = 1;
	public static final byte LP = 2;
	
	public static void setActive(byte plugin, boolean active) {
		activePlugins = (byte) (activePlugins & ~plugin);
		activePlugins += plugin * (active?1:0);
	}
	public static boolean getActive(byte plugin) {return (activePlugins & plugin) > 1;}
	
	public static final LuckPerms lp = Bukkit.getServicesManager().getRegistration(LuckPerms.class).getProvider();
	
	public static String getColoredText(Player player, String message) {
		
		ChatColor color = ChatColor.RESET;
		
		if(getActive(LP)) {
			net.luckperms.api.model.user.User lpuser = lp.getPlayerAdapter(Player.class).getUser(player);
			String prefix = lpuser.getCachedData().getMetaData().getPrefix();
			return format(prefix, player, message);
		} else if(getActive(ESSX) && player.isOp()) {
			Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
			File data = new File(ess.getDataFolder().getAbsolutePath()+"/config.yml");
			try {
				Scanner s = new Scanner(data);
				
				while(s.hasNextLine()) {
					String line = s.nextLine();
					if(line.split(":")[0].trim().equalsIgnoreCase("ops-name-color")) {
						if(line.split(":")[1].trim().equalsIgnoreCase("true")) {
							s.close();
							return format(ChatColor.RED.toString(), player, message);
						}
						else
							break;
					}
				}
				
				s.close();
			} catch(IOException e) {e.printStackTrace();}
		}
		
		return format(color.toString(), player, message);
	}
	
	public static String format(String color, Player player, String message) {
		return String.format("<%s%s%s> %s", color, player.getDisplayName(), ChatColor.RESET, message);
	}
}
