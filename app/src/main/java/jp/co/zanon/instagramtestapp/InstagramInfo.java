package jp.co.zanon.instagramtestapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by shintaro1 on 15/07/23.
 */
public class InstagramInfo {

    private final String TAG = getClass().getSimpleName();
    String nextUrl = null;
    private ArrayList<InstagramItem> itemList = new ArrayList<InstagramItem>();

    public InstagramInfo(String nextUrl){
        this.nextUrl = nextUrl;
    }

    public String getNextUrl(){
        return this.nextUrl;
    }

    public ArrayList<InstagramItem> getList(){
        return itemList;
    }

    public void clear() {
        this.itemList.clear();
    }

    public void loadJson(String str) {
        JsonNode root = getJsonNode(str);
        if (root != null) {

            // nextUrl を更新
            this.nextUrl = root.path("pagination").path("next_url").asText();

            // １つ１つの情報を取得
            Iterator<JsonNode> ite = root.path("data").elements();
            while (ite.hasNext()) {
                JsonNode node = ite.next();
                InstagramItem item = new InstagramItem();
                item.setJsonNode(node);

                // リストに追加
                itemList.add(item);

            }
        }
    }

    private JsonNode getJsonNode(String str) {
        try {
            return new ObjectMapper().readTree(str);
        } catch (IOException e) {
            LogUtil.d(getClass().getName(), e.getMessage());
        }
        return null;
    }
}
