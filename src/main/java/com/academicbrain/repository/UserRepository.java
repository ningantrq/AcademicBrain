package com.academicbrain.repository;

import com.academicbrain.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 * @author AcademicBrain
 */
@Mapper
public interface UserRepository extends BaseMapper<User> {
} 