package com.example.TP_Foyer.Repository;

import com.example.TP_Foyer.Entity.Foyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoyerRepository extends JpaRepository<Foyer, Long> {

	@Query("select f from Foyer f join f.blocs b join b.chambres c group by f order by count(c) desc")
	List<Foyer> findFoyersOrderByNombreChambresDesc();

}
