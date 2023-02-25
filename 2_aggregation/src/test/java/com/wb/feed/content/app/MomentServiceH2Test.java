package com.wb.feed.content.app;

import com.wb.feed.aggregation.app.MomentService;
import com.wb.feed.aggregation.app.model.EnterAppInfo;
import com.wb.feed.aggregation.domains.moment.MomentDomainService;
import com.wb.feed.aggregation.domains.moment.MomentPatcher;
import com.wb.feed.aggregation.domains.moment.logic.MomentDataLogic;
import com.wb.feed.aggregation.infra.UserFriendFacade;
import com.wb.idb.manager.config.ConfigManager;
import com.wb.idb.model.DbConfigWrapper;
import com.wb.idb.model.SourceConfig;
import com.wb.idb.model.TableConfig;
import com.wb.idb.model.TableSourceConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * 朋友圈单元测试
 * @author yanghuan
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.*", "com.sun.*", "javax.xml.*","org.apache.*"})
@PrepareForTest({ConfigManager.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = TestApplication.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@ActiveProfiles("test")
public class MomentServiceH2Test {

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

    private static volatile long ONE_HOUR = TimeUnit.HOURS.toMillis(1);

    @Before
    public void setup() {
        // mock bean 解析注入
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQueryMomentFeeds() {
        mockDB();
        doReturn(true).when(userFriendFacade).isFriend("219672201", "36050769");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("219672201");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("36050769");

        List<Long> feeds = momentService.queryMomentFeeds("219672201", 0L);

        Assert.assertNotNull(feeds);
    }

    @Test
    public void testPublishFeed() {
        mockDB();
        doReturn(true).when(userFriendFacade).isFriend("219672201", "36050769");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("219672201");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("36050769");

        try {
            Thread.sleep(ONE_HOUR);
        }
        catch (Exception e) {
        }

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
        mockDB();
        doReturn(true).when(userFriendFacade).isFriend("219672201", "36050769");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("219672201");
        doReturn(Arrays.asList("219672201", "36050769", "10000001")).when(userFriendFacade).getUserFriends("36050769");

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
        mockDB();
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
        mockDB();
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
        mockDB();
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

    private void mockDB() {
        {
            List<TableSourceConfig> tableSourceConfigList = new ArrayList<>();

            TableConfig tableConfig = new TableConfig();
            tableConfig.setSourceName("main");
            tableConfig.setCatlogName("feedrecv");
            tableConfig.setTableName("feed_recv");

            SourceConfig sourceConfig = new SourceConfig();
            sourceConfig.setSourceName("main");
            sourceConfig.setDbUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MySQL");
            sourceConfig.setUsername("root");
            sourceConfig.setPassword("root");
            sourceConfig.setExtra("characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai");

            TableSourceConfig tableSourceConfig = new TableSourceConfig(tableConfig, Collections.singletonList(sourceConfig));
            tableSourceConfigList.add(tableSourceConfig);

            DbConfigWrapper dbConfigWrapper = new DbConfigWrapper(tableSourceConfigList);

            PowerMockito.mockStatic(ConfigManager.class);
            when(ConfigManager.getAndCreate("", "feedrecv", "feed_recv")).thenReturn(dbConfigWrapper);

            DbConfigWrapper test = ConfigManager.getAndCreate("", "feedrecv", "feed_recv");
        }
    }
}