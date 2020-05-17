package com.jiangfucheng.im.auth.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 10:19
 *
 * @author jiangfucheng
 */
@Data
@TableName("im_user_permission")
public class UserPermissionPo {
	private Long userId;
	private Long permissionId;
	private Long createTime;
}
