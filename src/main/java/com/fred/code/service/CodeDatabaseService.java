package com.fred.code.service;

import java.util.List;
import java.util.Map;

import com.fred.code.domain.CodeDatabase;

public interface CodeDatabaseService {

	List<CodeDatabase> pageList(Map<String, Object> params);

	public int insert(CodeDatabase dmo);

	public int update(CodeDatabase dmo);

	public int deleteById(Long id);

	public CodeDatabase getById(Long id);

}