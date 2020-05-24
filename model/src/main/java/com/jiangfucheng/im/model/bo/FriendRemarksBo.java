package com.jiangfucheng.im.model.bo;

import com.jiangfucheng.im.model.po.RelationPo;
import com.jiangfucheng.im.common.utils.BeanUtil;
import com.jiangfucheng.im.model.vo.UpdateFriendRemarksVo;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 23:21
 *
 * @author jiangfucheng
 */
@Data
public class FriendRemarksBo {
	private Long userId;
	private Long friendId;
	private String remarks;

	public UpdateFriendRemarksVo convertToUpdateFriendRemarksVo() {
		UpdateFriendRemarksVo vo = new UpdateFriendRemarksVo();
		BeanUtil.copyProperties(vo, this);
		return vo;
	}

	public RelationPo convertToRelationPo() {
		RelationPo po = new RelationPo();
		BeanUtil.copyProperties(po, this);
		return po;
	}
}
