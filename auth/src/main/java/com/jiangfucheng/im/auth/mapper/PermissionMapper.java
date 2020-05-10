package com.jiangfucheng.im.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.auth.po.PermissionPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 10:30
 *
 * @author jiangfucheng
 */
@Mapper
@Repository
public interface PermissionMapper extends BaseMapper<PermissionPo> {
}
