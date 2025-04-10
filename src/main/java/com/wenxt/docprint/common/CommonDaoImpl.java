package com.wenxt.docprint.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wenxt.docprint.dms.model.QUERY_MASTER;

@Repository
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
	
	@Override
	public List<LOVDTO> executeLOVQuery(String query, Map<String, Object> paramsList) {
		if (paramsList == null || paramsList.size() <= 0) {
			List<LOVDTO> result = template.query(query, new BeanPropertyRowMapper<>(LOVDTO.class));
			return result;
		} else {
			List<LOVDTO> result = namedTemplate.query(query, paramsList, new BeanPropertyRowMapper<>(LOVDTO.class));
			return result;
		}
	}
	
	@Override
	public List<QueryParamMasterDTO> getQueryParams(int sysId) {
		String sql = "SELECT * FROM LJM_QUERY_PARAM_MASTER WHERE QPM_QM_SYS_ID = ? order by QPM_PARAM_TYPE desc";
		List<QueryParamMasterDTO> result = template.query(sql, new Object[] { sysId },
				new BeanPropertyRowMapper<>(QueryParamMasterDTO.class));
		return result;
	}

}
