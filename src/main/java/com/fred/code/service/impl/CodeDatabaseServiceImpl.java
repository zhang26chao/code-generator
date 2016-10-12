package com.fred.code.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fred.code.dao.CodeDatabaseMapper;
import com.fred.code.domain.CodeDatabase;
import com.fred.code.service.CodeDatabaseService;

@Service("CodeDatabaseService")
public class CodeDatabaseServiceImpl implements CodeDatabaseService {

	@Autowired
	private CodeDatabaseMapper mapper;

	@Override
	public List<CodeDatabase> pageList(Map<String, Object> params) {
		return mapper.pageList(params);
	}

	@Override
	public int insert(CodeDatabase dmo) {
		return mapper.insert(dmo);
	}

	@Override
	public int update(CodeDatabase dmo) {
		return mapper.update(dmo);
	}

	@Override
	public int deleteById(Long id) {
		return mapper.deleteById(id);
	}

	@Override
	public CodeDatabase getById(Long id) {
		return mapper.getById(id);
	}

}