package com.fred.code.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.fred.code.domain.CodeDatabase;
import com.fred.code.service.CodeDatabaseService;
import com.fred.code.tool.Tool;

/**
 * Created by IntelliJ IDEA. User: 14080608 Date: 16-4-8 Time: 下午6:18 To change
 * this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("code")
public class CodeDatabaseController extends BaseController {

	@Autowired
	private CodeDatabaseService codeDatabaseService;

	@RequestMapping("list")
	public ModelAndView list(HttpServletRequest request) {
		Map<String, Object> map = getParameterMap(request);
		map.put("page", getPage(request));
		List<CodeDatabase> list = codeDatabaseService.pageList(map);
		ModelAndView mav = new ModelAndView("code/list");
		mav.addObject("list", list);
		return mav;
	}

	@RequestMapping("create")
	public String create() {
		return "code/create";
	}

	@RequestMapping("save")
	public String save(CodeDatabase codeDatabase) {
		codeDatabase.setCreateTime(new Date());
		codeDatabaseService.insert(codeDatabase);
		return "redirect:/code/list.do";
	}

	@RequestMapping("edit")
	public ModelAndView edit(@RequestParam("id") Long id) {
		CodeDatabase database = codeDatabaseService.getById(id);
		ModelAndView mav = new ModelAndView("code/edit");
		mav.addObject("database", database);
		return mav;
	}

	@RequestMapping("update")
	public String update(CodeDatabase database) {
		codeDatabaseService.update(database);
		return "redirect:/code/list.do";
	}

	@RequestMapping("delete")
	public String delete(@RequestParam("id") Long id) {
		codeDatabaseService.deleteById(id);
		return "redirect:/code/list.do";
	}

	@RequestMapping("toGenerate")
	public String toGenerate() {
		return "code/generate";
	}

	@RequestMapping("loadTable")
	public void loadTable(@RequestParam("id") Long id, HttpServletResponse response) {
		try {
			CodeDatabase codeDatabase = codeDatabaseService.getById(id);
			if (codeDatabase == null) {
				response.getWriter().write("{'error':'true','msg':'id=[" + id + "]对应的数据库配置不存在.'}");
				return;
			}
			List<String> list = Tool.loadTables(codeDatabase);
			response.getWriter().write(JSON.toJSONString(list));
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			try {
				response.getWriter().write("{'error':'true','msg':'" + throwable.getMessage() + "'}");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping("generate")
	public void generate(@RequestParam("id") Long id, @RequestParam("tableName") String tableName,
			HttpServletResponse response) {
		try {
			CodeDatabase codeDatabase = codeDatabaseService.getById(id);
			if (codeDatabase == null) {
				response.getWriter().write(String.format("生成失败，id=[%s]对应的数据库配置不存在.", id));
				return;
			}
			String result = Tool.generate(codeDatabase, tableName);
			response.getWriter().write("生成成功，<a href=\"http://10.27.103.227/code/" + result + "\">点击下载</a>");
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			try {
				response.getWriter().write("生成失败，异常信息：" + throwable.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
