package edu.fatec.exame.model;

import java.time.LocalDate;

public class Exame {

    // id começa em 0 pq ainda não foi salvo no banco
    private long id = 0;

    // tipo de exame: sangue, raio-x, ultrassom, etc
    private String tipo = "";

    // data em que o exame foi feito
    private LocalDate dataRealizacao = LocalDate.now();

    // o que o exame mostrou (normal, alterado, etc)
    private String resultado = "";

    // observacoes extras que o medico queira registrar
    private String observacao = "";

    // nome do paciente que fez o exame
    private String nomePaciente = "";

    private String status = "";

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDate getDataRealizacao() { return dataRealizacao; }
    public void setDataRealizacao(LocalDate dataRealizacao) { this.dataRealizacao = dataRealizacao; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public String getNomePaciente() { return nomePaciente; }
    public void setNomePaciente(String nomePaciente) { this.nomePaciente = nomePaciente; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // usado em logs pra identificar o exame rapidamente
    @Override
    public String toString() {
        return tipo + " - " + nomePaciente;
    }
}
