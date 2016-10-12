package com.fred.code.framework.taglib;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.arvato.page.domain.DalPage;
import com.fred.code.domain.Page;

public class PaginatorTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	@Override
	public int doEndTag() throws JspException {
		try {
			DalPage page = (DalPage) pageContext.findAttribute(Page.PAGE);
			Integer pageNumber = page.getCurrentPage();
			int pageCount = page.getPages();
			renderNewPaginator(pageContext.getOut(), pageNumber, pageCount, getParameters());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	private void renderNewPaginator(Writer out, int pageNumber, int pageCount, Map<String, String> params)
			throws IOException {
		if (pageCount <= 1) {
			return;
		}
		out.write("<nav style=\"text-align:right;\">");
		out.write("<ul class=\"pagination\">");
		// 上一页、下一页
		int prevPage = (pageNumber > 1) ? (pageNumber - 1) : 1;
		int nextPage = (pageNumber < pageCount) ? (pageNumber + 1) : pageCount;
		// 上一页
		if (pageNumber == 1) {
			out.write("<li class=\"disabled\"><span>&laquo;</span></li>");
		} else {
			out.write("<li><a href=\"" + generatePaginationUrl(prevPage, params) + "\" title=\"上一页\">&laquo;</a></li>");
		}
		if (pageCount > 5) {
			miniPaginator(out, pageNumber, pageCount, params);
		} else {
			for (int i = 1; i <= pageCount; i++) {
				if (pageNumber == i) {
					out.write("<li class=\"active\"><span>" + i + "</span></li>");
				} else {
					out.write("<li><a href=\"" + generatePaginationUrl(i, params) + "\">" + i + "</a></li>");
				}
			}
		}
		// 下一页
		if (pageNumber == pageCount) {
			out.write("<li class=\"disabled\"><span>&raquo;</span></li>");
		} else {
			out.write("<li><a href=\"" + generatePaginationUrl(nextPage, params) + "\" title=\"下一页\">&raquo;</a></li>");
		}
		out.write("</ul>");
		out.write("</nav");
	}

	/**
	 * mini分页方法
	 *
	 * @param out
	 * @param pageNumber
	 * @param pageCount
	 * @param params
	 * @throws IOException
	 */
	private void miniPaginator(Writer out, int pageNumber, int pageCount, Map<String, String> params)
			throws IOException {
		List<Integer> list = new ArrayList<Integer>();
		int mid = pageCount / 2;
		// 前页
		int limit = pageNumber < mid ? 3 : 2;
		for (int i = 1; i <= limit; i++) {
			list.add(i);
		}
		// 中页
		int start = pageNumber == pageCount ? pageNumber - 2 : pageNumber - 1;
		for (int i = start; i <= pageNumber; i++) {
			if (i < 1 || i > pageCount) {
				continue;
			}
			if (!list.contains(i)) {
				list.add(i);
			}
		}
		// 后页
		if (pageNumber != pageCount) {
			for (int i = pageCount - 1; i <= pageCount; i++) {
				if (i < 1 || i > pageCount) {
					continue;
				}
				if (!list.contains(i)) {
					list.add(i);
				}
			}
		}
		for (int i = 0, length = list.size(); i < length; i++) {
			int curPage = list.get(i);
			if (pageNumber == curPage) {
				out.write("<li class=\"active\"><span>" + curPage + "</span></li>");
			} else {
				out.write("<li><a href=\"" + generatePaginationUrl(curPage, params) + "\">" + curPage + "</a></li>");
			}
			if (i != length - 1) {
				int nextPage = list.get(i + 1);
				if (curPage != nextPage - 1) {
					out.write("<li class=\"disabled\"><span>...</span><li>");
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getParameters() {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> urlParamsMap = pageContext.getRequest().getParameterMap();
		if (urlParamsMap != null) {
			for (Map.Entry<String, String[]> entry : urlParamsMap.entrySet()) {
				params.put(entry.getKey(), entry.getValue()[0]);
			}
		}
		return params;
	}

	private String generatePaginationUrl(Integer pageNumber, Map<String, String> params) {
		params.put(Page.PAGE_NUMBER, String.valueOf(pageNumber));
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		StringBuilder builder = new StringBuilder(100);
		builder.append(request.getContextPath());
		builder.append(request.getAttribute(Page.ORIGINAL_SERVLET_PATH));
		builder.append("?");
		for (Map.Entry<String, String> entry : params.entrySet()) {
			try {
				builder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"))
						.append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return builder.subSequence(0, builder.length() - 1).toString();
	}
}
