package com.wenxt.docprint.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wenxt.docprint.model.LjmRepWhere;

@Repository
public interface LjmRepwhereRepo extends JpaRepository<LjmRepWhere, Long> {

}
