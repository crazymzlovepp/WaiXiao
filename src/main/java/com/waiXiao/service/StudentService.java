package com.waiXiao.service;

import com.waiXiao.pojo.StudentInfoVo;

import java.util.List;
import java.util.Map;

/**
 * @param :
 * @author :     mym
 * @version :    V1.0
 * @date :       2019/11/27 16:55
 * @describe :   TODO
 * @return :
 */
public interface StudentService {
    Map<String, Object> selectStudentByParam(Map<String,Object> paramMap) throws Exception;
    //批量导入学生信息
    void insertStudentByList(List<StudentInfoVo> studentInfoList) throws Exception;
}
