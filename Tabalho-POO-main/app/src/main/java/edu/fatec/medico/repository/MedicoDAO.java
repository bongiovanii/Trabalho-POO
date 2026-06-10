package edu.fatec.medico.repository;

import java.util.List;

import edu.fatec.medico.model.Medico;

public interface MedicoDAO {
    void inserir(Medico m);
    void atualizar(long id, Medico m);
    void excluir(Medico m);
    List<Medico> pesquisarPorNome(String nome);
}
