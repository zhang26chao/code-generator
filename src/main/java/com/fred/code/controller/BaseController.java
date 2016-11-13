package com.fred.code.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.fred.code.domain.Page;
import com.fred.page.domain.DalPage;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 16-2-4 Time: 上午10:12 To
 * change this template use File | Settings | File Templates.
 */
public class BaseController {

	protected DalPage getPage(HttpServletRequest request) {
		DalPage page = new DalPage();
		String currentPage = request.getParameter(Page.PAGE_NUMBER);
		if (StringUtils.isNotBlank(currentPage)) {
			try {
				int pageNumber = Integer.decode(currentPage);
				page.setCurrentPage(pageNumber < 1 ? 1 : pageNumber);
			} catch (Exception e) {
				page.setCurrentPage(1);
			}
		}
		request.setAttribute(Page.PAGE, page);
		String path = (String) request.getAttribute(Page.ORIGINAL_SERVLET_PATH);
		if (path == null) {
			request.setAttribute(Page.ORIGINAL_SERVLET_PATH,
					request.getServletPath());
		}
		return page;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> getParameterMap(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String[]> urlParamsMap = request.getParameterMap();
		if (urlParamsMap != null) {
			for (Map.Entry<String, String[]> entry : urlParamsMap.entrySet()) {
				// 忽略分页参数
				if (StringUtils.equals(entry.getKey(), Page.PAGE_NUMBER)) {
					continue;
				}
				map.put(entry.getKey(), entry.getValue()[0]);
			}
		}
		return map;
	}

}
