package com.wenxt.docprint.dms.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wenxt.docprint.dms.model.LMCodes;

@Repository
public interface lmcodesRepo extends JpaRepository<LMCodes, String> {
	
	 @Query("SELECT l.pcDesc FROM LMCodes l WHERE l.pcType = :screenName AND l.pcCode = :uploadscrn")
	    Optional<String> findPcDescByPcTypeAndPcCode(@Param("screenName") String pcType, @Param("uploadscrn") String pcCode);

}


