package com.howlstudio.colorchat;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
/** ColorChat — Rank-based colored chat. Players with permissions use color codes in chat. Admin configurable. */
public final class ColorChatPlugin extends JavaPlugin {
    private ColorChatManager mgr;
    public ColorChatPlugin(JavaPluginInit init){super(init);}
    @Override protected void setup(){
        System.out.println("[ColorChat] Loading...");
        mgr=new ColorChatManager(getDataDirectory());
        CommandManager.get().register(mgr.getColorChatCommand());
        System.out.println("[ColorChat] Ready. "+mgr.getRankCount()+" rank formats.");
    }
    @Override protected void shutdown(){if(mgr!=null)mgr.save();System.out.println("[ColorChat] Stopped.");}
}
