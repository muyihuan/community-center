package com.wb.feed.content.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 单元测试启动类测试
 * @author yanghuan
 */
@SpringBootApplication(scanBasePackages = {"com.wb"})
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
