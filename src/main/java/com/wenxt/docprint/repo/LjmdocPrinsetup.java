package com.wenxt.docprint.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wenxt.docprint.dto.TemplateDataDTO;
import com.wenxt.docprint.model.LjmDocprintSetup;

@Repository
public interface LjmdocPrinsetup extends JpaRepository<LjmDocprintSetup, Long> {

	@Query("SELECT new com.wenxt.docprint.dto.TemplateDataDTO(t.DPS_SYSID, t.DPS_TEMPLATE_NAME) FROM LjmDocprintSetup t WHERE t.DPS_SCREEN_NAME = :DPS_SCREEN_NAME")
	List<TemplateDataDTO> findByDspTemplatename(@Param("DPS_SCREEN_NAME") String tempName);

	@Query("SELECT t.DPS_TEMP_LOC FROM LjmDocprintSetup t WHERE t.DPS_TEMPLATE_NAME = :templateName")
    Optional<String> findFileLocationByTemplateName(@Param("templateName") String templateName);
}