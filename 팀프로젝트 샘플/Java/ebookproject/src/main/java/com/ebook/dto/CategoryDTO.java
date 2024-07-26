package com.ebook.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

// 카테고리를 따로 나누기 위해
@Getter
@Setter
@ToString
public class CategoryDTO {
    // 카테고리 별로 번호와 제목을 수집한다.
    private int no; // 번호. pk
    private String name;
    private List<CategoryDTO> children;

    public String getCategoryName() {
        return this.name;
    }
}
