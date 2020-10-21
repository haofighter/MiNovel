package com.hao.minovel.view.minovelread;

import android.content.Context;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hao.minovel.R;
import com.hao.minovel.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

class NovelContentView extends FrameLayout  implements Observer {
 private    TextView novelTitle;
    private  NovelTextView novelContent;
    private  TextView novelPage;
    private  int maxLine; //当前页容纳的最大行数
    private  int nowPage;//当前页位置
    private  int chapterIndex;//章节位于列表的位置

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
                float scaledDensity =getContext().getResources().getDisplayMetrics().scaledDensity;
                Log.i("小说","总高度:"+getHeight()+"高度："+novelContent.getHeight()+"            行高： "+novelContent.getLineHeight()+"     字体大小：" +novelContent.getTextSize()+"        "+scaledDensity*novelContent.getTextSize()+"    行间距："+novelContent.getLineSpacingExtra());
                Log.i("小说","总高度:"+getHeight()+"novelTitle高度："+novelTitle.getHeight()+"     novelPage：" +novelPage.getHeight());
                maxLine = novelContent.getHeight() / novelContent.getLineHeight();
                int useless=novelContent.getHeight() % novelContent.getLineHeight();
                Log.i("小说","多余的:"+useless);
                novelContent.setPadding(novelContent.getPaddingLeft(),novelContent.getPaddingTop()+useless/2,novelContent.getPaddingRight(),novelContent.getPaddingBottom());
                novelContent.invalidate();
            }
        });
    }

    public NovelTextView getNovelContent() {
        return novelContent;
    }

    public void setContent(ChapterInfo chapterInfo) {
        int start = nowPage * maxLine;
        int end = (nowPage + 1) * maxLine;
        if (end > chapterInfo.getTextArray().size()) {
            end = chapterInfo.getTextArray().size();
        }
        List<String> contents = chapterInfo.getTextArray().subList(start, end);
        Log.i("小说","开始行:"+start+"     结束行：" +end);

        StringBuilder content = new StringBuilder();
        for (int i = 0; i < contents.size(); i++) {
            content.append(contents.get(i));
        }
        novelContent.setText(content);
        novelPage.setText(nowPage+1+"/"+chapterInfo.getPage());
        novelTitle.setText(chapterInfo.getChapterName());
    }


    @Override
    public void update(Observable o, Object arg) {

    }
}
