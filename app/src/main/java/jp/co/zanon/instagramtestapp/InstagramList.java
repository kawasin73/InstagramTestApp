package jp.co.zanon.instagramtestapp;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by shintaro1 on 15/07/24.
 */
public class InstagramList {

    // InstagramItemのリスト
    private List<InstagramItem> list = new LinkedList<InstagramItem>();

    // 次の画像を取得するためのURL
    private String nextUrl = null;

    public InstagramList(String nextUrl) {
        this.nextUrl = nextUrl;
    }
    public List<InstagramItem> getList() {
        return this.list;
    }
    public String getNextUrl() {
        return this.nextUrl;
    }
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    // 保持しているリストのクリア
    public void clear() {
        this.list.clear();
    }

    // 保持しているリストに画像追加（URL）を追加する
    public void add(JsonNode node) {
        InstagramItem item = new InstagramItem();
        item.setJsonNode(node);
        this.list.add(item);
    }
}
