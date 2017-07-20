package club.ranleng.psnine.event;

public class EmojiEvent {

    private String emoji;
    public EmojiEvent(String name){
        this.emoji = name;
    }

    public String getEmoji(){
        return emoji;
    }

}
