package com.waiXiao.serviceImpl;

import com.waiXiao.mapper.SystemLogVoMapper;
import com.waiXiao.pojo.MenuVo;
import com.waiXiao.pojo.SystemLogVo;
import com.waiXiao.service.SystemLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SystemLogServiceImpl implements SystemLogService {
	@Autowired
	SystemLogVoMapper systemLogVoMapper;
	/**
	 * 
	 * @author		mym
	 * @date        2018年8月24日下午7:05:28
	 * @describe    新增系统日志
	 * @return      void
	 */
	@Override
	public void insertSystemLog(SystemLogVo systemLogVo) {
		systemLogVoMapper.insert(systemLogVo);
	}
	//多条件查询系统日志
	@Override
	public Map<String, Object> selectAllSystemLogInfoByParam(Map<String, Object> returnMap) {
		try{
			int total = systemLogVoMapper.selectAllSystemLogCountInfoByParam(returnMap);
			List<MenuVo> menuVoList = systemLogVoMapper.selectAllSystemLogInfoByParam(returnMap);
			returnMap.put("total",total);
			returnMap.put("rows",menuVoList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnMap;
	}
}
