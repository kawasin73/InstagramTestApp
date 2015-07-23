package jp.co.zanon.instagramtestapp;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by shintaro1 on 15/07/23.
 */
public class InstagramItem {
    public String thumbnail;
    public String standard;

    public void setJsonNode(JsonNode node) {
        this.thumbnail = node.path("images").path("thumbnail").path("url").asText();
        this.standard = node.path("images").path("standard_resolution").path("url").asText();
    }
}
