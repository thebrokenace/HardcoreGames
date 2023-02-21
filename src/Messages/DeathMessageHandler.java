package Messages;

import Kits.KitTools.KitInfo;
import Main.PlayerStats;
import Util.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMessageHandler implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    Messages messages = new Messages();
    @EventHandler
    public void onDeath (EntityDeathEvent e) {
//        if (e.getEntity().hasMetadata("NPC")) {
//            return;
//        }
        if (e.getEntity() instanceof  Player) {

            if (e.getEntity().getLastDamageCause() != null) {
                if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();

                    messages.sendToAll(messages.deathFormattedMessage(entityEvent.getDamager(), (Player) e.getEntity()));

                    Bukkit.broadcastMessage(messages.peopleLeftMessage());


                } else {
                    messages.sendToAll(messages.changeDefault(e.getEntity().getLastDamageCause().getCause(), e.getEntity()));
                    Bukkit.broadcastMessage(messages.peopleLeftMessage());


                    //e.setDeathMessage();
                    //Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> Bukkit.broadcastMessage(messages.peopleLeftMessage()), 1);
                }
            }
        }
        }

        @EventHandler
        public void playerOnPlayer (PlayerDeathEvent e) {
            if (e.getEntity().getKiller() != null) {

                Player p = (Player) e.getEntity().getKiller();

                    PlayerStats.getStats().addKill(p);

            }

        }

        @EventHandler
        public void onDeathEvent (PlayerDeathEvent e) {
            e.setDeathMessage(null);
            if (game.isStarted()) {
                if (e.getEntity().getLocation().getWorld() != null)
                e.getEntity().getLocation().getWorld().strikeLightningEffect(e.getEntity().getLocation());
            }

        }

//        @EventHandler
//        public void onArrowDamage (EntityDamageByEntityEvent e) {
//         if (e.getEntity() instanceof Player) {
//             if (e.getDamager() instanceof Arrow) {
//                 if (((Arrow) e.getDamager()).getShooter() instanceof Player) {
//                     Player shooter =(Player) ((Arrow) e.getDamager()).getShooter();
//                     double damage = e.getDamage();
//                     e.setDamage(0);
//                     ((Player) e.getEntity()).damage(damage, shooter);
//                 }
//             }
//         }
//        }
}
