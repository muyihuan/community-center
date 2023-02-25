package com.wb.feed.content.app;

import com.wb.feed.aggregation.app.MomentService;
import com.wb.feed.aggregation.app.model.EnterAppInfo;
import com.wb.feed.aggregation.domains.moment.MomentDomainService;
import com.wb.feed.aggregation.domains.moment.MomentPatcher;
import com.wb.feed.aggregation.domains.moment.logic.MomentDataLogic;
import com.wb.feed.aggregation.infra.UserFriendFacade;
import com.wb.feed.content.domains.feed.FeedDomainService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.doReturn;

/**
 * 朋友圈单元测试
 * @author yanghuan
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class MomentServiceDEVTest {

    @Autowired
    @InjectMocks
    private MomentService momentService;

    @Autowired
    @InjectMocks
    private MomentDomainService momentDomainService;

    @Autowired
    @InjectMocks
    private MomentDataLogic momentDataLogic;

    @Autowired
    @InjectMocks
    private MomentPatcher momentPatcher;

    @Mock
    private UserFriendFacade userFriendFacade;

    @Autowired
    private FeedDomainService feedService;

    private static volatile long ONE_HOUR = TimeUnit.HOURS.toMillis(1);

    @Before
    public void setup() {
        // mock bean 解析注入
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQueryMomentFeeds() {
        doReturn(true).when(userFriendFacade).isFriend("219672201", "36050769");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("219672201");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("36050769");

        List<Long> feeds = momentService.queryMomentFeeds("219672201", 0L);

        Assert.assertNotNull(feeds);

        List<Long> secondPageFeeds = momentService.queryMomentFeeds("219672201", 127718L);

        Assert.assertNotNull(secondPageFeeds);
    }

    @Test
    public void testPublishFeed() {
        doReturn(true).when(userFriendFacade).isFriend("219672201", "36050769");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("219672201");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("36050769");

        feedService.openFeed(135113L);

        momentService.publishFeed(135113L);

        List<Long> feeds = momentService.queryMomentFeeds("219672201", 0L);
        Assert.assertNotNull(feeds);
        Assert.assertTrue(feeds.contains(135113L));

        try {
            Thread.sleep(ONE_HOUR);
        }
        catch (Exception e) {
        }
    }

    @Test
    public void testDeleteFeed() {
        doReturn(true).when(userFriendFacade).isFriend("219672201", "36050769");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("219672201");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("36050769");

        feedService.hideFeed(135113L);

        momentService.deleteFeed("219672201", 135113L);

        List<Long> feeds = momentService.queryMomentFeeds("219672201", 0L);
        Assert.assertNotNull(feeds);
        Assert.assertTrue(!feeds.contains(135113L));

        try {
            Thread.sleep(ONE_HOUR);
        }
        catch (Exception e) {
        }
    }

    @Test
    public void testDissolveFriend() {
        doReturn(false).when(userFriendFacade).isFriend("219672201", "36050769");
        doReturn(Arrays.asList("219672201", "10000001")).when(userFriendFacade).getUserFriends("219672201");
        doReturn(Arrays.asList("36050769", "10000001")).when(userFriendFacade).getUserFriends("36050769");

        momentService.dissolveFriend("219672201", "36050769");

        try {
            Thread.sleep(ONE_HOUR);
        }
        catch (Exception e) {
        }
    }

    @Test
    public void testEnterApp() {
        doReturn(true).when(userFriendFacade).isFriend("219672201", "36050769");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("219672201");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("36050769");

        EnterAppInfo enterAppInfo = new EnterAppInfo();
        enterAppInfo.setUid("219672201");
        enterAppInfo.setLastLoginTime(0L);

        momentService.enterApp(enterAppInfo);

        try {
            Thread.sleep(ONE_HOUR);
        }
        catch (Exception e) {
        }
    }

    @Test
    public void testDataConsistency() {
        doReturn(true).when(userFriendFacade).isFriend("219672201", "36050769");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("219672201");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("36050769");

        new Thread(() -> {
            while(true) {
                momentService.publishFeed(135113L);

                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) {
                }
            }
        }).start();

        new Thread(() -> {
            while(true) {
                momentService.deleteFeed("219672201", 135113L);

                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) {
                }
            }
        }).start();

        new Thread(() -> {
            while(true) {
                momentService.dissolveFriend("219672201", "36050769");

                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) {
                }
            }
        }).start();

        try {
            Thread.sleep(3000);
        }
        catch (Exception e) {
        }

        // 最终一致 非好友
        doReturn(false).when(userFriendFacade).isFriend("219672201", "36050769");

        try {
            Thread.sleep(ONE_HOUR);
        }
        catch (Exception e) {
        }
    }

    @Test
    public void testAll() {
        ONE_HOUR = 30;

        testQueryMomentFeeds();
        testPublishFeed();
        testDeleteFeed();
        testDissolveFriend();
        testEnterApp();
        testDataConsistency();
    }
}
