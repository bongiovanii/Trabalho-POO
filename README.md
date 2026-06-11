# Gestão de Clínica Médica

## Integrantes
- Guilherme Teixeira Rodrigues
- Gustavo Santos Bongiovani de Oliveira

## Tema Escolhido
Boa Saúde e Bem-Estar (ODS 3)

## Descrição do Problema Resolvido
Sistema desktop de gerenciamento de clínica médica que permite o cadastro e controle de pacientes, médicos, exames e consultas. O sistema facilita o acompanhamento do histórico de atendimentos, organização de exames solicitados e agendamento de consultas, contribuindo para uma gestão mais eficiente e organizada da clínica.

## Entidades Implementadas
- **Paciente** — cadastro de pacientes com dados pessoais e informações de saúde
- **Médico** — cadastro de médicos com CRM, especialidade e contato
- **Exame** — registro de exames médicos com tipo, data, resultado e status
- **Consulta** — registro de consultas com médico, paciente, diagnóstico e status

## Instruções para Execução

### Pré-requisitos
- Java 21 (BellSoft Liberica Full)
- MySQL rodando localmente na porta 3306
- Gradle

### Passos
1. Inicie o serviço do MySQL
2. Execute o arquivo `schema.sql` para criar o banco de dados e as tabelas
3. Na pasta do projeto, execute:
```bash
./gradlew run
```

## Divisão de Responsabilidades

| Integrante | Responsabilidades |
|---|---|
| Guilherme Teixeira Rodrigues | Entidades Exame e Consulta (model, controls, repository, view) |
| Gustavo Santos Bongiovani de Oliveira | Entidades Paciente e Médico (model, controls, repository, view), tela principal e menu de navegação |

## Link do Vídeo
https://youtu.be/MZ7R2mM8t10
