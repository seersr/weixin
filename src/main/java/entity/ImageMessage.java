package entity;

public class ImageMessage extends BaseMessage{
    private ImageItem Image;

    public ImageItem getImage() {
        return Image;
    }

    public void setImage(ImageItem image) {
        Image = image;
    }
}
