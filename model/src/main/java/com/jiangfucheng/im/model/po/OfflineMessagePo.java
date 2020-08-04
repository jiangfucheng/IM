package com.jiangfucheng.im.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jiangfucheng.im.model.bo.OfflineMessageBo;
import com.jiangfucheng.im.model.utils.BeanUtil;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:01
 *
 * @author jiangfucheng
 */
@Data
@TableName("im_offline_msg")
public class OfflineMessagePo {
	private Long id;
	private Long fromId;
	private Long toId;
	private Integer msgType;
	private String content;
	private Date createTime;

	public OfflineMessageBo convertToOfflineMessageBo() {
		OfflineMessageBo bo = new OfflineMessageBo();
		BeanUtil.copyProperties(bo, this);
		return bo;
	}
}
