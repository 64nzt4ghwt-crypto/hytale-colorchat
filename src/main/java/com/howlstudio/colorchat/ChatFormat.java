package com.howlstudio.colorchat;
public class ChatFormat {
    private final String rank, prefix, nameColor, messageColor;
    public ChatFormat(String rank,String prefix,String nameColor,String messageColor){
        this.rank=rank;this.prefix=prefix;this.nameColor=nameColor;this.messageColor=messageColor;
    }
    public String getRank(){return rank;} public String getPrefix(){return prefix;}
    public String getNameColor(){return nameColor;} public String getMessageColor(){return messageColor;}
    public String format(String player,String message){return prefix+"§r "+nameColor+player+"§7: "+messageColor+message;}
    public String toConfig(){return rank+"|"+prefix+"|"+nameColor+"|"+messageColor;}
    public static ChatFormat fromConfig(String s){String[]p=s.split("\\|",4);return p.length>=4?new ChatFormat(p[0],p[1],p[2],p[3]):null;}
}
