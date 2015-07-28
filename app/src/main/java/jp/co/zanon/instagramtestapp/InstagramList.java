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

    private String firstUrl = null; // 最初のURL
    private String nextUrl = null;  // 次の画像を取得するためのURL

    public InstagramList(String nextUrl) {
        setFirseUrl(nextUrl);
    }

    public List<InstagramItem> getList() {
        return this.list;
    }

    public String getNextUrl() {
        return this.nextUrl;
    }

    //次のURLを設定
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    // リストをクリアして、最初のURLを設定
    public void setFirseUrl(String nextUrl) {
        this.firstUrl = nextUrl;
        this.nextUrl = nextUrl;
        clear();
    }

    // リストをクリアして、最初のURLを再設定
    public void refresh() {
        setFirseUrl(this.firstUrl);
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
