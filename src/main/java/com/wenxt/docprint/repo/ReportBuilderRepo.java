package com.wenxt.docprint.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wenxt.docprint.model.LjmReportBuilder;

@Repository
public interface ReportBuilderRepo extends JpaRepository<LjmReportBuilder, Integer> {

}
