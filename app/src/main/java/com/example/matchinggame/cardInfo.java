package com.example.matchinggame;

public class cardInfo {
    private int imageViewId;
    private int availableId;
    private boolean isFlipped = false;
    private boolean isMatched = false;

    public cardInfo(int imageViewId, int availableId) {
        this.imageViewId = imageViewId;
        this.availableId = availableId;
    }

    public int getImageViewId() {
        return imageViewId;
    }

    public void setImageViewId(int imageViewId) {
        this.imageViewId = imageViewId;
    }

    public int getAvailableId() {
        return availableId;
    }

    public void setAvailableId(int availableId) {
        this.availableId = availableId;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    @Override
    public String toString() {
        return "cardInfo{" +
                "imageViewId=" + imageViewId +
                ", availableId=" + availableId +
                ", isFlipped=" + isFlipped +
                ", isMatched=" + isMatched +
                '}';
    }
}
