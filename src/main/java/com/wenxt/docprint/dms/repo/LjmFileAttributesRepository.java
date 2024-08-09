package com.wenxt.docprint.dms.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wenxt.docprint.dms.model.LjmFileAttributes;

@Repository
public interface LjmFileAttributesRepository extends JpaRepository<LjmFileAttributes, Long> {

	Optional<LjmFileAttributes> findByTranId(String tranId);



}