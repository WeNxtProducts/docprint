package com.wenxt.docprint.dms.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wenxt.docprint.dms.model.LjmFileAttributes;

@Repository
public interface LjmFileAttributesRepository extends JpaRepository<LjmFileAttributes, Long> {

	Optional<LjmFileAttributes> findByTranId(String tranId);

	// Find all file attributes by author
	List<LjmFileAttributes> findByAuthorContaining(String author);

	// Find all file attributes by docType
	List<LjmFileAttributes> findByDocTypeContaining (String docType);
	
	 // Find all file attributes by fileName
    List<LjmFileAttributes> findByFileNameContaining (String fileName);
    

	List<LjmFileAttributes> findByTranIdContaining(String tranId);
	
	
	// Repository method to find by fileName, docModule, and tranId
	List<LjmFileAttributes> findByFileNameAndDocModuleAndTranId(String fileName, String docModule, String tranId);


}