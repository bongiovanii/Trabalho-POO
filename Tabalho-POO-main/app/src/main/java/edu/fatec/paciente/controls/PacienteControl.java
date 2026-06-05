package edu.fatec.paciente.controls;

import edu.fatec.paciente.model.Paciente;
import edu.fatec.paciente.repository.PacienteDAO;

public class PacienteControl {
    private PacienteDAO dao = new PacienteDAO();
    public void salvar(Paciente paciente) {
        dao.inserir(paciente);
    }

    public Paciente pesquisar(String nome) {
        return dao.pesquisarPorNome(nome);
    }

    public Paciente pesquisarPorId(Long id) {
        return dao.pesquisarPorId(id);
    }

    public Long obterProximoId() {
        return dao.obterProximoId();
    }

    public void atualizar(Paciente paciente) {
       dao.atualizar(paciente);
    }

    public void deletar(Paciente paciente) {
       dao.deletar(paciente.getId());
    }

}
