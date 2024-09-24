package com.wenxt.docprint.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wenxt.docprint.model.LjmCommonRepConfig;

@Repository
public interface LjmCommonRepConfigRepo extends JpaRepository<LjmCommonRepConfig, Long> {

}
