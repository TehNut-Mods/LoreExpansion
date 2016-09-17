package me.dmillerw.loreexpansion.json.data;

import com.google.common.base.Objects;

public class Content {

    private final String title;
    private final String body;
    private final String audio;

    public Content(String title, String body, String audio) {
        this.title = title;
        this.body = body;
        this.audio = audio;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getAudio() {
        return audio;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("title", getTitle())
                .add("body", getBody())
                .add("audio", getAudio())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Content)) return false;

        Content content = (Content) o;

        if (getTitle() != null ? !getTitle().equals(content.getTitle()) : content.getTitle() != null) return false;
        if (getBody() != null ? !getBody().equals(content.getBody()) : content.getBody() != null) return false;
        return getAudio() != null ? getAudio().equals(content.getAudio()) : content.getAudio() == null;

    }

    @Override
    public int hashCode() {
        int result = getTitle() != null ? getTitle().hashCode() : 0;
        result = 31 * result + (getBody() != null ? getBody().hashCode() : 0);
        result = 31 * result + (getAudio() != null ? getAudio().hashCode() : 0);
        return result;
    }
}
