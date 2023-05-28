package com.carro.service.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.carro.service.entidad.Carro;

import java.util.List;

@Repository
public interface CarroRepository extends JpaRepository<Carro,Integer> {

    @Query(value="SELECT * FROM carro WHERE usuario_id = ?1",nativeQuery = true)
    List<Carro> findByUsuario(int usuarioId);
}
