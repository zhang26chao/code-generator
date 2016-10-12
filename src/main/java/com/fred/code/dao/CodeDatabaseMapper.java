package com.fred.code.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.fred.code.domain.CodeDatabase;

@Mapper
public interface CodeDatabaseMapper {

	List<CodeDatabase> pageList(Map<String, Object> params);

	int insert(CodeDatabase codeDatabase);

	int update(CodeDatabase codeDatabase);

	int deleteById(Long id);

	CodeDatabase getById(Long id);

}
