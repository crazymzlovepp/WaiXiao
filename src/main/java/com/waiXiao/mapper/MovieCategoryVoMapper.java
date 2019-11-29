package com.waiXiao.mapper;

import com.waiXiao.pojo.MovieCategoryVo;
import java.util.List;

public interface MovieCategoryVoMapper {
    int insert(MovieCategoryVo record);

    List<MovieCategoryVo> selectAll();

}