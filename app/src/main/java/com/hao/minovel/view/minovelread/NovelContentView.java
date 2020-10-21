package com.hao.minovel.view.minovelread;

import android.content.Context;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hao.minovel.R;
import com.hao.minovel.db.DBManage;
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
    NovelTextViewHelp novelTextViewHelp;

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
        novelTextViewHelp=  DBManage.chackNovelConfig();
        View v=  LayoutInflater.from(getContext()).inflate(R.layout.read_novel_page, null);
        novelTitle = v.findViewById(R.id.novel_title);
        novelContent =v. findViewById(R.id.novel_content);
        novelPage =v. findViewById(R.id.novel_page);
        if(novelTextViewHelp!=null) {
            novelContent.setPadding(novelContent.getPaddingLeft(), (int) (novelContent.getPaddingTop() + novelTextViewHelp.getTextPadingtop()), novelContent.getPaddingRight(), novelContent.getPaddingBottom());
        }else{
            maxLine=novelTextViewHelp.getLineNum();
        }
        addView(v);
        post(new Runnable() {
            @Override
                public void run() {
                if(novelTextViewHelp==null){
                    novelTextViewHelp=new NovelTextViewHelp();
                }
                maxLine = novelContent.getHeight() / novelContent.getLineHeight();
                novelTextViewHelp.setLineNum(maxLine);
                int useless=novelContent.getHeight() % novelContent.getLineHeight();
                novelTextViewHelp.setTextPadingtop(useless/2);
            }
        });
    }

    public NovelTextView getNovelContent() {
        return novelContent;
    }

    public void setContent(ChapterInfo chapterInfo) {
        int start = nowPage * maxLine;
        if(start>chapterInfo.getTextArray().size()){
            nowPage=0;
            start=0;
        }
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

    public int getNowPage() {
        return nowPage;
    }


    public int getChapterIndex() {
        return chapterIndex;
    }

    public NovelTextViewHelp getNovelTextViewHelp() {
        return novelTextViewHelp;
    }
}
