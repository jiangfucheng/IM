package com.jiangfucheng.im.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiangfucheng.im.auth.bo.PermissionResourceBo;
import com.jiangfucheng.im.auth.mapper.PermissionMapper;
import com.jiangfucheng.im.auth.mapper.UserPermissionMapper;
import com.jiangfucheng.im.auth.po.PermissionPo;
import com.jiangfucheng.im.auth.po.UserPermissionPo;
import com.jiangfucheng.im.auth.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 10:31
 *
 * @author jiangfucheng
 */
@Service
public class PermissionServiceImpl implements PermissionService {

	private final PermissionMapper permissionMapper;
	private final UserPermissionMapper userPermissionMapper;

	@Autowired
	public PermissionServiceImpl(PermissionMapper permissionMapper, UserPermissionMapper userPermissionMapper) {
		this.permissionMapper = permissionMapper;
		this.userPermissionMapper = userPermissionMapper;
	}

	@Override
	public void createPermission(PermissionPo permissionPo) {
		permissionMapper.insert(permissionPo);
	}

	@Override
	public void createPermission(String permissionCode) {
		createPermission(decodePermissionCode(permissionCode));
	}

	@Override
	public void deletePermission(Long permissionId) {
		permissionMapper.deleteById(permissionId);
	}

	@Override
	public void updatePermission(PermissionPo permissionPo) {
		permissionMapper.updateById(permissionPo);
	}

	@Override
	public String hasPermission(Long id) {
		PermissionPo permissionPo = permissionMapper.selectById(id);
		return convertPermissionCode(permissionPo);
	}

	@Override
	public List<String> queryPermissionWithUser(Long userId) {
		List<UserPermissionPo> userPermissionPos = userPermissionMapper.selectList(new QueryWrapper<UserPermissionPo>()
				.eq("user_id", userId));
		List<String> permissionCodes = new ArrayList<>();
		userPermissionPos.stream().map(UserPermissionPo::getPermissionId)
				.forEach(pid -> permissionCodes.add(convertPermissionCode(permissionMapper.selectById(pid))));
		return permissionCodes;
	}

	@Override
	public boolean hasPermission(Long userId, Long permissionId) {
		return null != userPermissionMapper.selectOne(new QueryWrapper<UserPermissionPo>().eq("user_id", userId)
				.eq("permission__id", permissionId));
	}

	@Override
	public boolean hasPermission(Long userId, String permissionCode) {
		PermissionPo permissionPo = decodePermissionCode(permissionCode);
		permissionPo = permissionMapper.selectOne(new QueryWrapper<PermissionPo>()
				.eq("resource_name", permissionPo.getResourceName())
				.eq("resource_id", permissionPo.getResourceId())
				.eq("operation", permissionPo.getOperation()));

		return hasPermission(userId, permissionPo.getId());
	}

	@Override
	public List<String> queryPermissions() {
		return permissionMapper.selectList(new QueryWrapper<>()).stream().map(this::convertPermissionCode).collect(Collectors.toList());
	}

	@Override
	public void awardPermission(Long userId, String permissionCode) {
		PermissionPo permissionPo = decodePermissionCode(permissionCode);
		permissionPo = permissionMapper.selectOne(new QueryWrapper<PermissionPo>()
				.eq("resource_name", permissionPo.getResourceName())
				.eq("resource_id", permissionPo.getResourceId())
				.eq("operation", permissionPo.getOperation()));
		UserPermissionPo userPermissionPo = new UserPermissionPo();
		userPermissionPo.setUserId(userId);
		userPermissionPo.setPermissionId(permissionPo.getId());
		userPermissionMapper.insert(userPermissionPo);
	}

	@Override
	public void revokePermission(Long userId, Long permissionId) {
		userPermissionMapper.delete(new QueryWrapper<UserPermissionPo>()
				.eq("user_id", userId)
				.eq("permission_id", permissionId));
	}

	@Override
	public void revokePermissionByResource(PermissionResourceBo permissionResourceBo) {
		List<Long> permissionIds = new ArrayList<>();
		PermissionPo permissionPo = decodePermissionCode(String.format("%s:%s:%s", permissionResourceBo.getResourceName(), "*", permissionResourceBo.getResourceId()));
		PermissionPo permissionPoView = decodePermissionCode(String.format("%s:%s:%s", permissionResourceBo.getResourceName(), "view", permissionResourceBo.getResourceId()));
		PermissionPo permissionPoUpdate = decodePermissionCode(String.format("%s:%s:%s", permissionResourceBo.getResourceName(), "update", permissionResourceBo.getResourceId()));
		PermissionPo permissionPoCreate = decodePermissionCode(String.format("%s:%s:%s", permissionResourceBo.getResourceName(), "create", permissionResourceBo.getResourceId()));
		PermissionPo permissionPoDelete = decodePermissionCode(String.format("%s:%s:%s", permissionResourceBo.getResourceName(), "delete", permissionResourceBo.getResourceId()));
		insertPermissionPoToList(permissionPo, permissionIds);
		insertPermissionPoToList(permissionPoView, permissionIds);
		insertPermissionPoToList(permissionPoUpdate, permissionIds);
		insertPermissionPoToList(permissionPoCreate, permissionIds);
		insertPermissionPoToList(permissionPoDelete, permissionIds);
		permissionIds.forEach(pid -> userPermissionMapper.delete(new QueryWrapper<UserPermissionPo>()
				.eq("user_id", permissionResourceBo.getResourceId())
				.eq("permission_id", pid)));
	}

	private void insertPermissionPoToList(PermissionPo permissionPo, List<Long> list) {
		list.add(permissionMapper.selectOne(new QueryWrapper<PermissionPo>()
				.eq("resource_name", permissionPo.getResourceName())
				.eq("operation", permissionPo.getOperation())
				.eq("resource_id", permissionPo.getResourceId()))
				.getId());
	}


	@Override
	public void revokePermissionByPermissionCode(Long userId, String permissionCode) {
		PermissionPo permissionPo = decodePermissionCode(permissionCode);
		permissionPo = permissionMapper.selectOne(new QueryWrapper<PermissionPo>()
				.eq("resource_name", permissionPo.getResourceName())
				.eq("resource_id", permissionPo.getResourceId())
				.eq("operation", permissionPo.getOperation()));
		revokePermissionByPermissionId(userId, permissionPo.getId());
	}

	@Override
	public void revokePermissionByPermissionId(Long userId, Long permissionId) {
		userPermissionMapper.delete(new QueryWrapper<UserPermissionPo>()
				.eq("user_id", userId)
				.eq("permission_id", permissionId));
	}

	private String convertPermissionCode(PermissionPo permissionPo) {
		return String.format("%s:%s:%s", permissionPo.getResourceName(), permissionPo.getOperation(), permissionPo.getId());
	}

	private PermissionPo decodePermissionCode(String permissionCode) {
		String[] permission = permissionCode.split(":");
		PermissionPo permissionPo = new PermissionPo();
		permissionPo.setResourceName(permission[0]);
		permissionPo.setOperation(permission[1]);
		permissionPo.setResourceId(Long.valueOf(permission[2]));
		return permissionPo;
	}
}
