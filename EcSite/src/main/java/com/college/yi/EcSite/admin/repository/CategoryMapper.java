package com.college.yi.EcSite.admin.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.college.yi.EcSite.entity.Category;

@Mapper
public interface CategoryMapper {
    Category findById(@Param("categoryId") Long categoryId);
    List<Category> findByIds(@Param("categoryIds") List<Long> categoryIds);
    List<Category> findAll();
}
