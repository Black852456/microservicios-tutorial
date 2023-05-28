package com.moto.service.repositorio;

import com.moto.service.entidades.Moto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotoRepository extends JpaRepository<Moto,Integer> {

    @Query(value="SELECT * FROM moto WHERE usuario_id = ?1",nativeQuery = true)
    List<Moto> findByUsuario(int usuarioId);

}
