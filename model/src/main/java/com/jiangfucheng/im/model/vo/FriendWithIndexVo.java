package com.jiangfucheng.im.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 23:47
 *
 * @author jiangfucheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendWithIndexVo {
	private Character index;
	//无实际含义，给前端使用
	private Character remarks;
	private List<FriendVo> friends;
}
