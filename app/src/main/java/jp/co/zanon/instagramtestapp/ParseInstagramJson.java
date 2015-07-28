package jp.co.zanon.instagramtestapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by shintaro1 on 15/07/23.
 */
public class ParseInstagramJson {

    private final String TAG = getClass().getSimpleName();
    private InstagramList mList;

    public ParseInstagramJson(InstagramList list) {
        this.mList = list;
    }

    public void loadJson(String str) {
        JsonNode root = getJsonNode(str);
        if (root != null) {

            // nextUrl を更新
            this.mList.setNextUrl(root.path("pagination").path("next_url").asText());

            // １つ１つの情報を取得
            Iterator<JsonNode> ite = root.path("data").elements();
            while (ite.hasNext()) {
                JsonNode node = ite.next();

                // リストに追加
                mList.add(node);
            }
        }
    }

    private JsonNode getJsonNode(String str) {
        try {
            return new ObjectMapper().readTree(str);
        } catch (IOException e) {
            LogUtil.d(TAG, e.getMessage());
        }
        return null;
    }
}
