package com.jiangfucheng.im.httpserver.bo;

import com.jiangfucheng.im.httpserver.utils.BeanUtil;
import com.jiangfucheng.im.httpserver.vo.MessageVo;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 23:12
 *
 * @author jiangfucheng
 */
@Data
public class MessageBo {
	private Long id;
	private Long fromId;
	private String profilePhoto;
	private String nickName;
	private String remarks;
	private Integer msgType;
	private String content;
	private Long crateTime;

	public MessageVo convertToMessageVo() {
		MessageVo vo = new MessageVo();
		BeanUtil.copyProperties(vo, this);
		return vo;
	}
}
