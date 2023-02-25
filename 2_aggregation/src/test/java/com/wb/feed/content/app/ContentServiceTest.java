package com.wb.feed.content.app;

import com.wb.common.WbJSON;
import com.wb.feed.common.FeedScene;
import com.wb.feed.common.model.VersionParam;
import com.wb.feed.content.domains.feed.FeedDomainService;
import com.wb.feed.rpc.client.param.content.*;
import com.wb.feed.rpc.client.result.source.FeedPrivilegeEnum;
import com.wb.feed.rpc.client.result.source.FeedSourceModel;
import com.wb.feed.rpc.client.result.source.FeedSourceTypeEnum;
import com.wb.feed.rpc.client.result.source.FeedSystemPrivilegeEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author liudongyue
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年05月17日 15:09:00
 */
@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class ContentServiceTest {

    /*****************************************************/
    /*****************************************************/
    /***注：删掉FeedSourceMapper的WbMapper注解的shardColumn***/
    /*****************************************************/
    /*****************************************************/

    @Autowired
    private ContentService contentService;

    @Test
    public void createFeed() {
        TextFeedCreateBO textFeedCreateBO = new TextFeedCreateBO();
        textFeedCreateBO.setTextContent("社区重构-文本feed创建测试");
        textFeedCreateBO.setUid("60321443");
        textFeedCreateBO.setPrivilege(1);
        textFeedCreateBO.setSystemPrivilege(FeedSystemPrivilegeEnum.UGC_SELF_VIEW);
        contentService.createFeed(textFeedCreateBO);

        try {
            // 有子线程执行，主线程先不能结束
            Thread.sleep(1000000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createImgFeed() {
        ImageFeedCreateBO imageFeedCreateBO = new ImageFeedCreateBO();
        imageFeedCreateBO.setImageUrl("https://qiniustatic.wodidashi.com/head/xh5C1lpjKziX5");
        imageFeedCreateBO.setUid("60321443");
        imageFeedCreateBO.setTextContent("社区重构-图片创建测试");
        imageFeedCreateBO.setPrivilege(1);
        imageFeedCreateBO.setSystemPrivilege(FeedSystemPrivilegeEnum.DEF);
        List<AtInfoBO> atInfos = new ArrayList<>();
        AtInfoBO atInfoBO = new AtInfoBO();
        atInfoBO.setUid("31251171");
        atInfoBO.setUserName("10435");
        atInfos.add(atInfoBO);
        imageFeedCreateBO.setAtInfos(atInfos);
        List<String> createTags = new ArrayList<>();
        createTags.add("你好呀");
        imageFeedCreateBO.setCreateTags(createTags);
        List<Long> tagIds = new ArrayList<>();
        tagIds.add(2526L);
        imageFeedCreateBO.setTagIds(tagIds);

        contentService.createFeed(imageFeedCreateBO);

        try {
            Thread.sleep(1000000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createMultiImgFeed() {
        MultiImageFeedCreateBO multiImageFeedCreateBO= new MultiImageFeedCreateBO();
        List<String> imglist = new ArrayList<>();
        imglist.add("https://qiniustatic.wodidashi.com/picture/gJHDs4lX0cZj6");
        imglist.add("https://qiniustatic.wodidashi.com/head/xh5C1lpjKziX5");
        multiImageFeedCreateBO.setImageUrlList(imglist);
        multiImageFeedCreateBO.setUid("60321443");
        multiImageFeedCreateBO.setTextContent("社区重构-多图创建测试");
        List<AtInfoBO> atInfos = new ArrayList<>();
        AtInfoBO atInfoBO = new AtInfoBO();
        atInfoBO.setUid("31251171");
        atInfoBO.setUserName("10435");
        atInfos.add(atInfoBO);
        multiImageFeedCreateBO.setAtInfos(atInfos);
        multiImageFeedCreateBO.setPrivilege(1);
        multiImageFeedCreateBO.setSystemPrivilege(FeedSystemPrivilegeEnum.DEF);

        contentService.createFeed(multiImageFeedCreateBO);

        try {
            Thread.sleep(1000000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createLinkFeed() {
        LinkFeedCreateBO linkFeed = new LinkFeedCreateBO();
        linkFeed.setLinkTitle("令人笑出猪叫的画猜接龙");
        linkFeed.setLinkDesc("快来参与话题讨论吧");
        linkFeed.setLinkIcon("https://qiniustatic.wodidashi.com/tag_index.png");
        linkFeed.setUrl("");
        LinkOptInfoBO linkOptInfoBO = new LinkOptInfoBO();
        linkOptInfoBO.setUnifyJump("wanba://webview/inner?_info=eyJ1cmwiOiJodHRwczovL3QxLnpodWh1aXlhby5jbi93ZWIvZnJvbnQtYWN0aXZpdHkvYWN0aXZpdHkvMjAwNzAyLWdyYXNzLWJvYXQvaW5kZXguaHRtbD9hcHBpZD0xMDAwMiJ9");
        linkFeed.setLinkOptInfo(linkOptInfoBO);
        linkFeed.setUid("57180700");
        linkFeed.setSourceType(FeedSourceTypeEnum.LINK);
        linkFeed.setPrivilege(FeedPrivilegeEnum.SQUARE_VIEW.getCode());
        linkFeed.setTagIds(Arrays.asList(2525L));
        linkFeed.setTextContent("链接feed测试");
        VersionParam versionParam = new VersionParam();
        versionParam.setAppId("1001");
        versionParam.setAppVersion("101702");
        linkFeed.setVersionParam(versionParam);
        AtInfoBO atInfoBO = new AtInfoBO();
        atInfoBO.setUid("98392943");
        atInfoBO.setUserName("12345");
        linkFeed.setAtInfos(Arrays.asList(atInfoBO));

        contentService.createFeed(linkFeed);

        try {
            Thread.sleep(1000000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createPaintFeed() {
        PaintFeedCreateBO paintFeed = new PaintFeedCreateBO();
        paintFeed.setPaintAuthorUid("81336779");
        paintFeed.setAuthorUserName("回首掏");
        paintFeed.setAuthorIconImg("https://qiniustatic.wodidashi.com/head/35asNEZCbeHOf?imageView/2/w/200/h/200\\");
        paintFeed.setHint("行为");
        paintFeed.setAnswerId(6020);
        paintFeed.setAnswer("听歌");
        paintFeed.setImgUrl("https://qiniustatic.wodidashi.com/picture/QFsKU1eOEaw4z");
        paintFeed.setStrokeUrl("https://qiniustatic.wodidashi.com/d6ca20b7-32f4-4e8c-87d1-c11f69d4fac3");
        paintFeed.setTimeCost(30177);
        paintFeed.setUid("81336779");
        paintFeed.setRoundId("414253066");
        paintFeed.setSourceType(FeedSourceTypeEnum.PAINT_PLAY_NEW);

        AtInfoBO atInfoBO = new AtInfoBO();
        atInfoBO.setUid("98392943");
        atInfoBO.setUserName("12345");
        paintFeed.setAtInfos(Arrays.asList(atInfoBO));
        paintFeed.setTagIds(Arrays.asList(2351L));
        paintFeed.setTextContent("画猜feed测试");
        VersionParam versionParam = new VersionParam();
        versionParam.setAppId("1001");
        versionParam.setAppVersion("101702");
        paintFeed.setVersionParam(versionParam);
        contentService.createFeed(paintFeed);

        try {
            Thread.sleep(1000000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createForwardFeed() {
        ForwardFeedCreateBO forwardFeed = new ForwardFeedCreateBO();
        forwardFeed.setForwardFeedId(133581L);
        forwardFeed.setUid("57180700");
        forwardFeed.setTagIds(Collections.singletonList(2525L));
        VersionParam versionParam = new VersionParam();
        versionParam.setAppId("1001");
        versionParam.setAppVersion("101702");
        forwardFeed.setVersionParam(versionParam);
        AtInfoBO atInfoBO = new AtInfoBO();
        atInfoBO.setUid("98392943");
        atInfoBO.setUserName("12345");
        forwardFeed.setAtInfos(Collections.singletonList(atInfoBO));
        contentService.createFeed(forwardFeed);

        try {
            Thread.sleep(1000000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createAudioFeed() {
        System.out.println("**********createAudioFeed**********");
        AudioFeedCreateBO audioFeedCreateBO = new AudioFeedCreateBO();
        audioFeedCreateBO.setAudioLength(7);
        audioFeedCreateBO.setAudioPropId("173000004");
        audioFeedCreateBO.setAudioSize(0);
        audioFeedCreateBO.setAudioUrl("https://qiniustatic.wodidashi.com/audio/E0xp6jINM9srO");
        audioFeedCreateBO.setUid("31251171");
        audioFeedCreateBO.setTextContent("社区重构-音频创建测试");
        List<AtInfoBO> atInfos = new ArrayList<>();
        AtInfoBO atInfoBO = new AtInfoBO();
        atInfoBO.setUid("99376742");
        atInfoBO.setUserName("10432");
        atInfos.add(atInfoBO);
        audioFeedCreateBO.setAtInfos(atInfos);
        audioFeedCreateBO.setCardId(702L);
        List<Long> tagIds = new ArrayList<>();
        tagIds.add(2525L);
        audioFeedCreateBO.setTagIds(tagIds);
        List<String> createTags = new ArrayList<>();
        createTags.add("你若安好便是晴天");
        audioFeedCreateBO.setCreateTags(createTags);
        audioFeedCreateBO.setPrivilege(0);
        LabelInfoBO labelInfoBO = new LabelInfoBO();
        labelInfoBO.setLabelId("32");
        labelInfoBO.setLabelName("小清新");
        audioFeedCreateBO.setLabelInfoBO(labelInfoBO);
        contentService.createFeed(audioFeedCreateBO);

        try {
            Thread.sleep(1000000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createVideoFeed() {
        System.out.println("**********createVideoFeed**********");
        VideoFeedCreateBO videoFeedCreateBO = new VideoFeedCreateBO();
        videoFeedCreateBO.setCoverUrl("https://qiniustatic.wodidashi.com/h5/front-backstage/robotManage/1622517960414_31.mp4?vframe/jpg/offset/0");
        videoFeedCreateBO.setVideoUrl("https://qiniustatic.wodidashi.com/h5/front-backstage/robotManage/1622517960414_31.mp4");
        videoFeedCreateBO.setVideoHeight(544);
        videoFeedCreateBO.setVideoLength(8);
        videoFeedCreateBO.setVideoWidth(960);
        videoFeedCreateBO.setUid("31251171");
        videoFeedCreateBO.setTextContent("社区重构-视频创建测试");
        List<AtInfoBO> atInfos = new ArrayList<>();
        AtInfoBO atInfoBO = new AtInfoBO();
        atInfoBO.setUid("99376742");
        atInfoBO.setUserName("10432");
        atInfos.add(atInfoBO);
        videoFeedCreateBO.setAtInfos(atInfos);
        videoFeedCreateBO.setCardId(702L);
        List<Long> tagIds = new ArrayList<>();
        tagIds.add(2525L);
        videoFeedCreateBO.setTagIds(tagIds);
        List<String> createTags = new ArrayList<>();
        createTags.add("你若安好便是晴天");
        videoFeedCreateBO.setCreateTags(createTags);
        videoFeedCreateBO.setPrivilege(0);
        LabelInfoBO labelInfoBO = new LabelInfoBO();
        labelInfoBO.setLabelId("32");
        labelInfoBO.setLabelName("小清新");
        videoFeedCreateBO.setLabelInfoBO(labelInfoBO);
        contentService.createFeed(videoFeedCreateBO);

        try {
            Thread.sleep(1000000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************/
    /*****************************************************/
    /***********             查询               ***********/
    /*****************************************************/
    /*****************************************************/

    @Test
    public void testQueryFeedById() {
        FeedSourceModel feed = contentService.getFeed(135035L);
        Assert.assertNotNull(feed);
        System.out.println(WbJSON.toJson(feed));
    }

    @Test
    public void testPatchQueryFeedByIds() {
        List<FeedSourceModel> feedList = contentService.batchGetFeeds(Arrays.asList(135035L, 135014L, 134973L, 134955L));
        Assert.assertNotNull(feedList);
        System.out.println(WbJSON.toJson(feedList));
    }

    @Autowired
    FeedDomainService feedService;

    @Test
    public void testCreate() {
        TextFeedCreateBO feedCreateBO = new TextFeedCreateBO();
        feedCreateBO.setTextContent("feed生命周期");
        feedCreateBO.setTagIds(new ArrayList<>());
        feedCreateBO.setSourceId(40058L);
        feedCreateBO.setSourceType(FeedSourceTypeEnum.STATUS);
        feedCreateBO.setUid("688618586");

        SceneInfoBO sceneInfoBO = new SceneInfoBO();
        sceneInfoBO.setScene(FeedScene.NONE);
        sceneInfoBO.setSceneParam(null);
        feedCreateBO.setSceneInfoBO(sceneInfoBO);

        feedCreateBO.setPrivilege(1);

        feedService.createFeed(feedCreateBO);
    }
}