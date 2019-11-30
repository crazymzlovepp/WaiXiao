package com.waiXiao.controller;


import com.alibaba.druid.util.StringUtils;
import com.waiXiao.common.utils.BeanToMap;
import com.waiXiao.common.utils.FileTool;
import com.waiXiao.pojo.StudentInfoVo;
import com.waiXiao.service.StudentService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/* *
* @author :     mym
* @date Date :  2019/11/27 16:53
* @version :    V1.0
* @describe :   学生信息
* @param :      
* @return :     
*/
@RestController
@RequestMapping("/student")
public class StudentController {
    private static final Log LOG = LogFactory.getLog(StudentController.class);
	@Autowired
	StudentService studentService;
	/**
	 * 
	 * @author 		mym
	 * @date   		2018年7月31日下午3:19:31
	 * @describe	
	 * @return		String
	 *
	 */
	@RequestMapping("/studentInfo")
	public ModelAndView goToStudentInfo(ModelAndView model){
		try {
			model.addObject("userName","admin");
			model.setViewName("actionPage/studentInfo");
		} catch (Exception e) {
            LOG.error(e.getMessage());
			//e.printStackTrace();
		}
		return model;
	}
	/* *
	* @author :     mym
	* @date Date :  2019/11/27 17:53
	* @version :    V1.0
	* @describe :   查询学生信息
	* @param :      
	* @return :     
	*/
	@RequestMapping("/selectStudentByParam")
	public Map<String,Object> selectStudentByParam(StudentInfoVo studentInfoVo, String birthdayTimeStart, String birthdayTimeEnd){
		Map<String,Object> returnMap = new HashMap<>();
		try {
			Map<String,Object> paramMap = BeanToMap.beanToMap(studentInfoVo);
            paramMap.put("birthdayTimeStart",birthdayTimeStart);
            paramMap.put("birthdayTimeEnd",birthdayTimeEnd);
            returnMap = studentService.selectStudentByParam(paramMap);
		} catch (Exception e) {
            LOG.error(e.getMessage());
		}
		return returnMap;
	}
	/* *
	* @author :     mym
	* @date Date :  2019/11/29 11:28
	* @version :    V1.0
	* @describe :   一键上传学生信息
	* @param :      
	* @return :     
	*/
	@RequestMapping("/importAll")
	public Map<String,Object> importAll(@RequestParam("file") MultipartFile importAllFile){
		Map<String,Object> returnMap = new HashMap<>();
		List<StudentInfoVo> studentInfoList = new ArrayList<>();
		try {
			if(!importAllFile.isEmpty() && importAllFile.getInputStream() != null){
				File file = new File(importAllFile.getOriginalFilename());
				file = FileTool.inputStreamToFile(importAllFile.getInputStream(),file);
				FileInputStream fis = new FileInputStream(file);
				XSSFWorkbook wook = new XSSFWorkbook(fis);
				XSSFSheet sheet = wook.getSheetAt(0);
				for (int i = 1; i < sheet.getLastRowNum(); i++) {//表头不新增
					XSSFRow row = sheet.getRow(i);
					if(row != null){
						if((FileTool.getCellValueToString(row.getCell(0)) == FileTool.getCellValueToString(row.getCell(1)))
								&& FileTool.getCellValueToString(row.getCell(0)) == ""){
							break;
						}

						StudentInfoVo vo = new StudentInfoVo();
						vo.setStudentId(UUID.randomUUID().toString().replaceAll("-",""));
						vo.setStudentNum(Integer.valueOf(FileTool.getCellValueToString(row.getCell(0))));
						vo.setStudentName(FileTool.getCellValueToString(row.getCell(1)));
						vo.setSex(getSex(FileTool.getCellValueToString(row.getCell(2))));
						vo.setBirthday(getBirthday(FileTool.getCellValueToString(row.getCell(3))));
						vo.setAge(Integer.valueOf(FileTool.getCellValueToString(row.getCell(4))));
						vo.setNational("汉");
						vo.setHomeAddress(FileTool.getCellValueToString(row.getCell(6)));
						vo.setGuardian(FileTool.getCellValueToString(row.getCell(7)));
						vo.setGuardianPhone(FileTool.getCellValueToString(row.getCell(8)));
						vo.setRemark("初始化批量导入");
						vo.setCreateDate(new Date());
						studentInfoList.add(vo);
					}
				}
				//删除临时文件
				file.delete();
			}
			studentService.insertStudentByList(studentInfoList);
			returnMap.put("success","0");
			returnMap.put("msg","");
		} catch (Exception e) {
			LOG.error(e.getMessage());
			returnMap.put("success","1");
			returnMap.put("msg","批量上传学生信息失败！");
			e.printStackTrace();
		}
		return returnMap;
	}

	private Integer getSex(String value) throws Exception{
		Integer sex = 99;
		if(StringUtils.isEmpty(value)){
			return sex;
		}
		if(value.equals("男")){//1男2女0其他
			sex = 1;
		}else if(value.equals("女")){
			sex = 2;
		}else{
			sex = 0;
		}
		return sex;
	}

	private Date getBirthday(String value) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		if(value == null){
			return date;
		}
		date = sdf1.parse(value);
		String dateStr = sdf.format(date);
		return sdf.parse(dateStr);
	}
}
