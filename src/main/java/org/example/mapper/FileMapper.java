package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entitys.File;

@Mapper
public interface FileMapper extends BaseMapper<File> {
}
