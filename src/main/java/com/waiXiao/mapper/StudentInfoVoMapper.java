package com.waiXiao.mapper;

import com.waiXiao.pojo.StudentInfoVo;
import java.util.List;
import java.util.Map;

public interface StudentInfoVoMapper {
    int deleteByPrimaryKey(String studentId);

    int insert(StudentInfoVo record);

    StudentInfoVo selectByPrimaryKey(String studentId);

    List<StudentInfoVo> selectAll();

    int updateByPrimaryKey(StudentInfoVo record);

    List<StudentInfoVo> selectStudentByParam(Map<String,Object> paramMap) throws Exception;

    int selectStudentCountByParam(Map<String,Object> paramMap) throws Exception;

    void insertStudentByList(List<StudentInfoVo> studentInfoList) throws Exception;
}