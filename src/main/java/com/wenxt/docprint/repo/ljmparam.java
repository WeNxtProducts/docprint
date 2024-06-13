package com.wenxt.docprint.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wenxt.docprint.model.LjmDocprintParam;

@Repository
public interface ljmparam extends JpaRepository<LjmDocprintParam, Long> {

	@Query("SELECT p FROM LjmDocprintParam p WHERE p.LjmDocprintSetup.DPS_SYSID = :DPS_SYSID")
	List<LjmDocprintParam> findByDppDpsSysid(@Param("DPS_SYSID") Long dpsSysid);
}
