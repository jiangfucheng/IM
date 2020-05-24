package com.jiangfucheng.im.model.bo;

import com.jiangfucheng.im.model.utils.BeanUtil;
import com.jiangfucheng.im.model.vo.NotifyVo;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 23:17
 *
 * @author jiangfucheng
 */
@Data
public class NotifyBo {
	private Long id;
	private Integer type;
	private Long fromId;
	private String name;
	private String profilePhoto;
	private String content;
	private String verifyMessage;
	private Long createTime;

	public NotifyVo convertToNotifyVo() {
		NotifyVo vo = new NotifyVo();
		BeanUtil.copyProperties(vo, this);
		return vo;
	}
}
