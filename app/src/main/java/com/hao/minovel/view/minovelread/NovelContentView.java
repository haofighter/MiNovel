package com.hao.minovel.view.minovelread;

import android.content.Context;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hao.minovel.R;

import java.util.ArrayList;
import java.util.List;

class NovelContentView extends FrameLayout {
    TextView novelTitle;
    NovelTextView novelContent;
    TextView novelPage;
    int maxLine; //当前页容纳的最大行数
    int nowPage;//当前页位置
    int chapterIndex;//章节位于列表的位置

    public NovelContentView(@NonNull Context context) {
        this(context, null);
    }

    public NovelContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NovelContentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setDiraction(int chapterIndex, int nowPage) {
        this.nowPage = nowPage;
        this.chapterIndex = chapterIndex;
    }

    private void init() {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.read_novel_page, null));
        novelTitle = findViewById(R.id.novel_title);
        novelContent = findViewById(R.id.novel_content);
        novelPage = findViewById(R.id.novel_page);
        post(new Runnable() {
            @Override
            public void run() {
                maxLine = (int) (novelContent.getHeight() / (novelContent.getTextSize() + novelContent.getLineSpacingExtra()));
            }
        });
    }


    public TextView getNovelTitle() {
        return novelTitle;
    }

    public NovelTextView getNovelContent() {
        return novelContent;
    }

    public TextView getNovelPage() {
        return novelPage;
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    public void setContent(ChapterInfo chapterInfo) {
        int start = nowPage * maxLine;
        int end = (nowPage + 1) * maxLine;
        if (end > chapterInfo.getTextArray().size()) {
            end = chapterInfo.getTextArray().size();
        }
        List<String> contents = chapterInfo.getTextArray().subList(start, end);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < contents.size(); i++) {
            content.append(contents.get(i));
        }
        novelContent.setText(content);
    }


}
