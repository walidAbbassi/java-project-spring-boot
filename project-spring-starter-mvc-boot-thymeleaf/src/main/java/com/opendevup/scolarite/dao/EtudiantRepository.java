/**
* @author  Walid Abbassi
*/
package com.opendevup.scolarite.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.opendevup.scolarite.entities.Etudiant;

@Transactional
public interface EtudiantRepository extends JpaRepository<Etudiant, Long>{
	
	/**
	* Return the List Etudiant having the passed etudiantName or null if no Etudiant is found.
	* 
	* @param etudiantName the Etudiant Name.
	*/	
	public List<Etudiant> findByNom(String etudiantName);
	
	/**
	* Return pageable the List Etudiant having the passed etudiantName or null if no Etudiant is found.
	* 
	* @param etudiantName the Etudiant Name.
	*/
	public Page<Etudiant> findByNom(String etudiantName, Pageable pageable);

	/**
	* other way to write function findByNom
	* Return pageable the List Etudiant having the passed etudiantName or null if no Etudiant is found.
	* 
	* @param etudiantName the Etudiant Name.
	*/
	@Query("select e from Etudiant e where e.nom like :etudiant_name")
	public Page<Etudiant> chercherEtudiant(@Param("etudiant_name")String etudiantName, Pageable pageable);	
	/**
	* Return pageable the List Etudiant having the passed dateStart and dateEnd or null if no Etudiant is found.
	* 
	* @param dateStart the Etudiant Date.
	* @param dateEnd the Etudiant Date.
	*/
	@Query("select e from Etudiant e where e.dateNaissance >:etudiant_date_naissance_debut and e.dateNaissance <:etudiant_date_naissance_fin")
	public List<Etudiant> chercherEtudiant(@Param("etudiant_date_naissance_debut")Date dateStart, @Param("etudiant_date_naissance_fin")Date dateEnd);
}
