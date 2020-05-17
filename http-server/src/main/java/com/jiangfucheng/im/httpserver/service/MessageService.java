package com.jiangfucheng.im.httpserver.service;

import com.jiangfucheng.im.httpserver.bo.MessageBo;
import com.jiangfucheng.im.httpserver.bo.NotifyBo;
import com.jiangfucheng.im.httpserver.bo.OneMessageListBo;
import com.jiangfucheng.im.httpserver.bo.QueryHistoryMsgBo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 23:06
 *
 * @author jiangfucheng
 */

public interface MessageService {

	List<OneMessageListBo> getMessageList(Long userId);

	List<MessageBo> queryHistoryMessage(QueryHistoryMsgBo queryHistoryMsgBo);

	List<NotifyBo> queryNotifiesWthUser(Long userId);

}
