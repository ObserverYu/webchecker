package com.ruiyun.jvppeteer.protocol.input;

import java.util.List;

public class BoxModel {


    private List<Integer> content;

    private List<Integer> padding;

    private List<Integer> border;

    private List<Integer> margin;

    private int width;

    private int height;

    public BoxModel(List<Integer> content, List<Integer> padding, List<Integer> border, List<Integer> margin, int width, int height) {
        this.content = content;
        this.padding = padding;
        this.border = border;
        this.margin = margin;
        this.width = width;
        this.height = height;
    }

    public BoxModel() {
    }

    public List<Integer> getContent() {
        return content;
    }

    public void setContent(List<Integer> content) {
        this.content = content;
    }

    public List<Integer> getPadding() {
        return padding;
    }

    public void setPadding(List<Integer> padding) {
        this.padding = padding;
    }

    public List<Integer> getBorder() {
        return border;
    }

    public void setBorder(List<Integer> border) {
        this.border = border;
    }

    public List<Integer> getMargin() {
        return margin;
    }

    public void setMargin(List<Integer> margin) {
        this.margin = margin;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
