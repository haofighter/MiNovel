package com.hao.minovel.view.minovelread;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hao.minovel.R;
import com.hao.minovel.log.MiLog;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.utils.TypeFaceUtils;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

class NovelContentView extends FrameLayout {
    private TextView novelTitle;
    private NovelTextView novelContent;
    private TextView novelPage;
    private View v;
    private int maxLine; //当前页容纳的最大行数
    private int nowPage;//当前页位置
    private int chapterIndex;//章节位于列表的位置
    private NovelTextDrawInfo novelTextDrawInfo;//小说内容界面的配置信息

    public NovelContentView(NovelTextDrawInfo novelTextDrawInfo, @NonNull Context context) {
        this(context);
        this.novelTextDrawInfo = novelTextDrawInfo;
        init();
        initContentConfig();
        if (this.novelTextDrawInfo == null) {
            throw new NullPointerException("novelTextDrawInfo为空，需要配置一个不为空的对象");
        }
    }

    private NovelContentView(@NonNull Context context) {
        this(context, null);
    }

    private NovelContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private NovelContentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    public void setChapterIndex(int chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    private void init() {
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.read_novel_page, null);
            addView(v);
        }
        novelTitle = v.findViewById(R.id.novel_title);
        novelContent = v.findViewById(R.id.novel_content);
        novelPage = v.findViewById(R.id.novel_page);
        int paddingTop = v.getPaddingTop();
        if (paddingTop < SystemUtil.getStatusBarHeight(getContext())) {
            paddingTop = v.getPaddingTop() + SystemUtil.getStatusBarHeight(getContext());
        }
        v.setPadding(v.getPaddingLeft(), paddingTop, v.getPaddingRight(), v.getPaddingBottom());
//        initContentConfig();
        post(new Runnable() {
            @Override
            public void run() {
                maxLine = novelContent.getHeight() / novelContent.getLineHeight();
                MiLog.i("字体高度：" + novelContent.getLineHeight() + "     " + novelContent.getHeight() + "    行数： " + maxLine);
                novelTextDrawInfo.setMaxLine(maxLine);
                int useless = novelContent.getHeight() % novelContent.getLineHeight();
                novelTitle.setPadding(novelTitle.getPaddingLeft(), novelTitle.getPaddingTop(), novelTitle.getPaddingRight(), novelTitle.getPaddingBottom() + useless / 2);
            }
        });
    }


    private void initContentConfig() {
        maxLine = novelTextDrawInfo.getMaxLine();
        if (novelTextDrawInfo.getTextSize() == 0) {
            novelTextDrawInfo.setTextSize(SystemUtil.px2sp(getContext(), novelContent.getTextSize()));
        } else {
            novelContent.setTextSize(novelTextDrawInfo.getTextSize());
        }

        if (TextUtils.isEmpty(novelTextDrawInfo.getTypeFaceName())) {
            novelTextDrawInfo.setTypeFaceName("默认字体");
        }
        novelContent.setTypeface(TypeFaceUtils.getTypeFaceByName(novelTextDrawInfo.getTypeFaceName()));
        novelTitle.setTypeface(TypeFaceUtils.getTypeFaceByName(novelTextDrawInfo.getTypeFaceName()));
        novelPage.setTypeface(TypeFaceUtils.getTypeFaceByName(novelTextDrawInfo.getTypeFaceName()));
    }


    public NovelTextView getNovelContent() {
        return novelContent;
    }

    public void setContent(ChapterInfo chapterInfo) {
        if (novelTextDrawInfo.getMaxLine() != 0) {
            maxLine = novelTextDrawInfo.getMaxLine();
        }
        int start = nowPage * maxLine;
        if (start < 0 || start > chapterInfo.getTextArray().size()) {
            nowPage = 0;
            start = 0;
        }
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
        novelPage.setText(nowPage + 1 + "/" + chapterInfo.getPage());
        novelTitle.setText(chapterInfo.getChapterName());
    }

    public int getNowPage() {
        return nowPage;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public int getMaxLine() {
        return maxLine;
    }


}
