package entity;

public class MusicMessage extends BaseMessage{
    private MusicItem Music;

    public MusicItem getMusic() {
        return Music;
    }

    public void setMusic(MusicItem music) {
        Music = music;
    }
}
