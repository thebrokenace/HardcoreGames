package Util;

import Main.HardcoreGames;
import Main.PlayerStats;
import com.github.johnnyjayjay.spigotmaps.MapBuilder;
import com.github.johnnyjayjay.spigotmaps.RenderedMap;
import com.github.johnnyjayjay.spigotmaps.rendering.AnimatedTextRenderer;
import com.github.johnnyjayjay.spigotmaps.rendering.ImageRenderer;
import com.github.johnnyjayjay.spigotmaps.util.ImageTools;
import org.bukkit.Color;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class WinHandler {
//fix armor stand erorr + winhandler crown + currentgamenumber + help kits + fix feast and mini lag
    public static void rewardWinner (Player p) throws IOException {


        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (!pl.equals(p)) {
                pl.teleport(p.getLocation());
            }
        }
        Game game = Game.getSharedGame();
        PlayerStats stats = PlayerStats.getStats();
        PlayerStats.getStats().addWinForPlayer(p);


        p.setInvulnerable(true);
        try {
            particlesface(p);
        } catch (IOException exception) {
            exception.printStackTrace();
        }











//        //https://i.gifer.com/SBMP.gif
//


        URL url = new URL("https://cravatar.eu/head/"+p.getName());
        BufferedImage catImage = ImageIO.read(url); // read an image from a source, e.g. a file
        catImage = ImageTools.resizeToMapSize(catImage); // resize the image to the minecraft map size

        URL crownImage = new URL("https://i.imgur.com/PhuDyWQ.png");
        BufferedImage crown = ImageIO.read(crownImage);



        crown = ImageTools.resizeToMapSize(crown);



        URL backImage = new URL("https://steamuserimages-a.akamaihd.net/ugc/513756752757597167/BBCBAEE1A6FCD6CE0D3B4D715BAADDBA8BEE0E7E/");
        BufferedImage background = ImageIO.read(backImage);
        background = ImageTools.resizeToMapSize(background);

        catImage = overlayImages(background, catImage);
        catImage = overlayImages(catImage, crown);
        ImageRenderer catRenderer = ImageRenderer.builder()
                .addPlayers(p) // set the players this map should be rendered to (omitting this means it renders for everyone)
                .image(catImage) // set the image to render

                .build(); // build the instance

        Dimension mapSize = ImageTools.MINECRAFT_MAP_SIZE; // the dimensions of a Minecraft map (in pixels)




        String one = ("§16;Congratulations");
        String two =  centerString(one.length()-4, p.getName());
        String three = centerString (one.length()-4, "for winning" + " the ");
        String threehalf = centerString(one.length() -4, stats.getCurrentGame() + "th");
        String four = centerString(one.length()-4, "Elysiums");
        String five = centerString(one.length()-4, "Hardcore Games!");
        String six = centerString(one.length()-4,"Your total wins: " + stats.getTotalWins(p));
        AnimatedTextRenderer renderer = AnimatedTextRenderer.builder()
                .addLines(one, two, three, threehalf, four, five, six)
                .charsPerSecond(4) // 10 characters should appear each second
                .delay(1) // start the animation after 20 ticks (1 second)
                .startingPoint(new Point(mapSize.width/4, mapSize.height / 2))
                .build();

        RenderedMap map = MapBuilder.create() // make a new builder
                 // set a MapStorage for the map
                .addRenderers(catRenderer, renderer) // add the renderers to this map
                .world(p.getWorld()) // set the world this map is bound to, e.g. the world of the target player
                .build(); // build the map

        ItemStack mapItem = map.createItemStack(); // get an ItemStack from this map to work with
        ItemMeta meta = mapItem.getItemMeta();
        if (meta != null)
        meta.setDisplayName(ChatColor.BLUE + "CONGRATULATIONS ON WINNING " + p.getName().toUpperCase() + "!!");
        mapItem.setItemMeta(meta);
        glowItem(mapItem);
        p.getInventory().setItemInMainHand(mapItem);

        //        MapView view = Bukkit.createMap(w);
//        for(MapRenderer renderer : view.getRenderers())
//            view.removeRenderer(renderer);
//        view.addRenderer(myCustomRenderer);
//        i.setDurability(view.getId());


        if (game.getWinner() != null) {

            dragon(p);
            new BukkitRunnable() {
                int time = 0;
                @Override
                public void run() {
                    if (time < 52 && time >= 0) {
                        p.getInventory().setItem(time, mapItem);
                    }

                    time++;

                    Bukkit.broadcastMessage(ChatColor.AQUA + "Congratulations " + p.getName() + " for winning!");
                    playFireworks(p);

                    particles(p);
                    p.giveExp(50);
                    if (time >= 45) {
                        for (Player pl : Bukkit.getOnlinePlayers()) {
                            pl.kickPlayer(ChatColor.AQUA + "Congratulations for winning, " + p.getName() + "!");
                        }
                        game.restartGame(game);
                        cancel();

                    }
                }
            }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);
        }
    }

    public static String centerString (int width, String s) {
        return String.format("%-" + width  + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }


    public static void playFireworks(Player p) {
        //Text.displayTextBox
        Random random = new Random();
        int bound = 6;
        int randomfirework = random.nextInt(bound);
        if (randomfirework == 0) {
            spawnFireworks(p.getLocation(), 2, Color.RED);
        }
        if (randomfirework == 1) {
            spawnFireworks(p.getLocation(), 2, Color.LIME);
        }
        if (randomfirework == 2) {
            spawnFireworks(p.getLocation(), 2, Color.BLUE);
        }
        if (randomfirework == 3) {
            spawnFireworks(p.getLocation(), 2, Color.AQUA);
        }
        if (randomfirework == 4) {
            spawnFireworks(p.getLocation(), 2, Color.ORANGE);
        }
        if (randomfirework == 5) {
            spawnFireworks(p.getLocation(), 2, Color.YELLOW);
        }


    }
    public static void spawnFireworks(Location location, int amount, Color color){
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }
    public static void particlesface(Player p) throws IOException {
//        Location l  = p.getLocation().clone();
//        URL ur1 = new URL("https://cravatar.eu/helmavatar/"+p.getName() + "/64.png");
//
//        BufferedImage face = ImageIO.read(ur1); // read an image from a source, e.g. a file
//
//        ImageParticles particles=new ImageParticles(face,1);
//        particles.setAnchor(32, 32);
//        particles.setDisplayRatio(0.1);
//
//        new BukkitRunnable() {
//            int time = 0;
//            @Override
//            public void run() {
//                l.setYaw(time);
//                Map<Location, Color> particle = particles.getParticles(l.clone().add(0,10,0));
//
//                for(Location spot:particle.keySet()) {
//                    Color color=particle.get(spot);
//                    //spawn particle at location "spot" with color "color"
//                    Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
//                    p.getWorld().spawnParticle(Particle.REDSTONE,spot,1,dustOptions);
//                }
//                if (time > 120) {
//                    time = 0;
//                    cancel();
//                }
//
//                time += 3;
//            }
//        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 4L);
//



    }
    public static void dragon(Player p) {
        if (p.getLocation().getWorld() != null) {
            EnderDragon dragon = p.getLocation().getWorld().spawn(p.getLocation().clone().add(0, 5, 0), EnderDragon.class);
            dragon.damage(dragon.getHealth());
            dragon.setHealth(0);
        }

        //entities.getLocation().setDirection(player.getLocation().subtract(entities.getLocation()).toVector());

    }

    public static void glowItem(ItemStack item) {
        ItemMeta itemStackMeta = item.getItemMeta();
        itemStackMeta.addEnchant(Enchantment.LURE, 0, true);
        itemStackMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        item.setItemMeta(itemStackMeta);
    }
//    static void particles2(final Location location, final String text, final Particle particle, final List<Color> colors, final int count, final float yaw) {
//        final char[] chars = text.toCharArray();
//        final MapFont.CharacterSprite[] sprites = new MapFont.CharacterSprite[chars.length];
//        for (int i = 0; i < chars.length; ++i) {
//            sprites[i] = MinecraftFont.Font.getChar(chars[i]);
//            if (sprites[i] == null) {
//                sprites[i] = MinecraftFont.Font.getChar('?');
//            }
//        }
//        final double width = MinecraftFont.Font.getWidth(text) * 0.5;
//        final double height = MinecraftFont.Font.getHeight() * 0.5;
//        location.setYaw(yaw);
//        location.setPitch(0.0f);
//        final Vector direction = location.getDirection();
//        location.subtract(direction.normalize().multiply(width * 0.5));
//        location.add(0.0, height, 0.0);
//        direction.normalize();
//        direction.multiply(0.5);
//        final List<ParticleData> particles = new ArrayList<ParticleData>();
//        int colorSel = 0;
//
//        for (final MapFont.CharacterSprite sprite : sprites) {
//            for (int col = 0; col < sprite.getWidth(); ++col) {
//                for (int row = 0; row < sprite.getHeight(); ++row) {
//                    if (sprite.get(row, col)) {
//                        if (particle == Particle.REDSTONE) {
//                            final Color color = colors.get(colorSel % colors.size());
//                            particles.add(new ColoredParticleData(location.getX(), location.getY() - row * 0.5, location.getZ(), color));
//                        }
//                        else {
//                            particles.add(new ParticleData(particle, location.getX(), location.getY() - row * 0.5, location.getZ()));
//                        }
//                        ++colorSel;
//                    }
//                }
//                location.add(direction);
//            }
//            location.add(direction);
//        }
//        for (ParticleData p : particles) {
//            location.getWorld().spawnParticle(p.getParticle(), location, 1);
//        }
//    }
    public static void particles(Player p) {
        double Xval = 2;
        double Zval = 1.2;
        Location yourLoc = p.getLocation();
        // Location playerLoc = p.getLocation();
        new BukkitRunnable() {
            double t = 0;
            // Player player = (Player) e.getDamager();
            // Location loc = player.getLocation();

            // Location start = player.getLocation();
            // Vector dir = player.getLocation().getDirection().normalize();

            public void run() {
                t = t + 0.2;
                Location playerLoc = new Location(p.getWorld(), yourLoc.getX() + Xval, yourLoc.getY() + Zval,
                        yourLoc.getZ());
                Vector v = new Vector(playerLoc.getX() - yourLoc.getX(), playerLoc.getY() - yourLoc.getY(),
                        playerLoc.getZ() - yourLoc.getZ()).normalize();
                //    Location second = yourLoc.clone();
                // multiply
                v.multiply(-1);
                double x = v.getX() * t;
                double y = v.getY() * t ;
                double z = v.getZ() * t;
                playerLoc.add(x, y, z); // add
                playerLoc.getWorld().spawnParticle(Particle.FLAME, playerLoc, 1);
                //ParticleEffect.FLAME.display(0, 0, 0, 0, 100, playerLoc, 20);
                if (playerLoc.getWorld() == yourLoc.getWorld()) {
                    playerLoc.subtract(x, y, z); // subtract
                    if (playerLoc.distance(yourLoc) <= 2) {
                        System.out.println("test");
                    }
                    if (t == 1.5) {
                        this.cancel();


                    }
                }

            }

        }.runTaskTimer(HardcoreGames.getInstance(), 0, 1);


    }

    public static BufferedImage overlayImages(BufferedImage bgImage,
                                              BufferedImage fgImage) {

        /**
         * Doing some preliminary validations.
         * Foreground image height cannot be greater than background image height.
         * Foreground image width cannot be greater than background image width.
         *
         * returning a null value if such condition exists.
         */
        if (fgImage.getHeight() > bgImage.getHeight()
                || fgImage.getWidth() > fgImage.getWidth()) {

            return null;
        }

        /**Create a Graphics  from the background image**/
        Graphics2D g = bgImage.createGraphics();
        /**Set Antialias Rendering**/
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        /**
         * Draw background image at location (0,0)
         * You can change the (x,y) value as required
         */
        g.drawImage(bgImage, 0, 0, null);

        /**
         * Draw foreground image at location (0,0)
         * Change (x,y) value as required.
         */
        g.drawImage(fgImage, 0, 0, null);

        g.dispose();
        return bgImage;
    }
}
