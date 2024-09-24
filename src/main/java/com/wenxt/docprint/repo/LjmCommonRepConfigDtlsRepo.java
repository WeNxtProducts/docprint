package com.wenxt.docprint.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wenxt.docprint.model.LjmCommonRepConfigDtl;

@Repository
public interface LjmCommonRepConfigDtlsRepo extends JpaRepository<LjmCommonRepConfigDtl, Long> {

}
