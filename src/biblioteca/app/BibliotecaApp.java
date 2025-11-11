package biblioteca.app;

import biblioteca.exception.EntidadeNaoEncontradaException;
import biblioteca.exception.ValidacaoException;
import biblioteca.model.*; // Importa Livro, Categoria, Usuario, Aluno, Professor, Funcionario
import biblioteca.repository.*; // Importa Repositorio e as implementações em arquivo

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BibliotecaApp {

    // Repositórios (camada de dados da Etapa 2)
    // Estamos usando as implementações de Arquivo, conforme a proposta
    private static final Repositorio<Livro> livroRepo = new LivroRepositorioArquivo();
    private static final Repositorio<Usuario> usuarioRepo = new UsuarioRepositorioArquivo();
    private static final Repositorio<Categoria> categoriaRepo = new CategoriaRepositorioArquivo();

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("SISTEMA DE GESTÃO DE BIBLIOTECA - INICIADO");

        while (true) {
            exibirMenuPrincipal();
            try {
                int opcao = lerOpcao();
                switch (opcao) {
                    case 1:
                        gerenciarLivros();
                        break;
                    case 2:
                        gerenciarUsuarios();
                        break;
                    case 3:
                        gerenciarCategorias(); // Adicionado (baseado nos repositórios da Etapa 2)
                        break;
                    case 4:
                        System.out.println("(!) Módulo 'Gerenciar Empréstimos' ainda não implementado.");
                        // TODO: Chamar gerenciarEmprestimos();
                        break;
                    case 5:
                        System.out.println("(!) Módulo 'Gerenciar Reservas' ainda não implementado.");
                        // TODO: Chamar gerenciarReservas();
                        break;
                    case 0:
                        System.out.println("Encerrando o sistema...");
                        scanner.close();
                        return;
                    default:
                        System.err.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Erro: Entrada inválida. Por favor, digite um número.");
                scanner.nextLine(); // Limpa o buffer do scanner
            } catch (Exception e) {
                System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace(); // Útil para depuração
            }
        }
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Gerenciar Livros");
        System.out.println("2. Gerenciar Usuários");
        System.out.println("3. Gerenciar Categorias");
        System.out.println("4. Gerenciar Empréstimos (Não implementado)");
        System.out.println("5. Gerenciar Reservas (Não implementado)");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    // -----------------------------------------------------------------
    // GERENCIAMENTO DE LIVROS
    // -----------------------------------------------------------------

    private static void gerenciarLivros() {
        while (true) {
            System.out.println("\n--- Gerenciar Livros [CRUD] ---");
            System.out.println("1. Adicionar Livro");
            System.out.println("2. Listar Todos os Livros");
            System.out.println("3. Buscar Livro por ID");
            System.out.println("4. Atualizar Livro");
            System.out.println("5. Remover Livro");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = lerOpcao();
                switch (opcao) {
                    case 1:
                        adicionarLivro();
                        break;
                    case 2:
                        listarLivros();
                        break;
                    case 3:
                        buscarLivroPorId();
                        break;
                    case 4:
                        atualizarLivro();
                        break;
                    case 5:
                        removerLivro();
                        break;
                    case 0:
                        return; // Volta ao menu principal
                    default:
                        System.err.println("Opção inválida.");
                }
            } catch (ValidacaoException | EntidadeNaoEncontradaException e) {
                // Tratamento amigável de exceções (Etapa 1)
                System.err.println("Erro: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println("Erro: Entrada inválida. Use números.");
                scanner.nextLine();
            } catch (Exception e) {
                System.err.println("Erro inesperado: " + e.getMessage());
            }
        }
    }

    private static void adicionarLivro() throws ValidacaoException {
        System.out.println("Adicionar Novo Livro:");
        int id = lerInt("ID: ");
        String titulo = lerString("Título: ");
        String autor = lerString("Autor: ");
        String isbn = lerString("ISBN: ");
        int ano = lerInt("Ano: ");
        
        System.out.println("Status (1: DISPONIVEL, 2: EMPRESTADO): ");
        Livro.Status status = (lerOpcao() == 2) ? Livro.Status.EMPRESTADO : Livro.Status.DISPONIVEL;

        Livro novoLivro = new Livro(id, titulo, autor, isbn, ano, status);
        livroRepo.adicionar(novoLivro);
        System.out.println("Livro adicionado com sucesso!");
    }

    private static void listarLivros() {
        System.out.println("Listando todos os livros:");
        List<Livro> livros = livroRepo.listaTodos();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    private static void buscarLivroPorId() throws EntidadeNaoEncontradaException {
        int id = lerInt("ID do livro a buscar: ");
        Livro livro = livroRepo.buscaPorId(id);
        System.out.println("Livro encontrado: " + livro);
    }

    private static void atualizarLivro() throws EntidadeNaoEncontradaException, ValidacaoException {
        int id = lerInt("ID do livro a atualizar: ");
        Livro livroExistente = livroRepo.buscaPorId(id); // Valida se existe

        System.out.println("Digite os novos dados (deixe em branco para manter o atual):");
        
        String titulo = lerString("Título (" + livroExistente.getTitulo() + "): ");
        String autor = lerString("Autor (" + livroExistente.getAutor() + "): ");
        String isbn = lerString("ISBN (" + livroExistente.getIsbn() + "): ");
        String anoStr = lerString("Ano (" + livroExistente.getAno() + "): ");
        String statusStr = lerString("Status (1: DISPONIVEL, 2: EMPRESTADO) (" + livroExistente.getStatus() + "): ");

        // Atualiza o objeto
        if (!titulo.isEmpty()) livroExistente.setTitulo(titulo);
        if (!autor.isEmpty()) livroExistente.setAutor(autor);
        if (!isbn.isEmpty()) livroExistente.setIsbn(isbn);
        if (!anoStr.isEmpty()) livroExistente.setAno(Integer.parseInt(anoStr));
        if (!statusStr.isEmpty()) {
            livroExistente.setStatus((statusStr.equals("2")) ? Livro.Status.EMPRESTADO : Livro.Status.DISPONIVEL);
        }

        livroRepo.atualizar(livroExistente);
        System.out.println("Livro atualizado com sucesso!");
    }

    private static void removerLivro() throws EntidadeNaoEncontradaException {
        int id = lerInt("ID do livro a remover: ");
        livroRepo.remover(id); // O repositório já trata a exceção se não encontrar
        System.out.println("Livro removido com sucesso!");
    }


    // -----------------------------------------------------------------
    // GERENCIAMENTO DE USUÁRIOS (com Polimorfismo)
    // -----------------------------------------------------------------

    private static void gerenciarUsuarios() {
        while (true) {
            System.out.println("\n--- Gerenciar Usuários [CRUD] ---");
            System.out.println("1. Adicionar Usuário (Aluno, Professor, Funcionário)");
            System.out.println("2. Listar Todos os Usuários");
            System.out.println("3. Buscar Usuário por ID");
            System.out.println("4. Atualizar Usuário");
            System.out.println("5. Remover Usuário");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = lerOpcao();
                switch (opcao) {
                    case 1:
                        adicionarUsuario();
                        break;
                    case 2:
                        listarUsuarios();
                        break;
                    case 3:
                        buscarUsuarioPorId();
                        break;
                    case 4:
                        atualizarUsuario();
                        break;
                    case 5:
                        removerUsuario();
                        break;
                    case 0:
                        return; // Volta ao menu principal
                    default:
                        System.err.println("Opção inválida.");
                }
            } catch (ValidacaoException | EntidadeNaoEncontradaException e) {
                System.err.println("Erro: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println("Erro: Entrada inválida. Use números.");
                scanner.nextLine();
            } catch (Exception e) {
                System.err.println("Erro inesperado: " + e.getMessage());
            }
        }
    }

    private static void adicionarUsuario() throws ValidacaoException {
        System.out.println("Tipo de Usuário (1: Aluno, 2: Professor, 3: Funcionário):");
        int tipo = lerOpcao();

        int id = lerInt("ID: ");
        String nome = lerString("Nome: ");
        String email = lerString("Email: ");
        String telefone = lerString("Telefone: ");

        Usuario novoUsuario;

        switch (tipo) {
            case 1: // Aluno
                String matricula = lerString("Matrícula: ");
                novoUsuario = new Aluno(id, nome, email, telefone, matricula);
                break;
            case 2: // Professor
                String siape = lerString("SIAPE: ");
                novoUsuario = new Professor(id, nome, email, telefone, siape);
                break;
            case 3: // Funcionário
                String cargo = lerString("Cargo: ");
                novoUsuario = new Funcionario(id, nome, email, telefone, cargo);
                break;
            default:
                System.err.println("Tipo inválido, cancelando operação.");
                return;
        }

        usuarioRepo.adicionar(novoUsuario); // Polimorfismo: adiciona Aluno/Prof/Func como Usuario
        System.out.println("Usuário adicionado com sucesso!");
    }

    private static void listarUsuarios() {
        System.out.println("Listando todos os usuários:");
        List<Usuario> usuarios = usuarioRepo.listaTodos();
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            // O polimorfismo do `toString()` de Aluno, Professor e Funcionario
            // é ativado aqui e no repositório de arquivo.
            usuarios.forEach(System.out::println);
        }
    }

    private static void buscarUsuarioPorId() throws EntidadeNaoEncontradaException {
        int id = lerInt("ID do usuário a buscar: ");
        Usuario u = usuarioRepo.buscaPorId(id);
        System.out.println("Usuário encontrado: " + u);
    }

    private static void atualizarUsuario() throws EntidadeNaoEncontradaException, ValidacaoException {
        int id = lerInt("ID do usuário a atualizar: ");
        Usuario usuario = usuarioRepo.buscaPorId(id); // Valida se existe

        System.out.println("Digite os novos dados (deixe em branco para manter o atual):");
        
        String nome = lerString("Nome (" + usuario.getNome() + "): ");
        String email = lerString("Email (" + usuario.getEmail() + "): ");
        String telefone = lerString("Telefone (" + usuario.getTelefone() + "): ");

        // Atualiza campos comuns
        if (!nome.isEmpty()) usuario.setNome(nome);
        if (!email.isEmpty()) usuario.setEmail(email);
        if (!telefone.isEmpty()) usuario.setTelefone(telefone);

        // Atualiza campos específicos (usando 'instanceof' como na Etapa 2)
        if (usuario instanceof Aluno) {
            String matricula = lerString("Matrícula (" + ((Aluno) usuario).getMatricula() + "): ");
            if (!matricula.isEmpty()) ((Aluno) usuario).setMatricula(matricula);
        } else if (usuario instanceof Professor) {
            String siape = lerString("SIAPE (" + ((Professor) usuario).getSiape() + "): ");
            if (!siape.isEmpty()) ((Professor) usuario).setSiape(siape);
        } else if (usuario instanceof Funcionario) {
            String cargo = lerString("Cargo (" + ((Funcionario) usuario).getCargo() + "): ");
            if (!cargo.isEmpty()) ((Funcionario) usuario).setCargo(cargo);
        }

        usuarioRepo.atualizar(usuario);
        System.out.println("Usuário atualizado com sucesso!");
    }

    private static void removerUsuario() throws EntidadeNaoEncontradaException {
        int id = lerInt("ID do usuário a remover: ");
        usuarioRepo.remover(id);
        System.out.println("Usuário removido com sucesso!");
    }

    // -----------------------------------------------------------------
    // GERENCIAMENTO DE CATEGORIAS (Bônus, baseado na Etapa 2)
    // -----------------------------------------------------------------
    
    private static void gerenciarCategorias() {
        while (true) {
            System.out.println("\n--- Gerenciar Categorias [CRUD] ---");
            System.out.println("1. Adicionar Categoria");
            System.out.println("2. Listar Todas as Categorias");
            System.out.println("3. Buscar Categoria por ID");
            System.out.println("4. Atualizar Categoria");
            System.out.println("5. Remover Categoria");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = lerOpcao();
                switch (opcao) {
                    case 1:
                        adicionarCategoria();
                        break;
                    case 2:
                        listarCategorias();
                        break;
                    case 3:
                        buscarCategoriaPorId();
                        break;
                    case 4:
                        atualizarCategoria();
                        break;
                    case 5:
                        removerCategoria();
                        break;
                    case 0:
                        return; // Volta ao menu principal
                    default:
                        System.err.println("Opção inválida.");
                }
            } catch (ValidacaoException | EntidadeNaoEncontradaException e) {
                System.err.println("Erro: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println("Erro: Entrada inválida. Use números.");
                scanner.nextLine();
            } catch (Exception e) {
                System.err.println("Erro inesperado: " + e.getMessage());
            }
        }
    }

    private static void adicionarCategoria() throws ValidacaoException {
        int id = lerInt("ID: ");
        String nome = lerString("Nome: ");
        String descricao = lerString("Descrição: ");
        categoriaRepo.adicionar(new Categoria(id, nome, descricao));
        System.out.println("Categoria adicionada com sucesso!");
    }

    private static void listarCategorias() {
        System.out.println("Listando todas as categorias:");
        List<Categoria> categorias = categoriaRepo.listaTodos();
        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada.");
        } else {
            categorias.forEach(System.out::println);
        }
    }

     private static void buscarCategoriaPorId() throws EntidadeNaoEncontradaException {
        int id = lerInt("ID da categoria a buscar: ");
        Categoria c = categoriaRepo.buscaPorId(id);
        System.out.println("Categoria encontrada: " + c);
    }

    private static void atualizarCategoria() throws EntidadeNaoEncontradaException, ValidacaoException {
        int id = lerInt("ID da categoria a atualizar: ");
        Categoria cat = categoriaRepo.buscaPorId(id);

        String nome = lerString("Nome (" + cat.getNome() + "): ");
        String descricao = lerString("Descrição (" + cat.getDescricao() + "): ");

        if (!nome.isEmpty()) cat.setNome(nome);
        if (!descricao.isEmpty()) cat.setDescricao(descricao);

        categoriaRepo.atualizar(cat);
        System.out.println("Categoria atualizada com sucesso!");
    }
    
    private static void removerCategoria() throws EntidadeNaoEncontradaException {
        int id = lerInt("ID da categoria a remover: ");
        categoriaRepo.remover(id);
        System.out.println("Categoria removida com sucesso!");
    }


    // -----------------------------------------------------------------
    // MÉTODOS UTILITÁRIOS (Helpers)
    // -----------------------------------------------------------------

    /**
     * Lê um número inteiro do scanner (opção de menu), tratando InputMismatchException.
     */
    private static int lerOpcao() {
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consome o "Enter" (nova linha)
        return opcao;
    }

    /**
     * Lê um número inteiro, exibindo um prompt.
     */
    private static int lerInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                int valor = scanner.nextInt();
                scanner.nextLine(); // Consome o "Enter"
                return valor;
            } catch (InputMismatchException e) {
                System.err.println("Entrada inválida. Digite um número:");
                scanner.nextLine(); // Limpa o buffer
            }
        }
    }

    /**
     * Lê uma string do scanner, exibindo um prompt.
     */
    private static String lerString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}