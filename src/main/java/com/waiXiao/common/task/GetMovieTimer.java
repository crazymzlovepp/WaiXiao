package com.waiXiao.common.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.waiXiao.common.Page;
import com.waiXiao.common.PageParserTool;
import com.waiXiao.common.RequestAndResponseTool;
import com.waiXiao.pojo.SystemLogVo;
import com.waiXiao.pojo.WebLinksVo;
import com.waiXiao.service.MovieService;
import com.waiXiao.service.SystemLogService;
/**
 * 
 * @author 		mym
 * @date   		2018年7月13日上午9:57:19
 * @describe	通过定时器获取新电影资源
 */
@Component
public class GetMovieTimer {
	@Autowired
	MovieService movieService;
	@Autowired
	SystemLogService systemLogService;
	//已访问的 url 集合  已经访问过的 主要考虑 不能再重复了 使用set来保证不重复;
	private static Set visitedUrlSet = new HashSet();
	//待访问的 url 集合  待访问的主要考虑 1:规定访问顺序;2:保证不提供重复的带访问地址;
	private static LinkedList unVisitedUrlQueue = new LinkedList();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Scheduled(cron="0 0/25 2 * * ?") //每天凌晨2点开始每隔25分钟执行一次  0 0/25 2 * * ?
	public void getNewMovie(){
		SystemLogVo systemLogVo = new SystemLogVo();
		systemLogVo.setLogId(UUID.randomUUID().toString().replaceAll("-", ""));
		systemLogVo.setLogName("通过定时器获取新电影资源  getNewMovie()");
		String startDate = sdf.format(new Date());
		String startInfo = "定时器开始执行时间为"+startDate;
		String endInfo = "";
		int nums = 0;//定时器执行时新增或更新的数据条数
		int insertType = 0;//新增成功后返回1  其他返回0
		try {
			List<WebLinksVo> webLinksList = movieService.getWebLinksVo();//每次查询两条网站初始化链接
			for (WebLinksVo webLinksVo : webLinksList) {
				//int i = 0;
				unVisitedUrlQueue.add(webLinksVo.getWebLink());
				//定义过滤器，例如提取以 http://www.dytt8.net/html/gndy/ 开头的链接
				//循环条件：待抓取的链接不空且每次最多抓取600的链接
				while (unVisitedUrlQueue!=null) {
					
					//先从待访问的序列中取出第一个；
					String visitUrl = (String)unVisitedUrlQueue.removeFirst();;
					if (visitUrl == null){
						continue;
					}

					//根据URL得到page;
					Page page = RequestAndResponseTool.sendRequstAndGetResponse(visitUrl);
					//对page进行处理： 访问DOM的某个标签
					Elements elements = PageParserTool.select(page,"a");
					List<Map<String,Object>> downHrefList = getMovieInfo(elements,page);
					insertType = movieService.insertAlldownHrefByList(downHrefList,webLinksVo);
					nums = nums+insertType;
					/*i += 1;
					if(i==1){//每一个网址只更新一次状态
						movieService.updateWebLinks(webLinksVo);
					}*/

					//将文件保存
					//FileTool.saveToLocal(page);

					//将已经访问过的链接放入已访问的链接中；
					//Links.addVisitedUrlSet(visitUrl);
					visitedUrlSet.add(visitUrl);


					//if(unVisitedUrlQueue.size()<=600){
						//得到超链接
						Set<String> links = PageParserTool.getLinks(page,"a");
						for (String link : links) {
							//Links.addUnvisitedUrlQueue(link);
							if (link != null && !link.trim().equals("")  && !visitedUrlSet.contains(link)  
									&& !unVisitedUrlQueue.contains(link) && link.startsWith("http")){
								unVisitedUrlQueue.add(link);
							}
							/*if(unVisitedUrlQueue.size()<=600){//超过600不在添加新链接
								continue;
							}else{
								break;
							}*/
						}
					//}
				}
			}
			String endDate = sdf.format(new Date());
			endInfo = ",定时器执行结束时间为"+endDate;
			systemLogVo.setLogContent("定时器正常执行，		"+startInfo+endInfo
					+"。此次定时器持续时间为"+(sdf.parse(endDate).getTime()-sdf.parse(startDate).getTime())/1000+"秒！  本次新增数据一共"+nums+"条！");
			systemLogVo.setLogLevel("1");
			systemLogVo.setLogCreateDate(new Date());
			systemLogService.insertSystemLog(systemLogVo);
		} catch (Exception e) {
			String endDate = sdf.format(new Date());
			try {
				endInfo = ",定时器执行结束时间为"+endDate;
				systemLogVo.setLogContent("定时器执行异常，		"+startInfo+endInfo
						+"。此次定时器持续时间为"+(sdf.parse(endDate).getTime()-sdf.parse(startDate).getTime())/1000+"秒！ 本次新增数据一共"+nums+"条！		异常信息为"+e.toString().substring(0, e.toString().length()>600?600:e.toString().length()));
				systemLogVo.setLogLevel("2");
				systemLogVo.setLogCreateDate(new Date());
				systemLogService.insertSystemLog(systemLogVo);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	//获得电影信息
	private List<Map<String,Object>> getMovieInfo(Elements elements, Page page) throws Exception{
		List<Map<String,Object>> returnList = new ArrayList<>();
		for (Element element : elements) {
			if(element!=null && element.toString().contains("ftp://")){
				Elements imgElements = PageParserTool.select(page,"img");
				Map<String,Object> map = new HashMap<>();
				if(!imgElements.isEmpty()){
					String imgUrl = "";
					for (Element imgElement : imgElements) {
						imgUrl = imgUrl + "#"+imgElement.toString();
					}
					map.put("downHrf", element.toString()+imgUrl);
				}else{
					map.put("downHrf", element.toString());
				}
				map.put("describes","暂无当前影片详情！");
				Elements describesElements = PageParserTool.select(page,"p");
				for (Element describesElement : describesElements) {
					if(describesElement.toString().trim().contains("简　　介")&&describesElement.toString().trim().contains("片　　名")&&describesElement.toString().trim().contains("<br>")){
						map.put("describes",describesElement);
					}
				}
				returnList.add(map);
			}
		}
		return returnList;
	}  
}