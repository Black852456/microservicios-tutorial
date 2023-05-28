package com.moto.service.servicios;

import com.moto.service.entidades.Moto;
import com.moto.service.repositorio.MotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoService {

    @Autowired
    private MotoRepository motoRepository;


    public List<Moto> getAll(){
        return motoRepository.findAll();
    }

    public Moto getMotoById(int id){
        return motoRepository.findById(id).orElse(null);
    }

    public Moto save(Moto carro){
        Moto nuevoMoto = motoRepository.save(carro);
        return nuevoMoto;
    }

    public List<Moto> byUsuarioId(int idUsuario){
        return motoRepository.findByUsuario(idUsuario);
    }


}
