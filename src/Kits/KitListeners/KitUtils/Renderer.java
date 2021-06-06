package Kits.KitListeners.KitUtils;

import Kits.KitListeners.Kits.Utility.Hacker;
import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.map.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Renderer extends MapRenderer {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
       // mapCanvas.setPixel(56, 72, MapPalette.RED);
//        for (int x = 25; x < 50; x++) {
//            for (int y = 25; y < 50; y++) {
//                mapCanvas.setPixel(x, y, MapPalette.RED);
//            }
//        }
        //mapCanvas.drawText(15, 15, MinecraftFont.Font, player.getName());
        if (kitInfo.getPlayerKit(player) == Kits.RADAR) {
            mapView.setUnlimitedTracking(false);

            if (!Hacker.getCurrentObfuscated().contains(player)) {
                MapCursorCollection cursors = new MapCursorCollection();
                for (Entity entity : player.getNearbyEntities(200, 200, 200)) {
                    int playerX = player.getLocation().getBlockX();
                    int playerZ = player.getLocation().getBlockZ();

                    int entityX = entity.getLocation().getBlockX();
                    int entityZ = entity.getLocation().getBlockZ();

                    int mapX = 0;
                    int mapY = 0;

                    int difX = Math.abs(entityX - playerX);
                    int difZ = Math.abs(entityZ - playerZ);

                    if (entityX > playerX) {
                        mapX = mapX + difX;
                    } else {
                        mapX = mapX - difX;
                    }

                    if (entityZ > playerZ) {
                        mapY = mapY + difZ;
                    } else {
                        mapY = mapY - difZ;
                    }

                    if (entity.getType() == EntityType.PLAYER) {
                        if (!((Player) entity).isInvisible() || !((Player) entity).isSneaking()) {
                            MapCursor cursor = new MapCursor((byte) mapX, (byte) mapY, (byte) 12, MapCursor.Type.WHITE_CROSS, true);
                            cursor.setCaption(entity.getName());
                            cursors.addCursor(cursor);
                        }
                    }
                }


                //cursors.addCursor(60, 70, (byte) 12);
                mapView.setCenterX(player.getLocation().getBlockX());
                mapView.setCenterZ(player.getLocation().getBlockZ());
                mapCanvas.setCursors(cursors);

            }

        }
    }
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
