package entity;

import java.util.List;

public class NewsMessage extends BaseMessage{
    private int ArticleCount; //图文消息个数；当用户发送文本、图片、视频、图文、地理位置这五种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
    private List<NewsItem> Articles; //图文消息信息，注意，如果图文数超过限制，则将只发限制内的条数

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }

    public List<NewsItem> getArticles() {
        return Articles;
    }

    public void setArticles(List<NewsItem> articles) {
        Articles = articles;
    }
}
