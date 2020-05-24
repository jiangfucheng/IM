package com.jiangfucheng.im.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 23:44
 *
 * @author jiangfucheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMemberWithIndexVo {
	private Character index;
	private List<GroupMemberVo> groupMembers;
}
