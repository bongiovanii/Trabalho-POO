package edu.fatec.exame.repository;

import java.util.List;

import edu.fatec.exame.model.Exame;

// Interface que define as operacoes de banco pra entidade Exame
// Separar interface da implementacao facilita trocar o banco depois se precisar
public interface ExameDAO {

    // insere um novo exame no banco
    void cadastrar(Exame e);

    // remove um exame do banco
    void apagar(Exame e);

    // atualiza um exame existente no banco
    void atualizar(long id, Exame e);

    // pesquisa exames pelo tipo
    List<Exame> pesquisarPorTipo(String tipo);
}
