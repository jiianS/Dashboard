package com.java.eis;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	@Resource(name = "sqlSession")
	SqlSession session;
	

	@RequestMapping("getBar")
	public void getBar(HttpServletResponse resp) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		/*********************************************************************************/
		List<HashMap<String, Object>> resultList = session.selectList("eis.selectBar");
		List<HashMap<String, Object>> columns = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("type", "string");
		columnMap.put("value", "city");
		columnMap.put("column", "city");
		columns.add(columnMap);
		
		columnMap = new HashMap<String, Object>();
		columnMap.put("type", "number");
		columnMap.put("value", "2000 Population");
		columnMap.put("column", "Population2000");
		columns.add(columnMap);
		
		columnMap = new HashMap<String, Object>();
		columnMap.put("type", "number");
		columnMap.put("value", "2010 Population");
		columnMap.put("column", "Population2010");
		columns.add(columnMap);
		
//		같은 주소에 넣게 하지 않기 위해, columnMap을 새로 생성해서 각각 넣어준다. 
//		columns.add("city");
//		columns.add("Population2000");
//		columns.add("Population2000");
		
		result.put("result",resultList);
		result.put("size",resultList.size());
		result.put("columns", columns);
		/*********************************************************************************/
		//result를 보낼떄, utf8로 인코딩하고, json타입으로 보내려고 json타입으로 변환시켜주기
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		JSONObject json = new JSONObject();
		json = JSONObject.fromObject(JSONSerializer.toJSON(result));
		
		try {
			resp.getWriter().write(json.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@RequestMapping("getMelon")
	public void getMelon(HttpServletResponse resp) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		List<HashMap<String, Object>> resultList = session.selectList("eis.selectMelon");
		List<HashMap<String, Object>> columns = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("type", "string");
		columnMap.put("value", "genre");
		columnMap.put("column", "genre");
		columns.add(columnMap);
		
		columnMap = new HashMap<String, Object>();
		columnMap.put("type", "number");
		columnMap.put("value", "2015_10월 평균");
		columnMap.put("column", "avg201510");
		columns.add(columnMap);
		
		columnMap = new HashMap<String, Object>();
		columnMap.put("type", "number");
		columnMap.put("value", "2016_10월 평균");
		columnMap.put("column", "avg201610");
		columns.add(columnMap);
		
		columnMap = new HashMap<String, Object>();
		columnMap.put("type", "number");
		columnMap.put("value", "2017_10월 평균");
		columnMap.put("column", "avg201710");
		columns.add(columnMap);
		
		
		result.put("result",resultList);
		result.put("size",resultList.size());
		result.put("columns", columns);
		
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		JSONObject json = new JSONObject();
		json = JSONObject.fromObject(JSONSerializer.toJSON(result));
		
		try {
			resp.getWriter().write(json.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 0801 __ 수정된 부분
	@RequestMapping(value = "getData/{viewNm}", method = RequestMethod.POST)
//	@RequestMapping(value = "getData/{viewNm}")  get일경우 -> /eis/getData/google -> json타입으로 나옴
//	post -> 405에러 나옴(post방식이기에 데이터가 보여질 수 없다)
	public void getData(@PathVariable("viewNm") String viewNm, HttpServletResponse resp) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		List<HashMap<String, Object>> resultList = session.selectList("eis.selectBar");
		
		result.put("result", resultList);
		result.put("size", resultList.size());
		result.put("columns", session.selectList("eis.selectColumnList", viewNm));
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=utf-8");
		JSONObject json = JSONObject.fromObject(JSONSerializer.toJSON(result));
		try {
			resp.getWriter().write(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 이미지는 길기떄문에 무조건 POST로 와야함
	@RequestMapping(value="getImg", method = RequestMethod.POST)
	public void getImg(HttpServletRequest req) {
		String imgData = req.getParameter("imgData");
		System.out.println(imgData);
		
	}
	
	
}

