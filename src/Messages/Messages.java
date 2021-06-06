package Messages;

import Kits.KitTools.KitInfo;
import Main.PlayerStats;
import Util.Game;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftTippedArrow;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Messages {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    PlayerStats stats = PlayerStats.getStats();

    private static final String RAW_GRADIENT_HEX_REGEX = "<\\$#[A-Fa-f0-9]{6}>";

    private static String insertFades(String msg, String fromHex, String toHex, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean magic) {
        msg = msg.replaceAll("&k", "");
        msg = msg.replaceAll("&l", "");
        msg = msg.replaceAll("&m", "");
        msg = msg.replaceAll("&n", "");
        msg = msg.replaceAll("&o", "");
        int length = msg.length();
        Color fromRGB = Color.decode(fromHex);
        Color toRGB = Color.decode(toHex);
        double rStep = Math.abs((double) (fromRGB.getRed() - toRGB.getRed()) / length);
        double gStep = Math.abs((double) (fromRGB.getGreen() - toRGB.getGreen()) / length);
        double bStep = Math.abs((double) (fromRGB.getBlue() - toRGB.getBlue()) / length);
        if (fromRGB.getRed() > toRGB.getRed()) rStep = -rStep; //200, 100
        if (fromRGB.getGreen() > toRGB.getGreen()) gStep = -gStep; //200, 100
        if (fromRGB.getBlue() > toRGB.getBlue()) bStep = -bStep; //200, 100
        Color finalColor = new Color(fromRGB.getRGB());
        msg = msg.replaceAll(RAW_GRADIENT_HEX_REGEX, "");
        msg = msg.replace("", "<$>");
        for (int index = 0; index <= length; index++) {
            int red = (int) Math.round(finalColor.getRed() + rStep);
            int green = (int) Math.round(finalColor.getGreen() + gStep);
            int blue = (int) Math.round(finalColor.getBlue() + bStep);
            if (red > 255) red = 255; if (red < 0) red = 0;
            if (green > 255) green = 255; if (green < 0) green = 0;
            if (blue > 255) blue = 255; if (blue < 0) blue = 0;
//            System.out.println(red);
            /*System.out.println(green);
            System.out.println(blue);*/
            finalColor = new Color(red, green, blue);
            String hex = "#" + Integer.toHexString(finalColor.getRGB()).substring(2);

            //Bukkit.broadcastMessage(hex + msg);

            String formats = "";
            if (bold) formats += net.md_5.bungee.api.ChatColor.BOLD;
            if (italic) formats += net.md_5.bungee.api.ChatColor.ITALIC;
            if (underlined) formats += net.md_5.bungee.api.ChatColor.UNDERLINE;
            if (strikethrough) formats += net.md_5.bungee.api.ChatColor.STRIKETHROUGH;
            if (magic) formats += net.md_5.bungee.api.ChatColor.MAGIC;
            msg = msg.replaceFirst("<\\$>", "" + net.md_5.bungee.api.ChatColor.of(hex) + formats);
        }
        return msg;
    }

    private static final Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");
    static String format(String formatted) {
        Matcher match = pattern.matcher(formatted);
        while (match.find()) {
            String color = formatted.substring(match.start(), match.end());
            formatted = formatted.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            match = pattern.matcher(formatted);

        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', formatted);
    }
    public String trackMessage (Entity p) {
        return ChatColor.AQUA + "NOW TRACKING: " + ChatColor.GREEN + p.getName();
    }
    public String failedTrack () {
        return ChatColor.RED + "No players in range!";
    }
    public String pleaseWait () {
        return ChatColor.AQUA + "Please wait, a new game is about to begin.";
    }


    public String obfuscatedSpyTrack (List<Entity> nearest, Player pl) {
        StringBuilder finalMessage = new StringBuilder();
        for (Entity p : nearest) {
            double distance = p.getLocation().distance(pl.getLocation());
            String kit = kitInfo.getKitNameFormatted(kitInfo.getPlayerKit(p), p);
            String ign = p.getName();
            finalMessage.append(ChatColor.RED).append(ChatColor.MAGIC).append(ign+ ChatColor.translateAlternateColorCodes('&', " &c(&f" + (((int) Math.round(((LivingEntity) p).getHealth())) + "&c" + " ") + "❤&f)&c")).append(" [").append(ChatColor.AQUA).append(ChatColor.MAGIC).append(kit).append(ChatColor.RED).append(ChatColor.MAGIC).append("] ").append("is ").append(ChatColor.AQUA).append(ChatColor.MAGIC).append(Math.round(distance)).append(ChatColor.RED).append(ChatColor.MAGIC).append(" blocks away!").append("\n");

        }

        return finalMessage.toString();

    }
    public String spyTrack (List<Entity> nearest, Entity pl) {
        StringBuilder finalMessage = new StringBuilder();
        for (Entity p : nearest) {
            double distance = p.getLocation().distance(pl.getLocation());
            String kit = kitInfo.getKitNameFormatted(kitInfo.getPlayerKit(p),p);
            String ign = p.getName();
            finalMessage.append(ChatColor.RED).append(ign + ChatColor.translateAlternateColorCodes('&', " &c(&f" + (((int) Math.round(((LivingEntity) p).getHealth())) + "&c" + " ") + "❤&c)")).append(" [").append(ChatColor.AQUA).append(kit).append(ChatColor.RED).append("] ").append("is ").append(ChatColor.AQUA).append(Math.round(distance)).append(ChatColor.RED).append(" blocks away!").append("\n");

        }

        return finalMessage.toString();

    }
    public void sendToAll(TextComponent component) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(component);
        }
    }

    public String statsMessageCommand (Player p) {
        String s = "";
        s = s + ChatColor.translateAlternateColorCodes('&', "&6&l" + p.getName() + "'s " + "Stats:") + "\n";
        s = s + (ChatColor.translateAlternateColorCodes('&', "&b&lTotal Kills: " + stats.getTotalKills(p))  + "\n");
        s = s + (ChatColor.translateAlternateColorCodes('&', "&b&lTotal Wins: " + stats.getTotalWins(p)) + "\n");
        s = s + (ChatColor.translateAlternateColorCodes('&', "&b&lTotal Games Played: " + stats.getTotalGamesPlayed(p)+ "\n"));
        s = s + (ChatColor.translateAlternateColorCodes('&', "&b&lFavorite Kit: " + KitInfo.getSharedKitInfo().getKitUnformatted(stats.getMostFrequentlyUsedKit(p))));

        return s;
    }
    public String statsMessageCommand (OfflinePlayer p) {
        String s = "";
        s = s + ChatColor.translateAlternateColorCodes('&', "&6&l" + p.getName() + "'s " + "Stats:") + "\n";
        s = s + (ChatColor.translateAlternateColorCodes('&', "&b&lTotal Kills: " + stats.getTotalKills(p)  + "\n"));
        s = s + (ChatColor.translateAlternateColorCodes('&', "&b&lTotal Wins: " + stats.getTotalWins(p) + "\n"));
        s = s + (ChatColor.translateAlternateColorCodes('&', "&b&lTotal Games Played: " + stats.getTotalGamesPlayed(p) + "\n"));
        s = s + (ChatColor.translateAlternateColorCodes('&', "&b&lFavorite Kit: " + KitInfo.getSharedKitInfo().getKitUnformatted(stats.getMostFrequentlyUsedKit(p))));
        return s;
    }

    public List<String> statsMessageFormat (Player p) {
        List<String> formattedDesc = new ArrayList<>();
        formattedDesc.add(ChatColor.translateAlternateColorCodes('&', "&6&lYour Stats:"));

        formattedDesc.add(ChatColor.translateAlternateColorCodes('&', "&b&lTotal Kills: " + stats.getTotalKills(p)));
        formattedDesc.add(ChatColor.translateAlternateColorCodes('&', "&b&lTotal Wins: " + stats.getTotalWins(p)));
        formattedDesc.add(ChatColor.translateAlternateColorCodes('&', "&b&lTotal Games Played: " + stats.getTotalGamesPlayed(p)));
        formattedDesc.add(ChatColor.translateAlternateColorCodes('&', "&b&lFavorite Kit: " + KitInfo.getSharedKitInfo().getKitUnformatted(stats.getMostFrequentlyUsedKit(p))));
        return formattedDesc;
    }
    public TextComponent deathFormattedMessage (Entity killer, Player victim) {
                //entity on player

                String killerkit = kitInfo.getKitNameFormatted(kitInfo.getPlayerKit(killer), killer);
                String victimkit = kitInfo.getKitNameFormatted(kitInfo.getPlayerKit(victim), victim);

                TextComponent killerkitdesc = new TextComponent(killerkit);
                TextComponent victimkitdesc = new TextComponent(victimkit);
                //Bukkit.broadcastMessage(ChatColor.RED + formatDescription( KitInfo.kitDescription(kitInfo.getPlayerKit(killer))).toString());
                String description1 = ChatColor.AQUA + "";
                for (int i = 0; i < formatDescription( KitInfo.kitDescription(kitInfo.getPlayerKit(killer))).size(); i++) {
                    if (i == formatDescription( KitInfo.kitDescription(kitInfo.getPlayerKit(victim))).size()-1) {
                        description1 = description1 + formatDescription(KitInfo.kitDescription(kitInfo.getPlayerKit(killer))).get(i);

                    } else {
                        description1 = description1 + formatDescription(KitInfo.kitDescription(kitInfo.getPlayerKit(killer))).get(i) + "\n";
                    }                }
                String description2 = ChatColor.AQUA + "";
                for (int i = 0; i < formatDescription( KitInfo.kitDescription(kitInfo.getPlayerKit(victim))).size(); i++) {
                    if (i == formatDescription( KitInfo.kitDescription(kitInfo.getPlayerKit(victim))).size()-1) {
                        description2 = description2 + formatDescription(KitInfo.kitDescription(kitInfo.getPlayerKit(victim))).get(i);

                    } else {
                        description2 = description2 + formatDescription(KitInfo.kitDescription(kitInfo.getPlayerKit(victim))).get(i) + "\n";
                    }
                    }
                killerkitdesc.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                victimkitdesc.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                killerkitdesc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(description1)));
                victimkitdesc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(description2)));


        TextComponent mainComponent = new TextComponent(ChatColor.AQUA + victim.getName() + " (");

        if (killer.equals(victim)) {
            mainComponent.setColor(net.md_5.bungee.api.ChatColor.AQUA);
            mainComponent.addExtra(victimkitdesc);

            mainComponent.addExtra( ")" + " " + "was dishonored and committed ritual suicide");
            return mainComponent;
        }
        if (killer instanceof Player) {

                        String heldItem = ((Player)killer).getInventory().getItemInMainHand().getType().name();
                        heldItem = heldItem.replaceAll("_", " ").toLowerCase();
                        String[] itemNames = heldItem.split(" ");
                        heldItem = "";
                        for (int i = 0; i < itemNames.length; i++) {
                            heldItem = heldItem + itemNames[i].substring(0, 1).toUpperCase() + itemNames[i].substring(1) + " ";
                        }

                        mainComponent.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                        mainComponent.addExtra(victimkitdesc);

                        mainComponent.addExtra( ")" + " " + pickDeathMessage(heldItem) + " " + killer.getName() + " (");
                        mainComponent.addExtra(killerkitdesc);
                        mainComponent.addExtra(")" + "'s " + heldItem);
                        mainComponent.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                        return mainComponent;


        } else {
            //Bukkit.broadcastMessage("did not die to player");
                //Bukkit.broadcastMessage("mob killed player");
                mainComponent.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                mainComponent.addExtra(victimkitdesc);

                //mainComponent.addExtra( ")" + " ");

                Entity mob = killer;
                EntityType type = mob.getType();
                String newDeathMessage = "";
                switch (type) {
                    case ZOMBIE: newDeathMessage = "was mauled to death by a Zombie";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);
                        return mainComponent;
                    case CREEPER: newDeathMessage = "was ruthlessly blown up by a Creeper";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);
                        return mainComponent;
                    case ENDER_CRYSTAL: newDeathMessage = "forgot to be below the Ender Crystal";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);
                        return mainComponent;
                    case SKELETON: newDeathMessage = "was picked apart by a Skeleton";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);
                        return mainComponent;
                    case DROWNED: newDeathMessage = "was ripped apart by a Drowned";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);
                        return mainComponent;
                    case WITCH: newDeathMessage = "was magicked into submission by a Witch";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);
                        return mainComponent;
                    case BEE: newDeathMessage = "was buzzed into oblivion by a Bee";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);
                        return mainComponent;
                    case SLIME: newDeathMessage = "was slimed into submission by a Slime";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);
                        return mainComponent;
                    case SPIDER: newDeathMessage = "became Spider food";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);
                        return mainComponent;
                    case GUARDIAN: newDeathMessage = "became fish food by a Guardian";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);
                        return mainComponent;
                    case UNKNOWN: newDeathMessage = "died in mysterious, mysterious ways...";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);
                        return mainComponent;
                    case PRIMED_TNT: newDeathMessage = "was blown up into a million smithereens";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);
                        return mainComponent;

                    case PHANTOM: newDeathMessage = "was slain by a blue, flying creature";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);

                        return mainComponent;
//                    case ARROW: newDeathMessage = "was shot to death!";
//                        mainComponent.addExtra( ")" + " " + newDeathMessage);
//
//                        return mainComponent;
                    case BLAZE: newDeathMessage = "was burnt by a Blaze";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);

                        return mainComponent;
                    case WOLF: newDeathMessage = "was eaten alive by a pack of wolves";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);

                        return mainComponent;
                    case ENDERMAN: newDeathMessage = "was dismembered by an angry Enderman";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);

                        return mainComponent;
                    case FIREBALL: newDeathMessage = "was engulfed in flames from a Fireball";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);

                        return mainComponent;
                    case SPLASH_POTION: newDeathMessage = "was killed with magic";
                        mainComponent.addExtra( ")" + " " + newDeathMessage);

                        return mainComponent;
                }
                String str = type.name().toLowerCase();
                if (firstLetterConsonant(str)) {

                    newDeathMessage = "was killed by a " + str.substring(0, 1).toUpperCase() + str.substring(1);
                } else {

                    newDeathMessage = "was killed by an " + str.substring(0, 1).toUpperCase() + str.substring(1);

                }
                mainComponent.addExtra(")" + " " + newDeathMessage);

                return mainComponent;
            }




        //Bukkit.broadcastMessage(killer.getType().toString());
//
//        String newDeathMessage = "managed to die in a really strange way...";
////        mainComponent.setColor(net.md_5.bungee.api.ChatColor.AQUA);
////        mainComponent.addExtra(victimkitdesc);
//        mainComponent.addExtra( ")" + " " + newDeathMessage);
//
//        return mainComponent;

    }
    private static boolean firstLetterConsonant(String s)
    {
        char l = s.toLowerCase().trim().charAt(0);
        //check to see if the first letter is not a vowel
        return l != 'a' && l != 'e' && l != 'i' && l != 'o' && l != 'u';
    }

    public String pickDeathMessage(String s) {
        if (s.toLowerCase().contains("sword")) {
            return "was stabbed by";
        } else if (s.toLowerCase().contains("bow")){
            return "was shot by";
        } else if (s.toLowerCase().contains("air")) {
            return "was fisted into submission by";
        } else if (s.toLowerCase().contains("block")) {
            return "was blocked by";
        }
        Random random = new Random();
        int bound = 7;
        int message = random.nextInt(bound);
        if (message == 0) {
            return "was demolished by";
        }
        if (message == 1) {
            return "got destroyed by";
        }
        if (message == 2) {
            return "was put in the trash by";
        }
        if (message == 3) {
            return "was left in the dust by";
        }
        if (message == 4) {
            return "was turned inside out by";
        }
        if (message == 5) {
            return "was ravished by";
        }
        return "was demolished by";
    }

    public static String elysiumsTag () {
        String elysiumsMessage1 = insertFades("Elys", "#ff0000", "#ff9f00", true, false, false, false, false);
        String elysiumsMessage2 = insertFades("iums", "#ff9f00", "#ffffff", true, false, false, false,false);

        return format(ChatColor.translateAlternateColorCodes('&',elysiumsMessage1 + elysiumsMessage2));
    }

    public static List<String> elysiumsTagAnimation () {
        //String elysiumsMessage1 = insertFades("Elys", "#ff0000", "#ff9f00", true, false, false, false, false);
        //String elysiumsMessage2 = insertFades("iums", "#ff9f00", "#ffffff", true, false, false, false,false);

        String hex1 = "#ff2800"; //E
        String hex2 = "#ff5000"; //l
        String hex3 = "#ff7800"; //y
        String hex4 = "#ffa000"; //s
        String hex5 = "#ffb740"; //i
        String hex6 = "#ffcf80"; //u
        String hex7 = "#ffe7c0"; //m
        String hex8 = "#ffffff"; //s

        String e = "&lE";
        String l = "&ll";
        String y = "&ly";
        String s = "&ls";
        String i = "&li";
        String u = "&lu";
        String m = "&lm";
        String ss = "&ls";



        List<String> strings = new ArrayList<>();
        strings.add(hex1 + e + hex2 + l + hex3 + y + hex4 + s + hex5 + i + hex6 + u + hex7 + m + hex8 + ss);
        strings.add(hex8 + e + hex1 + l + hex2 + y + hex3 + s + hex4 + i + hex5 + u + hex6 + m + hex7 + ss);
        strings.add(hex7 + e + hex8 + l + hex1 + y + hex2 + s + hex3 + i + hex4 + u + hex5 + m + hex6 + ss);
        strings.add(hex6 + e + hex7 + l + hex8 + y + hex1 + s + hex2 + i + hex3 + u + hex4 + m + hex5 + ss);
        strings.add(hex5 + e + hex6 + l + hex7 + y + hex8 + s + hex1 + i + hex2 + u + hex3 + m + hex4 + ss);
        strings.add(hex4 + e + hex5 + l + hex6 + y + hex7 + s + hex8 + i + hex1 + u + hex2 + m + hex3 + ss);
        strings.add(hex3 + e + hex4 + l + hex5 + y + hex6 + s + hex7 + i + hex8 + u + hex1 + m + hex2 + ss);
        strings.add(hex2 + e + hex3 + l + hex4 + y + hex5 + s + hex6 + i + hex7 + u + hex8 + m + hex1 + ss);

        List<String> formatted = new ArrayList<>();
        for (String se : strings) {
            formatted.add(format(se));
        }

        return formatted;
    }

    public String welcome (Player p) {
        String elysiumsMessage1 = insertFades("Elys", "#ff0000", "#ff9f00", true, false, false, false, false);
        String elysiumsMessage2 = insertFades("iums", "#ff9f00", "#ffffff", true, false, false, false,false);


        return format(ChatColor.translateAlternateColorCodes('&', "&4&lWelcome to "  + elysiumsMessage1 + elysiumsMessage2  + "&r&4&l Hunger Games, &6&l" + p.getName() + "!\n&r&bThe Hunger Games is an all-out brawl with tons of unique kits to pick from, and more to come!\n&aYou can team with whoever you'd like, but there can be only one winner...\n&cTo get started, pick a kit with /kit!\n&eUnlock more kits by getting wins, or unlock kits temporarily by voting (/vote)!"));
    }

    public TextComponent changeDefault (EntityDamageEvent.DamageCause cause, Entity victim) {
        //s is default deathmessage
        //death is killed
        String victimkit = kitInfo.getKitNameFormatted(kitInfo.getPlayerKit(victim), victim);

//        s = s.replace(death.getName(), death.getName() + " (" + victimkit + ")");
//        s = ChatColor.AQUA + s;
       // Bukkit.broadcastMessage(ChatColor.RED + peopleLeftMessage() + "BOING");
        //s = s + "\n" + peopleLeftMessage();
        TextComponent mainComponent = new TextComponent(ChatColor.AQUA + victim.getName() + " (");
        mainComponent.setColor(net.md_5.bungee.api.ChatColor.AQUA);



        TextComponent victimkitdesc = new TextComponent(victimkit);

        String description2 = ChatColor.AQUA + "";
        for (int i = 0; i < formatDescription( KitInfo.kitDescription(kitInfo.getPlayerKit(victim))).size(); i++) {
            if (i == formatDescription( KitInfo.kitDescription(kitInfo.getPlayerKit(victim))).size()-1) {
                description2 = description2 + formatDescription(KitInfo.kitDescription(kitInfo.getPlayerKit(victim))).get(i);

            } else {
                description2 = description2 + formatDescription(KitInfo.kitDescription(kitInfo.getPlayerKit(victim))).get(i) + "\n";
            }
        }
        victimkitdesc.setColor(net.md_5.bungee.api.ChatColor.AQUA);
        victimkitdesc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(description2)));

        mainComponent.addExtra(victimkitdesc);
        mainComponent.addExtra( ")" + " ");

        //maincomponnent now looks like this: "Clew (None) "
        //Bukkit.broadcastMessage(cause.toString());
        switch (cause) {
            case FIRE_TICK: mainComponent.addExtra("sustained several burn wounds and died painfully");
                return mainComponent;

            case FIRE: mainComponent.addExtra("died in a tremendous blaze");
                return mainComponent;

            case BLOCK_EXPLOSION: mainComponent.addExtra("was blown up into a million pieces");
                return mainComponent;

            case FALL: mainComponent.addExtra("fell from a high place and died instantly");
                return mainComponent;

            case LAVA: mainComponent.addExtra("went swimming in hot orange water");
                return mainComponent;

            case VOID: mainComponent.addExtra("died in mysterious ways...");
                return mainComponent;

            case MAGIC: mainComponent.addExtra("was haunted and possessed magically");
                return mainComponent;

            case CUSTOM: mainComponent.addExtra("died in mysterious ways...");
                return mainComponent;

            case DROWNING: mainComponent.addExtra("drowned to death, and had their bloated body resurface");
                return mainComponent;
            case POISON: mainComponent.addExtra("was bitten by something venomous...");
                return mainComponent;

            case PROJECTILE: mainComponent.addExtra("was shot in between the eyes and died painfully");
                return mainComponent;

            case ENTITY_EXPLOSION: mainComponent.addExtra("was blown up into a million smithereens");
            return mainComponent;
            case SUFFOCATION: mainComponent.addExtra("suffocated to death");
                return mainComponent;

            case CONTACT: mainComponent.addExtra("was squished to death");
            return mainComponent;

            case STARVATION: mainComponent.addExtra("starved painfully to death");
            return mainComponent;

            case LIGHTNING: mainComponent.addExtra("was struck by lightning and died instantly");
            return mainComponent;

            case SUICIDE: mainComponent.addExtra("committed suicide");
            return mainComponent;

            case HOT_FLOOR: mainComponent.addExtra("stepped on the wrong block and died painfully");
            return mainComponent;

            case CRAMMING: mainComponent.addExtra("was crushed to death");
            return mainComponent;

            case FLY_INTO_WALL: mainComponent.addExtra("experienced kinetic energy");
            return mainComponent;

            case MELTING: mainComponent.addExtra("melted to death");
            return mainComponent;


        }

        String newDeathMessage = "managed to die in a strange way...";
        mainComponent.addExtra(newDeathMessage);
        return mainComponent;

        //npc lookclose and vulnerable

    }

    public String peopleLeftMessage () {
        return ChatColor.AQUA +  "" + game.currentPlayersLeft() + " players remaining.";
    }

    public String kickMessage (Player p) {
        if (game.humansLeft().size() != 1) {
            return ChatColor.GREEN + "You died!\n" +
                    p.getName() + "'s stats:\n" +
                    "Your kills: " + game.getKills(p.getUniqueId()) + "\nPlayers left: " + game.currentPlayerList().size() + "/" + game.playerCount();
        } else {
            if (game.getWinner() != null) {
                return ChatColor.GREEN + "You died!\n" +
                        p.getName() + "'s stats:\n" +
                        "Your kills: " + game.getKills(p.getUniqueId()) + "\n" + ChatColor.AQUA + "WINNER: " + game.getWinner().getName(); //NPE at this line, happens when NPC kills player
            } else {
                return ChatColor.GREEN + "You died!\n" +
                        p.getName() + "'s stats:\n" +
                        "Your kills: " + game.getKills(p.getUniqueId()) + "\n" + ChatColor.AQUA;
            }
            }
        }
    public String help (String s) {
        if (s.equals("feast")) {
            return ChatColor.YELLOW + "There are two types of Feasts - the Mini-Feast and the Feast. The mini-feasts are random and can occur at any point during the game. The main feast happens only near the end game, and always spawns dead center of the arena. Feasts contain lots of food, armor, loot and kit refills that can refill needed items for your kit. The main feast can contain diamond gear and potions as well as better chances of higher-tier items. The mini-feasts contain gear and loot as well.";
        }
        if (s.equals("soup")) {
            return ChatColor.YELLOW + "Soup is an important component of HG. You can craft soups with a multitude of recipes: 1 Cactus + 1 Bowl, 1 Cocoa Beans + 1 Bowl, 1 Red Mushroom + 1 Bowl, 1 Brown Mushroom + 1 Bowl, 3 Sugar Cane + Bowl, 3 Apples + Bowl. Soup heals 3 hearts of health or 3 food bars instantly.";

        }
        if (s.equals("kits")) {
            return "kits help here";

        }
        return "";
    }
    public void sendTitleMessage (Player p, String message, ChatColor color, String sub) {
        p.sendTitle(color + message, color + sub, 10, 10, 10);
    }

    public static List<String> formatDescription (List<String> string) {
        List<String> placeholder = new ArrayList<>();
        if (string.size() != 0) {
            List<String> strings = justify(string.get(0), 30);
            return strings;
        }
        return placeholder;


    }
    private static List<String> justify(String s, int limit) {
        List<String> strings = new ArrayList<>();
        StringBuilder justifiedText = new StringBuilder();
        StringBuilder justifiedLine = new StringBuilder();
        String[] words = s.split(" ");
        for (int i = 0; i < words.length; i++) {
            justifiedLine.append(words[i]).append(" ");
            if (i+1 == words.length || justifiedLine.length() + words[i+1].length() > limit) {
                justifiedLine.deleteCharAt(justifiedLine.length() - 1);
                strings.add(justifiedLine.toString());
                //justifiedText.append(justifiedLine.toString()).append(System.lineSeparator());
                justifiedLine = new StringBuilder();
            }
        }
        return strings;
    }
}
