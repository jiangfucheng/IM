package com.jiangfucheng.im.httpserver.bo;

import com.jiangfucheng.im.httpserver.utils.BeanUtil;
import com.jiangfucheng.im.httpserver.vo.MessageListElementVo;
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
	private Integer fromId;
	private Integer fromName;
	private String profilePhoto;
	private Long lastMsgTime;
	private Integer UnreadMsgCont;

	public MessageListElementVo convertToMessageListElementVo() {
		MessageListElementVo vo = new MessageListElementVo();
		BeanUtil.copyProperties(vo, this);
		return vo;
	}
}
