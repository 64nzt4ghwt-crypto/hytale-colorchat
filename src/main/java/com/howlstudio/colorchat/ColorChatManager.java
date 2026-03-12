package com.howlstudio.colorchat;
import com.hypixel.hytale.component.Ref; import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.nio.file.*; import java.util.*;
public class ColorChatManager {
    private final Path dataDir;
    private final Map<String,ChatFormat> formats=new LinkedHashMap<>();
    public ColorChatManager(Path d){this.dataDir=d;try{Files.createDirectories(d);}catch(Exception e){}loadDefaults();load();}
    private void loadDefaults(){
        formats.put("owner",new ChatFormat("owner","§4[Owner]","§c","§f"));
        formats.put("admin",new ChatFormat("admin","§c[Admin]","§6","§f"));
        formats.put("mod",new ChatFormat("mod","§2[Mod]","§a","§f"));
        formats.put("vip",new ChatFormat("vip","§6[VIP]","§e","§f"));
        formats.put("default",new ChatFormat("default","","§7","§f"));
    }
    public int getRankCount(){return formats.size();}
    public ChatFormat getFormat(String rank){return formats.getOrDefault(rank.toLowerCase(),formats.get("default"));}
    public String formatMessage(String rank,String player,String message){return getFormat(rank).format(player,message);}
    public void save(){try{StringBuilder sb=new StringBuilder();for(ChatFormat f:formats.values())sb.append(f.toConfig()).append("\n");Files.writeString(dataDir.resolve("formats.txt"),sb.toString());}catch(Exception e){}}
    private void load(){try{Path f=dataDir.resolve("formats.txt");if(!Files.exists(f))return;formats.clear();loadDefaults();for(String l:Files.readAllLines(f)){ChatFormat cf=ChatFormat.fromConfig(l);if(cf!=null)formats.put(cf.getRank(),cf);}}catch(Exception e){}}
    public AbstractPlayerCommand getColorChatCommand(){
        return new AbstractPlayerCommand("chatformat","[Admin] Manage chat formats. /chatformat list|set <rank> <prefix> <nameColor> <msgColor>|preview <rank>"){
            @Override protected void execute(CommandContext ctx,Store<EntityStore> store,Ref<EntityStore> ref,PlayerRef playerRef,World world){
                String[]args=ctx.getInputString().trim().split("\\s+",5);
                String sub=args.length>0?args[0].toLowerCase():"list";
                switch(sub){
                    case"list"->{playerRef.sendMessage(Message.raw("=== Chat Formats ==="));for(ChatFormat f:formats.values())playerRef.sendMessage(Message.raw("  §6"+f.getRank()+"§r: "+f.format("Player","Hello world!")));}
                    case"preview"->{if(args.length<2)break;playerRef.sendMessage(Message.raw(getFormat(args[1]).format(playerRef.getUsername(),"Preview message")));}
                    case"set"->{if(args.length<5){playerRef.sendMessage(Message.raw("Usage: /chatformat set <rank> <prefix> <nameColor> <msgColor>"));break;}ChatFormat cf=new ChatFormat(args[1],args[2],args[3],args[4]);formats.put(args[1].toLowerCase(),cf);save();playerRef.sendMessage(Message.raw("[ColorChat] Format set for: "+args[1]));}
                    default->playerRef.sendMessage(Message.raw("Usage: /chatformat list | preview <rank> | set <rank> <prefix> <nameColor> <msgColor>"));
                }
            }
        };
    }
}
