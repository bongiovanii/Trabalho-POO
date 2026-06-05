package edu.fatec.exame.model;

import java.time.LocalDate;

/**
 * Classe de modelo (entidade) que representa um Exame médico.
 * Segue o padrão JavaBean: atributos privados com getters e setters públicos.
 * Cada atributo corresponde a uma coluna na tabela 'exame' do banco de dados.
 */
public class Exame {

    // Identificador único do exame no banco de dados.
    // Começa em 0 para indicar que ainda não foi salvo (sem ID gerado).
    private long id = 0;

    // Tipo do exame (ex: "Sangue", "Raio-X", "Ultrassom").
    private String tipo = "";

    // Data em que o exame foi realizado.
    // Usa LocalDate, que é a classe moderna do Java para datas sem hora.
    private LocalDate dataRealizacao = LocalDate.now();

    // Resultado do exame (ex: "Normal", "Alterado", valor numérico, etc).
    private String resultado = "";

    // Campo livre para observações adicionais do médico ou laboratório.
    private String observacao = "";

    // Nome do paciente que realizou o exame.
    // Armazenado como String simples (sem FK para objeto Paciente),
    // seguindo o padrão adotado no projeto.
    private String nomePaciente = "";

    // --- Getters e Setters ---
    // Necessários para que o JavaFX (via Control) acesse e modifique os dados,
    // e para que o DAO consiga montar o objeto ao ler do banco.

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

    /**
     * Representação textual do exame.
     * Usada em logs e em componentes visuais que exibem o objeto diretamente.
     */
    @Override
    public String toString() {
        return tipo + " - " + nomePaciente;
    }
}
