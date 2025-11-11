# 📚 Sistema de Gestão de Biblioteca (Trabalho-LP2)

Projeto da disciplina de Programação Orientada a Objetos (POO) / LP2 para criar um sistema de gerenciamento de biblioteca. O sistema é interativo e utiliza um menu de console (CLI) para operar.

**Status do Projeto:** 🚀 Concluído

## ✨ Recursos Implementados

- **Menu Interativo (CLI):** Interface de usuário principal para navegar no sistema e gerenciar entidades.
- **CRUD de Livros:** Funcionalidade completa para Adicionar, Listar, Buscar por ID, Atualizar e Remover livros.
- **CRUD de Usuários (com Polimorfismo):** Gerenciamento completo de `Alunos`, `Professores` e `Funcionários` através da classe `Usuario` abstrata.
- **CRUD de Categorias:** Gerenciamento completo das categorias dos livros.
- **Persistência de Dados em CSV:** Todos os dados de livros, usuários e categorias são lidos e salvos em arquivos `.csv` localizados na pasta `data/`.
- **Tratamento de Exceções:** Uso de exceções personalizadas (`EntidadeNaoEncontradaException`, `ValidacaoException`) para um controle de fluxo robusto.

## 🛠️ Tecnologias e Padrões

- **Java** (JDK 11+)
- **Programação Orientada a Objetos (POO)**
  - **Encapsulamento:** Proteção dos atributos das classes de modelo.
  - **Herança:** `Usuario` (Abstrata) -> `Aluno`, `Professor`, `Funcionario`.
  - **Polimorfismo:** Utilizado na camada de repositório para salvar e carregar os diferentes tipos de usuários.
- **Padrão Repositório (Repository Pattern):** Abstração da camada de dados com a interface genérica `Repositorio<T>`.
- **Persistência em Arquivo:** Estratégia de salvamento de dados usando arquivos de texto (CSV).

## 🚀 Como Executar

Você precisará ter o **JDK (Java Development Kit)** instalado em sua máquina.

### 1. Compilando o Projeto

Abra um terminal na pasta raiz do projeto (`Trabalho-LP2-main/`).

```bash
# Cria o diretório 'out' (se ainda não existir)
mkdir -p out

# Compila todos os arquivos .java e coloca os .class em 'out/'
javac -d out/ -sourcepath src/ src/biblioteca/app/BibliotecaApp.java
```
