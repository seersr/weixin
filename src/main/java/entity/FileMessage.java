package entity;

public class FileMessage {
    private String type;
    private String media_id; //图片
    private String created_at;

    private String thumb_media_id; //缩略图

    public String getThumbmedia_id() {
        return thumb_media_id;
    }

    public void setThumbmedia_id(String thumbmedia_id) {
        this.thumb_media_id = thumbmedia_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
