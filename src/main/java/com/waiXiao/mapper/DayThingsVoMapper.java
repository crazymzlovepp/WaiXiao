package com.waiXiao.mapper;

import com.waiXiao.pojo.DayThingsVo;
import java.util.List;

public interface DayThingsVoMapper {
    int deleteByPrimaryKey(String id);

    int insert(DayThingsVo record);

    DayThingsVo selectByPrimaryKey(String id);

    List<DayThingsVo> selectAll();

    int updateByPrimaryKey(DayThingsVo record);

    int selectDaysByDayId(String dayId);
}