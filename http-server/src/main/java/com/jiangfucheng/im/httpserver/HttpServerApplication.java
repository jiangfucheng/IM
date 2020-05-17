package com.jiangfucheng.im.httpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/4
 * Time: 14:22
 *
 * @author jiangfucheng
 */
@SpringBootApplication
//@MapperScan("com.jiangfucheng.im.httpserver.mapper")
public class HttpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpServerApplication.class, args);
	}

}
