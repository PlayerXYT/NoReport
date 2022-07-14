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
	// 8 bits of active plugins to save memory
	private static byte activePlugins = 0;
	
	// Constants for setting and getting plugins
	// Constant = 2^pluginPosition
	public static final byte ESSX = 1;
	public static final byte LP = 2;
	
	// Luckperms plugin integration
	private static LuckPerms lp;
	
	/*
	 * Initializes the color manager
	 * Should be called only once
	 */
	public static void init() {
		try {
			lp = Bukkit.getServicesManager().getRegistration(LuckPerms.class).getProvider();
		} catch(Exception e) {}
	}
	
	/*
	 * Sets the active plugin with bit math mask
	 * @param plugin The plugin to set, use above constants
	 * @param active What the bit should be set as
	 */
	public static void setActive(byte plugin, boolean active) {
		activePlugins = (byte) (activePlugins & ~plugin);
		activePlugins += plugin * (active?1:0);
	}
	/*
	 * Gets if the plugin is registered as active with bit math mask
	 * @param plugin The plugin to get, use above constants
	 * @return If the plugin is active
	 */
	public static boolean getActive(byte plugin) {return (activePlugins & plugin) > 1;}
	
	/*
	 * Mimic the formatting of any active plugin or use the Minecraft default
	 * <Player> Message
	 * @param player The message sender
	 * @param message The message sent
	 * @return The formatted text
	 */
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
	
	/*
	 * String.format() with less parameters
	 * @param color The username's color
	 * @param player The message sender
	 * @param message The message
	 * @return The concatonated message
	 */
	public static String format(String color, Player player, String message) {
		return String.format("<%s%s%s> %s", color, player.getDisplayName(), ChatColor.RESET, message);
	}
	
	/*
	 * Short form for testing if a plugin is active
	 * @param name The name of the plugin (see plugin.yml)
	 * @return If the plugin is active on this server
	 */
	public static boolean getPlugin(String name) {return Bukkit.getPluginManager().getPlugin(name)!=null;}
}
