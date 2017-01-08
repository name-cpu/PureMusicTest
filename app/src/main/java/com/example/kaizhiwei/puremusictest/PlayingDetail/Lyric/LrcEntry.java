package com.example.kaizhiwei.puremusictest.PlayingDetail.Lyric;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hzwangchenyan on 2016/10/19.
 */
class LrcEntry implements Comparable<LrcEntry> {
    private long time;
    private String text;
    private StaticLayout staticLayout;
    private TextPaint paint;

    private LrcEntry(long time, String text) {
        this.time = time;
        this.text = text;
    }

    void init(TextPaint paint, int width) {
        this.paint = paint;
        staticLayout = new StaticLayout(text, paint, width, Layout.Alignment.ALIGN_CENTER, 1f, 0f, false);
    }

    long getTime() {
        return time;
    }

    StaticLayout getStaticLayout() {
        return staticLayout;
    }

    float getTextHeight() {
        if (paint == null || staticLayout == null) {
            return 0;
        }
        return staticLayout.getLineCount() * paint.getTextSize();
    }

    @Override
    public int compareTo(LrcEntry entry) {
        if (entry == null) {
            return -1;
        }
        return (int) (time - entry.getTime());
    }

    static List<LrcEntry> parseLrc(File lrcFile) {
        if (lrcFile == null || !lrcFile.exists()) {
            return null;
        }

        List<LrcEntry> entryList = new ArrayList<LrcEntry>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(lrcFile), "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                List<LrcEntry> list = parseLine(line);
                if (list != null && !list.isEmpty()) {
                    entryList.addAll(list);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(entryList);
        return entryList;
    }

    static List<LrcEntry> parseLrc(String lrcText) {
        if (TextUtils.isEmpty(lrcText)) {
            return null;
        }

        List<LrcEntry> entryList = new ArrayList<LrcEntry>();
        lrcText = lrcText.replace("\\n", "");
        List<String> listArray = new ArrayList<>();
        int startIndex = lrcText.indexOf("[", 0);
        int endIndex = 0;
        String strLyric = "";
        while (startIndex >= 0){
            endIndex = lrcText.indexOf("]", startIndex);
            if(endIndex < 0)
                break;

            if(endIndex+1 >= lrcText.length())
                break;
            String nextChar = lrcText.substring(endIndex+1, endIndex +2);
            if(nextChar.equalsIgnoreCase("[")){
                strLyric = lrcText.substring(startIndex, endIndex+1);
                listArray.add(strLyric);
            }
            else{
                endIndex = lrcText.indexOf("[", endIndex+1);
                if(endIndex >= 0){
                    strLyric = lrcText.substring(startIndex, endIndex);
                    listArray.add(lrcText.substring(startIndex, endIndex));
                }
            }
            Log.i("weikaizhi", strLyric + "#startIndex:" + startIndex +",#endIndex:" +endIndex);
            startIndex = endIndex+1;
        }

        for (int i = 0;i < listArray.size();i++) {
            List<LrcEntry> list = parseLine(listArray.get(i));
            if (list != null && !list.isEmpty()) {
                entryList.addAll(list);
            }
        }

        Collections.sort(entryList);
        return entryList;
    }

    private static List<LrcEntry> parseLine(String line) {
        if (TextUtils.isEmpty(line)) {
            return null;
        }

        line = line.trim();
        Matcher lineMatcher = Pattern.compile("((\\[\\d\\d:\\d\\d\\.\\d\\d\\])+)(.+)").matcher(line);
        if (!lineMatcher.matches()) {
            return null;
        }

        String times = lineMatcher.group(1);
        String text = lineMatcher.group(3);
        List<LrcEntry> entryList = new ArrayList<LrcEntry>();

        Matcher timeMatcher = Pattern.compile("\\[(\\d\\d):(\\d\\d)\\.(\\d\\d)\\]").matcher(times);
        while (timeMatcher.find()) {
            long min = Long.parseLong(timeMatcher.group(1));
            long sec = Long.parseLong(timeMatcher.group(2));
            long mil = Long.parseLong(timeMatcher.group(3));
            long time = min * DateUtils.MINUTE_IN_MILLIS + sec * DateUtils.SECOND_IN_MILLIS + mil * 10;
            entryList.add(new LrcEntry(time, text));
        }
        return entryList;
    }
}
