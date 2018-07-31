package com.java.eis;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
		result.put("result",session.selectList("eis.selectBar"));
		
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

}
