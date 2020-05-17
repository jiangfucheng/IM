package com.jiangfucheng.im.httpserver.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:01
 *
 * @author jiangfucheng
 */
@Data
@TableName("im_recently_notify")
public class RecentlyNotifyPo {
	private Long id;
	private Long userId;
	private Long notifyId;
	private Long createTime;
}
