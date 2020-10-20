package com.hao.minovel.moudle.activity;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hao.annotationengine.Router;
import com.hao.annotetion.annotation.Bind;
import com.hao.date.RouterContent;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseActivity;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.entity.ReadInfo;
import com.hao.minovel.moudle.service.LoadHtmlService;
import com.hao.minovel.moudle.service.LoadWebInfo;
import com.hao.minovel.spider.SpiderUtils;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.spider.data.NovelIntroduction;
import com.hao.minovel.tinker.app.AppContext;

import java.util.List;

@Bind
public class NovelDetailActivity extends MiBaseActivity implements View.OnClickListener {
    NovelIntroduction novelDetail;
    TextView startRead;
    LinearLayout novelNowReadLl;
    TextView novelNowRead;
    TextView novelTitle;
    TextView novelAuthor;
    TextView novelType;
    TextView novelIntroduce;
    TextView novelLastChaper;
    ImageView novelDetailImage;
    TextView moreOrPart;
    SwipeRefreshLayout refreshLayout;
    ReadInfo readInfo;

    LoadWebInfo loadWebInfo = new LoadWebInfo();


    @Override
    protected int layoutId() {
        return R.layout.activity_novel_detail;
    }

    @Override
    protected void initView(View v) {
        startRead = v.findViewById(R.id.start_read);
        refreshLayout = v.findViewById(R.id.refresh_layout);
        novelNowReadLl = v.findViewById(R.id.novel_now_read_ll);
        novelNowRead = v.findViewById(R.id.novel_now_read);
        novelTitle = v.findViewById(R.id.novel_title);
        novelAuthor = v.findViewById(R.id.novel_author);
        novelType = v.findViewById(R.id.novel_type);
        novelIntroduce = v.findViewById(R.id.novel_introduce);
        novelLastChaper = v.findViewById(R.id.novel_last_chaper);
        novelDetailImage = v.findViewById(R.id.novel_detail_image);
        moreOrPart = v.findViewById(R.id.more_or_part);
        moreOrPart.setOnClickListener(this);
        v.findViewById(R.id.start_read).setOnClickListener(this);
    }

    @Override
    protected void doElse() {
        initTranslation();
        novelDetail = getIntent().getParcelableExtra("novelDetail");
        readInfo = DBManage.checkedReadInfo(novelDetail.getNovelChapterListUrl());
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshLayout.setRefreshing(true);
        if (readInfo != null) {
            startRead.setText("继续阅读");
        } else {
            startRead.setText("开始阅读");
        }
        if (novelDetail.getNovelChapterListUrl() != null && !"".equals(novelDetail.getNovelChapterListUrl())) {
            initDate();
        } else {
            Toast.makeText(this, "暂未获取到小说数据", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initDate(NovelIntroduction novelIntroduction) {
        refreshLayout.setRefreshing(false);
        if (novelIntroduction != null) {
            ReadInfo readInfo = DBManage.checkedReadInfo(novelIntroduction.getNovelChapterListUrl());
            if (readInfo != null) {
                NovelChapter novelChapter = DBManage.checkNovelChaptterByUrl(readInfo.getNovelChapterUrl());
                novelNowReadLl.setVisibility(View.VISIBLE);
                novelNowRead.setText(novelChapter.getChapterName());
                novelNowRead.setOnClickListener(this);
            }
            novelTitle.setText(novelIntroduction.getNovelName());
            novelAuthor.setText(novelIntroduction.getNovelAutho());
            novelType.setText(novelIntroduction.getNovelType());
            novelIntroduce.setText("\t\t" + novelIntroduction.getNovelIntroduce());
            if (novelIntroduce.getLineCount() > 3) {
                novelIntroduce.setMaxLines(3);
                moreOrPart.setText("更多");
            } else {
                moreOrPart.setVisibility(View.INVISIBLE);
            }
            novelLastChaper.setText(novelIntroduction.getNovelNewChapterTitle());
            Glide.with(AppContext.application).load(novelIntroduction.getNovelCover()).error(R.mipmap.image_novel_normal_cover).into(novelDetailImage);
        }
    }

    /**
     * 加载本页数据
     */
    private void initDate() {
        NovelIntroduction novelIntroduction = DBManage.checkNovelByUrl(novelDetail.getNovelChapterListUrl());
        List<NovelChapter> novelChapters = DBManage.getChapterById(novelDetail.getNovelChapterListUrl());
        if (novelIntroduction != null && novelIntroduction.isComplete() && novelChapters != null && novelChapters.size() > 0) {
            initDate(novelIntroduction);
        } else {
            Intent intent = new Intent(this, LoadHtmlService.class);
            loadWebInfo.setTask(LoadHtmlService.novelDetail);
            intent.putExtra(LoadHtmlService.TASK, loadWebInfo);
            intent.putExtra(LoadHtmlService.DATE, novelIntroduction);
            startService(intent);
        }
    }

    @Override
    protected void doOnSetContent(View v) {

    }

    @Override
    public void eventBusOnEvent(LoadWebInfo str) {
        switch (str.getLoadStatus()) {
            case SpiderUtils.Success:
                refreshLayout.setRefreshing(false);
                initDate();
                break;
            case SpiderUtils.LOADING:
                refreshLayout.setRefreshing(true);
                break;
            default:
                refreshLayout.setRefreshing(false);
                Toast.makeText(this, "加载失败", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.novel_now_read:
//                Router.getInstance().build(RouterContent.READNOVELACTIVITY).skip();
                break;
            case R.id.start_read:
                Bundle bundle = new Bundle();
                if (((TextView) v).getText().toString().equals("继续阅读")) {
                    NovelChapter novelChapter = DBManage.checkNovelChaptterById(readInfo.getNovelChapterListUrl(), readInfo.getNovelChapterUrl());
                    bundle.putParcelable("novelChapter", novelChapter);
                    Router.getInstance().build(RouterContent.READNOVELACTIVITY, bundle).skip();
                } else if (((TextView) v).getText().toString().equals("开始阅读")) {
                    List<NovelChapter> novelChapters = DBManage.getChapterById(novelDetail.getNovelChapterListUrl());
                    if (novelChapters.size() > 0) {
                        bundle.putParcelable("novelChapter", novelChapters.get(0));
                        Router.getInstance().build(RouterContent.READNOVELACTIVITY, bundle).skip();
                    } else {
                        initDate();
                    }
                }
                break;
            case R.id.more_or_part:
                if (novelIntroduce.getMaxLines() != 3) {
                    moreOrPart.setText("更多");
                    novelIntroduce.setMaxLines(3);
                } else {
                    moreOrPart.setText("收起");
                    novelIntroduce.setMaxLines(100);
                }
                break;
        }
    }

    //转场动画
    private void initTranslation() {
        ImageView book_icon = findViewById(R.id.novel_detail_image);

        /**
         * 1、设置相同的TransitionName
         */
//        ViewCompat.setTransitionName(book_icon, "avatar");
        /**
         * 2、设置WindowTransition,除指定的ShareElement外，其它所有View都会执行这个Transition动画
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade().setDuration(3000));
            getWindow().setExitTransition(new Fade().setDuration(3000));
            /**
             * 3、设置ShareElementTransition,指定的ShareElement会执行这个Transiton动画
             */
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.addTransition(new ChangeBounds());
            transitionSet.addTransition(new ChangeTransform());
            transitionSet.addTarget(book_icon);
            getWindow().setSharedElementEnterTransition(transitionSet);
            getWindow().setSharedElementExitTransition(transitionSet);
        }
    }
}
