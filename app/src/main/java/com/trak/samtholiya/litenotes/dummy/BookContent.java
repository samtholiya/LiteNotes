package com.trak.samtholiya.litenotes.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class BookContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position),makeContent(position));
    }

    private static List<String> makeContent(int position){
        List<String> builder = new ArrayList<>();
        builder.add("Details about Item: "+String.valueOf(position));
        for (int i = 0; i < position; i++) {
            builder.add("\nMore details information here.");
        }
        return builder;
    }
    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String title;
        public final String details;
        public final List<String> contentList;
        public DummyItem(String id, String title, String details) {
            this.id = id;
            this.title = title;
            this.details = details;
            this.contentList=new ArrayList<>();
        }
        public DummyItem(String id,String title,String details,List<String> contentList){
            this.id = id;
            this.title = title;
            this.details = details;
            this.contentList=contentList;
        }


        @Override
        public String toString() {
            return title;
        }
    }
}
