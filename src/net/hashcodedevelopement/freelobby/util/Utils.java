package net.hashcodedevelopement.freelobby.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.SpigotConfig;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class Utils {

	public static ArrayList<Material> items = new ArrayList<>();

	public static boolean joinMessage, quitMessage, firstMessage, commandWhitelist, singleServer;

	public static int playerCount;
	public static String prefix, fehler, joinMsg, quitMsg, tablistHeader, tablistFooter, commandNotFound;
	public static List<String> firstjoinMsg;
	public static String world;
	
	public static File file;
	public static FileConfiguration cfg;

	public static File lobbyitemFile;
	public static FileConfiguration lobbyitemCfg;

	static {
		fehler = prefix + "�4�lFehler�7:�c ";

		file = new File("plugins//LobbySystem//Einstellungen//config.yml");
		cfg = YamlConfiguration.loadConfiguration(file);

		lobbyitemFile = new File("plugins//LobbySystem//Einstellungen//LobbyItems.yml");
		lobbyitemCfg = YamlConfiguration.loadConfiguration(lobbyitemFile);
	}
	
	public static void sendActionbar(String message, UUID uuid) {
		PlayerConnection connection = ((CraftPlayer) Bukkit.getPlayer(uuid)).getHandle().playerConnection;
		IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat packet = new PacketPlayOutChat(icbc, (byte) 2);
		connection.sendPacket(packet);
	}

	public static void loadValues() {
		if (!cfg.contains("Prefix")) {
			cfg.options().header(
					"Variablen: %prefix% = Prefix, %PlayerName% = Name des Spielers, %PlayerCount% = Zahl aller gejointen Spieler, %NewLine% = Neue Zeile");
			cfg.set("Prefix", "&6&lL&e&lobby&6&lS&e&lystem &8� &7");
			lobbyitemCfg.set("0.Slot", 1);
			lobbyitemCfg.set("0.Name", "&7Navigator");
			lobbyitemCfg.set("0.Material", Material.COMPASS.toString());
			lobbyitemCfg.set("0.Command", "/navigator");
			lobbyitemCfg.set("0.Action", Action.RIGHT_CLICK_AIR.toString());
			lobbyitemCfg.set("0.SecondAction", Action.RIGHT_CLICK_BLOCK.toString());
			lobbyitemCfg.set("1.Slot", 5);
			lobbyitemCfg.set("1.Name", "&7Spieler verstecken");
			lobbyitemCfg.set("1.Material", Material.BLAZE_ROD.toString());
			lobbyitemCfg.set("1.Command", "/playerhider");
			lobbyitemCfg.set("1.Action", Action.RIGHT_CLICK_AIR.toString());
			lobbyitemCfg.set("1.SecondAction", Action.RIGHT_CLICK_BLOCK.toString());
			lobbyitemCfg.set("2.Slot", 9);
			lobbyitemCfg.set("2.Name", "&7Profil");
			lobbyitemCfg.set("2.Material", Material.DIAMOND.toString());
			lobbyitemCfg.set("2.Command", "/profil");
			lobbyitemCfg.set("2.Action", Action.RIGHT_CLICK_AIR.toString());
			lobbyitemCfg.set("2.SecondAction", Action.RIGHT_CLICK_BLOCK.toString());
			lobbyitemCfg.set("3.Slot", 3);
			lobbyitemCfg.set("3.Name", "&aErstelle deine &eeigenen&a LobbyItems!");
			lobbyitemCfg.set("3.Material", Material.EMERALD.toString());
			lobbyitemCfg.set("3.Command", "/say Alle Items sind in der LobbyItems.yml editierbar.");
			lobbyitemCfg.set("3.Action", Action.RIGHT_CLICK_AIR.toString());
			lobbyitemCfg.set("3.SecondAction", Action.RIGHT_CLICK_BLOCK.toString());
		}
		if (!cfg.contains("JoinNachricht")) {
			cfg.set("JoinNachricht.Bool", true);
			cfg.set("JoinNachricht.Nachricht", "%prefix%Der Spieler &b%PlayerName% &7ist nun &aOnline&7!");
		}
		if (!cfg.contains("QuitNachricht")) {
			cfg.set("QuitNachricht.Bool", true);
			cfg.set("QuitNachricht.Nachricht", "%prefix%Der Spieler &b%PlayerName% &7ist nun &cOffline&7!");
		}
		if (!cfg.contains("CommandWhitelist")) {
			List<String> list = new ArrayList<>();
			list.add("navigator");
			list.add("playerhider");
			list.add("profil");
			cfg.set("CommandWhitelist.List", list);
			cfg.set("CommandWhitelist.Bool", true);
		}
		if (!cfg.contains("GejointeSpieler")) {
			cfg.set("GejointeSpieler", 0);
		}
		if (!cfg.contains("Mode")) {
			cfg.set("Mode.BungeeCord", true);
			cfg.set("Mode.SingleServer", false);
			cfg.set("Mode.SingleServerWorld", "world");
		}
		if (!cfg.contains("CommandNotFound")) {
			cfg.set("CommandNotFound", "%prefix%Dieser Command wurde nicht gefunden!");
		}
		if (!cfg.contains("FirstjoinNachricht")) {
			List<String> msg = new ArrayList<>();
			msg.add(" ");
			msg.add("&8&m--------------------");
			msg.add("%prefix%Der Spieler %PlayerName% ist neu auf dem Server! &b#%PlayerCount%");
			msg.add("&8&m--------------------");
			msg.add(" ");

			cfg.set("FirstjoinNachricht.Bool", true);
			cfg.set("FirstjoinNachricht.Nachricht", msg);
		}
		if (!cfg.contains("Tablist")) {
			cfg.set("Tablist.Header", "&0%NewLine%&8* &7Dein ServerName &8*%NewLine%&7Dein Motto%NewLine%&0");
			cfg.set("Tablist.Footer", "&0%NewLine%&7Teamspeak: &ats.arzania.eu%NewLine%&7Website: &aarzania.eu%NewLine%&7Twitter: &b@ArzaniaEU%NewLine%&0");
		}
		saveLobbyitemCfg();
		saveCfg();

		singleServer = cfg.getBoolean("Mode.SingleServer");
		if(singleServer){
			world = cfg.getString("Mode.SingleServerWorld");
		}
		
		for (String key : lobbyitemCfg.getKeys(false)) {
			items.add(Material.getMaterial(lobbyitemCfg.getString(key + ".Material")));
		}

		joinMessage = cfg.getBoolean("JoinNachricht.Bool");
		quitMessage = cfg.getBoolean("QuitNachricht.Bool");
		firstMessage = cfg.getBoolean("FirstjoinNachricht.Bool");
		commandWhitelist = cfg.getBoolean("CommandWhitelist.Bool");

		tablistFooter = ChatColor.translateAlternateColorCodes('&', cfg.getString("Tablist.Footer").replace("%NewLine%", "\n"));
		tablistHeader = ChatColor.translateAlternateColorCodes('&', cfg.getString("Tablist.Header").replace("%NewLine%", "\n"));
		prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("Prefix"));
		playerCount = cfg.getInt("GejointeSpieler");
		joinMsg = cfg.getString("JoinNachricht.Nachricht");
		quitMsg = cfg.getString("QuitNachricht.Nachricht");
		firstjoinMsg = cfg.getStringList("FirstoinNachricht.Nachricht");
		commandNotFound = cfg.getString("CommandNotFound").replace("%prefix%", prefix);
		
		SpigotConfig.unknownCommandMessage = commandNotFound;
	}

	public static void saveCfg() {
		try {
			Utils.cfg.save(Utils.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveLobbyitemCfg() {
		try {
			Utils.lobbyitemCfg.save(Utils.lobbyitemFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void noPermissions(Player player) {
		player.sendMessage(Utils.fehler + "Dazu hast du keine �nBerechtigung�c!");
		player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
	}

	public static boolean checkName(String Name, ItemStack stack) {
		if (stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()
				&& stack.getItemMeta().getDisplayName().equals(Name)) {
			return true;
		} else {
			return false;
		}
	}

	public static ArrayList<String> buildmode = new ArrayList<>();

	public static boolean isBuildMode(String player) {
		return buildmode.contains(player);
	}

	public static boolean getChatState(UUID uuid){
		boolean toReturn = false;
		
		File file = new File("plugins//Lobbysystem//Playerdata//"+uuid+".yml");
		FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		
		if(!configuration.contains("Chat")){
			configuration.set("Chat", true);
			try {
				configuration.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(configuration.getBoolean("Chat")){
			toReturn = true;
		} else toReturn = false;
		
		return toReturn;
	}
	
	public static String getChatStateString(UUID uniqueId) {
		String toReturn = "�8(�4Fehler�8)";
		
		File file = new File("plugins//Lobbysystem//Playerdata//"+uniqueId+".yml");
		FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		
		if(!configuration.contains("Chat")){
			configuration.set("Chat", true);
			try {
				configuration.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(configuration.getBoolean("Chat")){
			toReturn = "�8(�a�lAn�8)";
		} else toReturn = "�8(�c�lAus�8)";
		
		return toReturn;
	}
}