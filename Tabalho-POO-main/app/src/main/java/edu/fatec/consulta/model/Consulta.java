package edu.fatec.consulta.model;

import java.time.LocalDate;

public class Consulta {

    private long id = 0;

    private String nomeMedico = "";

    private String nomePaciente = "";

    private LocalDate dataConsulta = LocalDate.now();

    private String diagnostico = "";

    private String status = "";

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNomeMedico() { return nomeMedico; }
    public void setNomeMedico(String nomeMedico) { this.nomeMedico = nomeMedico; }

    public String getNomePaciente() { return nomePaciente; }
    public void setNomePaciente(String nomePaciente) { this.nomePaciente = nomePaciente; }

    public LocalDate getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDate dataConsulta) { this.dataConsulta = dataConsulta; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // util pra identificar a consulta em logs
    @Override
    public String toString() {
        return nomePaciente + " com " + nomeMedico + " (" + status + ")";
    }
}

