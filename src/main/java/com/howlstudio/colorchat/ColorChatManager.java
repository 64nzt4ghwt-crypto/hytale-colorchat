package com.howlstudio.colorchat;
import com.hypixel.hytale.component.Ref; import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.nio.file.*; import java.util.*;
public class ColorChatManager {
    private final Path dataDir;
    // uuid → chosen color code
    private final Map<UUID,String> playerColors=new HashMap<>();
    // rank → allowed colors
    private final Map<String,List<String>> rankColors=new LinkedHashMap<>();
    public ColorChatManager(Path d){
        this.dataDir=d;try{Files.createDirectories(d);}catch(Exception e){}
        // Defaults
        rankColors.put("vip",List.of("§a","§b","§d","§e","§6"));
        rankColors.put("mvp",List.of("§a","§b","§c","§d","§e","§6","§5","§2"));
        rankColors.put("admin",List.of("§0","§1","§2","§3","§4","§5","§6","§7","§8","§9","§a","§b","§c","§d","§e","§f"));
        load();
    }
    public String getPlayerColor(UUID uid){return playerColors.getOrDefault(uid,"§f");}
    public void setColor(UUID uid,String code){playerColors.put(uid,code);save();}
    public void save(){try{StringBuilder sb=new StringBuilder();for(var e:playerColors.entrySet())sb.append(e.getKey()+"|"+e.getValue()).append("\n");Files.writeString(dataDir.resolve("colors.txt"),sb.toString());}catch(Exception e){}}
    private void load(){try{Path f=dataDir.resolve("colors.txt");if(!Files.exists(f))return;for(String l:Files.readAllLines(f)){String[]p=l.split("\\|",2);if(p.length==2)playerColors.put(UUID.fromString(p[0]),p[1]);};}catch(Exception e){}}
    private static final Map<String,String> COLOR_NAMES=Map.of("red","§c","green","§a","blue","§9","yellow","§e","gold","§6","pink","§d","aqua","§b","white","§f","gray","§7","purple","§5");
    public AbstractPlayerCommand getColorChatCommand(){
        return new AbstractPlayerCommand("colorchat","Set your chat color. /colorchat <color|list>"){
            @Override protected void execute(CommandContext ctx,Store<EntityStore> store,Ref<EntityStore> ref,PlayerRef playerRef,World world){
                String arg=ctx.getInputString().trim().toLowerCase();
                if(arg.isEmpty()||arg.equals("list")){
                    playerRef.sendMessage(Message.raw("[ColorChat] Available colors: red, green, blue, yellow, gold, pink, aqua, white, gray, purple"));
                    playerRef.sendMessage(Message.raw("[ColorChat] Current: "+getPlayerColor(playerRef.getUuid())+"■§r /colorchat <name>"));
                    return;
                }
                String code=COLOR_NAMES.get(arg);
                if(code==null){playerRef.sendMessage(Message.raw("[ColorChat] Unknown color. Use /colorchat list"));return;}
                setColor(playerRef.getUuid(),code);
                playerRef.sendMessage(Message.raw("[ColorChat] Chat color set to "+code+arg+"§r!"));
            }
        };
    }
}
