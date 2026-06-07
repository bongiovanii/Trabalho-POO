package edu.fatec.consulta.repository;

import java.util.List;

import edu.fatec.consulta.model.Consulta;

// Interface com as operacoes de banco pra entidade Consulta
public interface ConsultaDAO {

    // insere uma nova consulta
    void cadastrar(Consulta consulta);

    // atualiza os dados de uma consulta ja existente
    void atualizar(long id, Consulta consulta);

    // remove uma consulta pelo id
    void apagar(Consulta consulta);

    // busca consultas pelo nome do paciente - passando "" retorna todas
    List<Consulta> pesquisarPorPaciente(String nomePaciente);
}
