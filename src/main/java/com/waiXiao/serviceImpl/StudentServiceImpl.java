package com.waiXiao.serviceImpl;

import com.waiXiao.mapper.StudentInfoVoMapper;
import com.waiXiao.pojo.StudentInfoVo;
import com.waiXiao.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param :
 * @author :     mym
 * @version :    V1.0
 * @date :       2019/11/27 16:56
 * @describe :   TODO
 * @return :
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentInfoVoMapper studentInfoVoMapper;
    @Override
    public Map<String, Object> selectStudentByParam(Map<String,Object> paramMap) throws Exception{
        Map<String, Object> returnMap = new HashMap<>();
        List<StudentInfoVo> rows = studentInfoVoMapper.selectStudentByParam(paramMap);
        int total = studentInfoVoMapper.selectStudentCountByParam(paramMap);
        returnMap.put("rows",rows);
        returnMap.put("total",total);
        return returnMap;
    }

    @Override
    public void insertStudentByList(List<StudentInfoVo> studentInfoList) throws Exception {
        studentInfoVoMapper.insertStudentByList(studentInfoList);
    }
}
