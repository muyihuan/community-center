package com.wb.feed.content.app;

import com.wb.feed.aggregation.app.ProfileService;
import com.wb.feed.aggregation.infra.FeedContentFacade;
import com.wb.feed.aggregation.infra.UserFriendFacade;
import com.wb.feed.rpc.client.result.source.FeedSourceModel;
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

import java.util.List;

import static org.mockito.Mockito.doReturn;

/**
 * 个人动态单元测试
 * @author yanghuan
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class ProfileServiceTest {

    @Autowired
    @InjectMocks
    private ProfileService profileService;

    @Mock
    private UserFriendFacade userFriendFacade;

    @Autowired
    private FeedContentFacade contentFacade;

    @Before
    public void setup() {
        // mock bean 解析注入
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQueryProfileFeedsA() {
        doReturn(true).when(userFriendFacade).isFriend("219672201", "36050769");

        List<Long> feeds = profileService.queryProfileFeeds("219672201", "36050769", 0L, 40);

        Assert.assertNotNull(feeds);
    }

    @Test
    public void testQueryProfileFeedsB() {
        doReturn(false).when(userFriendFacade).isFriend("219672201", "36050769");

        List<Long> feeds = profileService.queryProfileFeeds("219672201", "36050769", 0L, 40);

        Assert.assertNotNull(feeds);
    }

    @Test
    public void testQueryProfileFeedsC() {
        List<Long> feeds = profileService.queryProfileFeeds("219672201", "219672201", 0L, 40);

        Assert.assertNotNull(feeds);
    }

    @Test
    public void testPublishFeed() {
        profileService.publishFeed(135113L);

        FeedSourceModel feed = contentFacade.getFeed(135113L);
        Assert.assertNotNull(feed);

        List<Long> feeds = profileService.queryProfileFeeds(feed.getUid(), feed.getUid(), 0L, 40);
        Assert.assertTrue(feeds.contains(135113L));
    }

    @Test
    public void testDeleteFeedFeed() {
        profileService.deleteFeed("688618586", 135113L);

        FeedSourceModel feed = contentFacade.getFeed(135113L);
        List<Long> feeds = profileService.queryProfileFeeds(feed.getUid(), feed.getUid(), 0L, 40);
        Assert.assertFalse(feeds.contains(135113L));
    }
}