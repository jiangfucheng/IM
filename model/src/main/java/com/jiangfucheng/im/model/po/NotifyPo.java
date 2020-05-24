package com.jiangfucheng.im.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jiangfucheng.im.model.utils.BeanUtil;
import com.jiangfucheng.im.model.bo.NotifyBo;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:00
 *
 * @author jiangfucheng
 */
@Data
@TableName("im_notify")
public class NotifyPo {
	private Long id;
	private Integer type;
	private Long fromId;
	private Long toId;
	private String content;
	private Date createTime;

	public NotifyBo convertToNotifyBo(){
		NotifyBo bo = new NotifyBo();
		BeanUtil.copyProperties(bo,this);
		bo.setCreateTime(this.createTime.getTime());
		return bo;
	}
}
