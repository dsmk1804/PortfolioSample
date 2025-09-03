package com.ebook.service;

import com.ebook.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class  CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

}
