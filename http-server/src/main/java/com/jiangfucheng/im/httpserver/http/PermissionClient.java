package com.jiangfucheng.im.httpserver.http;

import com.alibaba.fastjson.JSON;
import com.jiangfucheng.im.common.constants.ErrorCode;
import com.jiangfucheng.im.httpserver.exceptions.IMException;
import com.jiangfucheng.im.httpserver.utils.BeanUtil;
import com.jiangfucheng.im.model.vo.PermissionResourceVo;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/17
 * Time: 21:05
 *
 * @author jiangfucheng
 */

public class PermissionClient {

	private OkHttpClient okHttpClient;

	public PermissionClient(OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
	}

	public boolean hasPermission(Long userId, String permissionCode) {
		/*
			/permission/{user_id}/{permission_code}
		 */
		String url = "/permission/%s/%s";
		url = String.format(url, userId, permissionCode);
		Request.Builder builder = new Request.Builder();
		Request request = builder.get().url(url).build();
		return parseResponseData(execute(request), Boolean.class);
	}

	public void createPermission(String permissionCode) {
		/*
			/permission/{permission_code}
		 */
		String url = "/permission/%s";
		url = String.format(url, permissionCode);
		Request.Builder builder = new Request.Builder();
		Request request = builder
				.post(null)
				.url(url)
				.build();
		execute(request);
	}

	public void deletePermission(Long permissionId) {
		/*
			/permission/{permission_id}
		 */
		String url = "/permission/%s";
		url = String.format(url, permissionId);
		Request.Builder builder = new Request.Builder();
		Request request = builder
				.delete()
				.url(url)
				.build();
		execute(request);
	}

	public void queryPermission(Long userId) {
		String url = "/permission/%s";
		url = String.format(url, userId);
		Request.Builder builder = new Request.Builder();
		Request request = builder
				.get()
				.url(url)
				.build();
		List list = parseResponseData(execute(request), List.class);
	}

	public void awardPermission(Long userId, String permissionCode) {
		/*
			/permission/{user_id}/{permission_code}
		 */
		String url = "/permission/%s/%s";
		url = String.format(url, userId, permissionCode);
		Request.Builder builder = new Request.Builder();
		Request request = builder
				.post(null)
				.url(url)
				.build();
		execute(request);
	}

	public void revokePermission(Long userId, String permissionCode) {
/*
			/permission/{user_id}/{permission_code}
		 */
		String url = "/permission/%s/%s";
		url = String.format(url, userId, permissionCode);
		Request.Builder builder = new Request.Builder();
		Request request = builder
				.delete(null)
				.url(url)
				.build();
		execute(request);
	}

	public void revokePermission(Long userId, PermissionResourceVo permissionResourceVo) {
		/*
			/permission/{user_id}/{permission_code}
		 */
		String url = "/permission/%s";
		url = String.format(url, userId);
		Request.Builder builder = new Request.Builder();
		Request request = builder
				.delete(RequestBody.create(MediaType.parse("application/json")
						, JSON.toJSONString(permissionResourceVo)))
				.url(url)
				.build();
		execute(request);
	}

	private String execute(Request request) {
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			return response.body().string();
		} catch (IOException e) {
			throw new IMException(ErrorCode.RPC_ERROR, "远程接口调用异常");
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	private <T> T parseResponseData(String response, Class<T> clazz) {
		com.jiangfucheng.im.common.resp.Response res = JSON.parseObject(response, com.jiangfucheng.im.common.resp.Response.class);
		T t = null;
		try {
			t = clazz.newInstance();
			BeanUtil.copyProperties(t, res.getData());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return t;
	}

}
