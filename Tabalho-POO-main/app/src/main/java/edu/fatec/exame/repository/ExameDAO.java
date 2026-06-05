package edu.fatec.exame.repository;

import java.util.List;

import edu.fatec.exame.model.Exame;

/**
 * Interface DAO (Data Access Object) para a entidade Exame.
 *
 * Define o CONTRATO de operações de banco de dados que qualquer
 * implementação de persistência deve cumprir.
 *
 * Separar a interface da implementação é uma boa prática de OOP:
 * se um dia mudar o banco (ex: MySQL para PostgreSQL), basta criar
 * uma nova implementação sem alterar o Control ou a Boundary.
 */
public interface ExameDAO {

    /**
     * Insere um novo exame no banco de dados.
     * @param e objeto Exame com os dados a serem gravados
     */
    void cadastrar(Exame e);

    /**
     * Remove um exame do banco de dados pelo seu ID.
     * @param e objeto Exame a ser removido (usa o ID internamente)
     */
    void apagar(Exame e);

    /**
     * Atualiza os dados de um exame já existente no banco.
     * @param id identificador do registro a ser atualizado
     * @param e  objeto Exame com os novos dados
     */
    void atualizar(long id, Exame e);

    /**
     * Busca exames cujo tipo contenha o texto informado.
     * Passando string vazia ("") retorna todos os exames.
     * @param tipo texto para filtrar pelo tipo do exame
     * @return lista de exames encontrados
     */
    List<Exame> pesquisarPorTipo(String tipo);
}
