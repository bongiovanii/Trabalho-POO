package edu.fatec.paciente.repository;
import java.util.List;

import edu.fatec.paciente.model.Paciente;

public interface PacienteDAO {
    // insere um novo paciente no banco
    void inserir(Paciente p);
  
    // atualiza um paciente existente no banco
    void atualizar(long id, Paciente p);
    
    // exclui um paciente do banco
    void excluir(Paciente p);

    // pesquisa um paciente pelo nome
    List<Paciente> pesquisarPorNome(String nome);


    
}
