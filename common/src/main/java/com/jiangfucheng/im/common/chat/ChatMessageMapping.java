package com.jiangfucheng.im.common.chat;

import com.jiangfucheng.im.protobuf.Base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/20
 * Time: 23:58
 *
 * @author jiangfucheng
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChatMessageMapping {
	Base.DataType messageType();
}
