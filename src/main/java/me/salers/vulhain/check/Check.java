package me.salers.vulhain.check;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import me.salers.vulhain.Vulhain;

import me.salers.vulhain.data.PlayerData;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@Getter
@Setter
public abstract class Check {

    private String name ;
    private String category;
    private String type ;
    private int vl, probabilty, delay;
    private boolean experimental ;
    public double buffer = 0;


    /**
     * Method for doing checks
     *
     * @param playerData the data reliated to the check
     * @param packet    the custom packet based on packetevents
     **/

    public abstract void onPacket(Object packet, PlayerData playerData);

    public Check(String name, String category, String type, boolean experimental) {
        this.name = name;
        this.category = category;
        this.type = type;
        this.experimental = experimental;
    }

    /**
     * Method for sending alerts
     *
     * @param data     the potential cheater's data
     * @param moreInfo more info on the check
     */

    protected void flag(PlayerData data, String moreInfo) {


        /** Exempting the player for prevent falses  **/
        if (data.getBukkitPlayerFromUUID().getAllowFlight() ||
                data.getBukkitPlayerFromUUID().getGameMode() == GameMode.SPECTATOR ||
                data.getBukkitPlayerFromUUID().getGameMode() == GameMode.CREATIVE) return;



        TextComponent toSendExp = new TextComponent(ChatColor.translateAlternateColorCodes('&', Vulhain.getInstance().getConfig()
                .getString("vulhain.alert-message").replaceAll("%player%", data.getBukkitPlayerFromUUID().getName()).
                        replaceAll("%check%", this.name).replaceAll("%type%", String.valueOf(this.type)).
                        replaceAll("%exp%", "&7(Experimental)").replaceAll("%vl%",
                        String.valueOf(vl)).replaceAll("%probabilty%", String.valueOf(probabilty))));

        TextComponent toSend = new TextComponent(ChatColor.translateAlternateColorCodes('&', Vulhain.getInstance().getConfig()
                .getString("vulhain.alert-message").replaceAll("%player%", data.getBukkitPlayerFromUUID().getName()).
                        replaceAll("%check%", this.name).replaceAll("%type%", String.valueOf(this.type)).
                        replaceAll("%exp%", "").replaceAll("%vl%",
                        String.valueOf(vl)).replaceAll("%probabilty%", String.valueOf(probabilty))));


        toSend.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.
                translateAlternateColorCodes('&', "&8[&c&lVulhain&8]\n" + "&cInfo:&7 " + moreInfo
                        + "\n&cExperimental:&7 " + experimental + "\n&cProbability: &7" + probabilty +
                        "\n\n&cClick to teleport")).create()));
        toSendExp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.
                translateAlternateColorCodes('&', "&8[&c&lVulhain&8]\n" + "&cInfo:&7 " + moreInfo
                        + "\n&cExperimental:&7 " + experimental + "\n&cProbability: &7" + probabilty +
                        "\n\n&cClick to teleport")).create()));
        toSend.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "tp " + data.getBukkitPlayerFromUUID().getName()));
        toSendExp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + data.getBukkitPlayerFromUUID().getName()));




        if (this.probabilty > 5) {
            probabilty = 5;
        }
        if (this.probabilty == 0) {
            this.probabilty = 1;
        }
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            if (onlinePlayers.hasPermission("vulhain.alerts")) {
                if (++delay > 5) {
                    vl++;
                    if (this.experimental) {
                        onlinePlayers.spigot().sendMessage(toSendExp);
                    } else onlinePlayers.spigot().sendMessage(toSend);
                    if (this.vl >= getMaxVL()) {
                        this.punish(data);

                    }


                    delay = 0;

                }

            }
        }


    }



    /**
     * @param toVerbose the thing to broadcast
     */
    protected void verbose(Object toVerbose) {
        Bukkit.broadcastMessage("§c§lVERBOSE: §f" + toVerbose);

    }

    /**
     * @param data the player to punish
     */
    protected void punish(PlayerData data) {
        if(!isPunish()) return;
            String toDispatch = ChatColor.translateAlternateColorCodes('&', Vulhain.getInstance().
                    getConfig().getString("vulhain.punish-command").replaceAll("%player%", data.getBukkitPlayerFromUUID().getName()).
                    replaceAll("%check%", this.name).replaceAll("%type%", String.valueOf(this.type)).
                    replaceAll("%exp%", "").replaceAll("%vl%",
                    String.valueOf(vl)).replaceAll("%probabilty%", String.valueOf(probabilty)));
            Bukkit.getScheduler().runTask(Vulhain.getInstance(),() -> Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),toDispatch));
            data.getBukkitPlayerFromUUID().getWorld().strikeLightningEffect(data.getBukkitPlayerFromUUID().getLocation());
            this.vl = this.probabilty = this.delay = 0;



    }

    public double getMaxBuffer() {
        return Vulhain.getInstance().getConfig().getInt("checks." + category +"." + name.toLowerCase() +"."
                + type.toLowerCase() + ".buffer");
    }

    public boolean isEnabled() {
        return Vulhain.getInstance().getConfig().getBoolean("checks." + category +"." + name.toLowerCase() +"." +
                type.toLowerCase() + ".enabled");
    }

    public boolean isPunish() {
        return Vulhain.getInstance().getConfig().getBoolean("checks." + category +"." + name.toLowerCase() +"." +
                type + ".punish");
    }

    public double getMaxVL() {
        return Vulhain.getInstance().getConfig().getInt("checks." + category +"." + name.toLowerCase() +"."
                + type + ".max-vl");
    }
}
