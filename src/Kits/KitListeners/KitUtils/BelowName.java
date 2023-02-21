package Kits.KitListeners.KitUtils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import net.minecraft.server.v1_16_R3.ChatBaseComponent;
import net.minecraft.server.v1_16_R3.IScoreboardCriteria;
import net.minecraft.server.v1_16_R3.Scoreboard;
import net.minecraft.server.v1_16_R3.ScoreboardObjective;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class BelowName {

    PacketContainer packet;

    public BelowName(Player player) {
        this.packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE);



        String name = UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        Scoreboard scoreboard = new Scoreboard();

        ScoreboardObjective objective = scoreboard.registerObjective("health", IScoreboardCriteria.HEALTH, ChatBaseComponent.ChatSerializer.a("{" +
                "  \"text\": \"hijbjsa\"" +
                "}"), IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);

        scoreboard.setDisplaySlot(2, objective);

        this.packet.getIntegers().write(0, 2);
        //set position

        this.packet.getStrings().write(0, player.getName());




        //this.packet.getSpecificModifier(Collection.class).write(0, Collections.singletonList(player.getName()));

        //this.packet.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&',"&f" + player.toString())));
        //this.packet.getSpecificModifier(Collection.class).write(0, Collections.singletonList(player.getName()));
    }
//
//    public BelowName setPrefix(String prefix) {
//        this.packet.getChatComponents().write(1, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', prefix + "&f") + " "));
//        return this;
//    }
//
//    public BelowName setSuffix(String suffix) {
//        this.packet.getChatComponents().write(2, WrappedChatComponent.fromText(" " + ChatColor.translateAlternateColorCodes('&', suffix)));
//        return this;
//    }



    public void build(Player p) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot send packet " + packet, e);
        }

    }
}