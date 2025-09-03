package com.bytearchive.mapper;

import com.bytearchive.dto.FileDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {
    FileDTO selectFileByUserEmail(String userEmail);
    FileDTO selectFileByUuid(String uuid);
}
