package com.wenxt.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.wenxt.docprint.dms.model.QUERY_MASTER;

public class CommonDaoImpl implements CommonDao {
	
	@Autowired
	private JdbcTemplate template;

	@Autowired
	private NamedParameterJdbcTemplate namedTemplate;

	@Override
	public QUERY_MASTER getQueryLov(Integer queryId) {
		try {
			String sql = "SELECT * FROM LJM_QUERY_MASTER WHERE QM_SYS_ID = ?";
			QUERY_MASTER result = template.queryForObject(sql, new Object[] { queryId },
					new BeanPropertyRowMapper<>(QUERY_MASTER.class));
			return result;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> getMapQuery(String query, Map<String, Object> queryParams) {
		String sql = query;

		List<Map<String, Object>> rows = namedTemplate.queryForList(sql, queryParams);

		return rows;
	}

}
