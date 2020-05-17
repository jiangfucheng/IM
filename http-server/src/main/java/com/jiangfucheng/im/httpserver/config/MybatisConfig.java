package com.jiangfucheng.im.httpserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 22:47
 *
 * @author jiangfucheng
 */
@Configuration
@PropertySource("classpath:mysql/mysql-${spring.profiles.active}.yml")
public class MybatisConfig {

}
