package com.jiangfucheng.im.model.bo;

import com.jiangfucheng.im.httpserver.utils.BeanUtil;
import com.jiangfucheng.im.model.vo.MessageListElementVo;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 23:07
 *
 * @author jiangfucheng
 */
@Data
public class OneMessageListBo {
	private Integer type;
	private Long lastMsgId;
	private String lastMsg;
	private Integer lastMsgType;
	private Long fromId;
	private String fromName;
	private String profilePhoto;
	private Long lastMsgTime;
	private Integer unreadMsgCount;

	public MessageListElementVo convertToMessageListElementVo() {
		MessageListElementVo vo = new MessageListElementVo();
		BeanUtil.copyProperties(vo, this);
		return vo;
	}
}
