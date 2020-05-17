package com.jiangfucheng.im.httpserver.config;

import com.jiangfucheng.im.httpserver.shiro.IMRealm;
import com.jiangfucheng.im.httpserver.shiro.JwtFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/16
 * Time: 15:19
 *
 * @author jiangfucheng
 */
//@Configuration
public class ShiroConfig {

	@Bean(name = "imRealm")
	public Realm IMRealm(){
		return new IMRealm();
	}

	@Bean(name = "imSecurityManager")
	public WebSecurityManager securityManager(@Qualifier("imRealm") Realm imRealm) {
		WebSecurityManager securityManager = new DefaultWebSecurityManager();
		return securityManager;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("imSecurityManager") SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setFilters(Collections.singletonMap("jwt", new JwtFilter()));
		shiroFilterFactoryBean.setFilterChainDefinitionMap(Collections.singletonMap("/**", "jwt"));
		return shiroFilterFactoryBean;
	}

}
