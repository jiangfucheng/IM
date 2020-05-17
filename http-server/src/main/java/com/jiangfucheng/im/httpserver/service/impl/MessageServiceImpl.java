package com.jiangfucheng.im.httpserver.service.impl;

import com.jiangfucheng.im.httpserver.bo.MessageBo;
import com.jiangfucheng.im.httpserver.bo.NotifyBo;
import com.jiangfucheng.im.httpserver.bo.OneMessageListBo;
import com.jiangfucheng.im.httpserver.bo.QueryHistoryMsgBo;
import com.jiangfucheng.im.httpserver.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 23:49
 *
 * @author jiangfucheng
 */
@Service
public class MessageServiceImpl implements MessageService {
	@Override
	public List<OneMessageListBo> getMessageList(Long userId) {
		return null;
	}

	@Override
	public List<MessageBo> queryHistoryMessage(QueryHistoryMsgBo queryHistoryMsgBo) {
		return null;
	}

	@Override
	public List<NotifyBo> queryNotifiesWthUser(Long userId) {
		return null;
	}
}
