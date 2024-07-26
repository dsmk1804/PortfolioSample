package com.ebook.mapper;

import com.ebook.dto.CategoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 카테고리별로 가져오는 작업
    List<CategoryDTO> selectAllCategory();

}
