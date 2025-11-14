classDiagram
    direction LR

    class Usuario {
        <<Abstract>>
        -id: int
        -nome: String
        -matriculaEmail: String
        -telefone: String
        +Usuario(id, nome, matriculaEmail, telefone)
        +getId() int
        +getNome() String
        +getMatriculaEmail() String
        +getTelefone() String
        +setNome(nome)
        +setMatriculaEmail(matriculaEmail)
        +setTelefone(telefone)
    }

    class Aluno {
        +Aluno(id, nome, matricula, telefone)
    }

    class Professor {
        +Professor(id, nome, email, telefone)
    }

    class Funcionario {
        -cargo: String
        -credenciais: String
        +Funcionario(id, nome, cargo, credenciais)
        +getCargo() String
        +getCredenciais() String
    }

    class Livro {
        -id: int
        -titulo: String
        -autor: String
        -isbn: String
        -ano: int
        -status: StatusLivro
        +Livro(id, titulo, autor, isbn, ano)
        +getStatus() StatusLivro
        +setStatus(status)
    }

    class Categoria {
        -id: int
        -nome: String
        -descricao: String
        +Categoria(id, nome, descricao)
    }

    class Emprestimo {
        -id: int
        -idLivro: int
        -idUsuari: int
        -dataEmprestimo: Date
        -dataDevolucaoPrevista: Date
    }

    class Reserva {
        -id: int
        -idLivro: int
        -idUsuario: int
        -dataReserva: Date
    }

    %% --- Herança ---
    Usuario <|-- Aluno
    Usuario <|-- Professor
    Usuario <|-- Funcionario

    %% --- Enum (Status do Livro) ---
    class StatusLivro {
        <<Enumeration>>
        DISPONIVEL
        EMPRESTADO
    }
    Livro o-- StatusLivro

    %% --- Associações (Exemplos) ---
    Livro "1" -- "0..*" Emprestimo : emprestado por >
    Usuario "1" -- "0..*" Emprestimo : realiza >
    Livro "1" -- "0..*" Reserva : reservado por >
    Usuario "1" -- "0..*" Reserva : faz >
    Categoria "1" -- "0..*" Livro : agrupa >

    %% --- Interface Repositório (Tarefa 2) ---
    class Repositorio {
        <<Interface>>
        +T salvar(T entidade)
        +T buscarPorId(int id)
        +List~T~ listarTodos()
        +void atualizar(T entidade)
        +void deletar(int id)
    }
    class ArquivoRepositorio {
        +T salvar(T entidade)
        +T buscarPorId(int id)
        +List~T~ listarTodos()
        +void atualizar(T entidade)
        +void deletar(int id)
    }
    class MemoriaRepositorio {
        +T salvar(T entidade)
        +T buscarPorId(int id)
        +List~T~ listarTodos()
        +void atualizar(T entidade)
        +void deletar(int id)
    }
    Repositorio <|.. ArquivoRepositorio : implementa
    Repositorio <|.. MemoriaRepositorio : implementa

    %% --- Exceções (Sua Tarefa) ---
    class Exception {
        <<Java Util>>
    }
    class ValidacaoException {
        +ValidacaoException(String mensagem)
    }
    class EntidadeNaoEncontradaException {
        +EntidadeNaoEncontradaException(String mensagem)
    }
    class OperacaoInvalidaException {
        +OperacaoInvalidaException(String mensagem)
    }
    Exception <|-- ValidacaoException
    Exception <|-- EntidadeNaoEncontradaException
    Exception <|-- OperacaoInvalidaException