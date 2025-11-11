package biblioteca.app;

import biblioteca.exception.EntidadeNaoEncontradaException;
import biblioteca.exception.ValidacaoException;
import biblioteca.model.*; // Importa Livro, Categoria, Usuario, Aluno, Professor, Funcionario
import biblioteca.repository.*; // Importa Repositorio e as implementações em arquivo

// NOVO: Imports para Empréstimo, Reserva e Datas
import biblioteca.model.Emprestimo;
import biblioteca.model.Reserva;
import java.time.LocalDate;
// FIM NOVO

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BibliotecaApp {

    // Repositórios (camada de dados da Etapa 2)
    private static final Repositorio<Livro> livroRepo = new LivroRepositorioArquivo();
    private static final Repositorio<Usuario> usuarioRepo = new UsuarioRepositorioArquivo();
    private static final Repositorio<Categoria> categoriaRepo = new CategoriaRepositorioArquivo();
    
    // NOVO: Repositórios para Empréstimo e Reserva
    private static final Repositorio<Emprestimo> emprestimoRepo = new EmprestimoRepositorioArquivo();
    private static final Repositorio<Reserva> reservaRepo = new ReservaRepositorioArquivo();
    // FIM NOVO

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
                        gerenciarCategorias();
                        break;
                    case 4:
                        // ATUALIZADO: Chamando o método real
                        gerenciarEmprestimos();
                        break;
                    case 5:
                        // ATUALIZADO: Chamando o método real
                        gerenciarReservas();
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
        System.out.println("4. Gerenciar Empréstimos"); // ATUALIZADO
        System.out.println("5. Gerenciar Reservas");    // ATUALIZADO
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    // ... (Os métodos gerenciarLivros, gerenciarUsuarios e gerenciarCategorias permanecem os mesmos) ...
    // ... (Vou omiti-los aqui para economizar espaço, mas eles devem permanecer no seu arquivo) ...
    
    // -----------------------------------------------------------------
    // GERENCIAMENTO DE LIVROS (Métodos omitidos por brevidade)
    // -----------------------------------------------------------------
     private static void gerenciarLivros() { /* ... Seu código existente ... */ }
     private static void adicionarLivro() throws ValidacaoException { /* ... Seu código existente ... */ }
     private static void listarLivros() { /* ... Seu código existente ... */ }
     private static void buscarLivroPorId() throws EntidadeNaoEncontradaException { /* ... Seu código existente ... */ }
     private static void atualizarLivro() throws EntidadeNaoEncontradaException, ValidacaoException { /* ... Seu código existente ... */ }
     private static void removerLivro() throws EntidadeNaoEncontradaException { /* ... Seu código existente ... */ }

    // -----------------------------------------------------------------
    // GERENCIAMENTO DE USUÁRIOS (Métodos omitidos por brevidade)
    // -----------------------------------------------------------------
     private static void gerenciarUsuarios() { /* ... Seu código existente ... */ }
     private static void adicionarUsuario() throws ValidacaoException { /* ... Seu código existente ... */ }
     private static void listarUsuarios() { /* ... Seu código existente ... */ }
     private static void buscarUsuarioPorId() throws EntidadeNaoEncontradaException { /* ... Seu código existente ... */ }
     private static void atualizarUsuario() throws EntidadeNaoEncontradaException, ValidacaoException { /* ... Seu código existente ... */ }
     private static void removerUsuario() throws EntidadeNaoEncontradaException { /* ... Seu código existente ... */ }

    // -----------------------------------------------------------------
    // GERENCIAMENTO DE CATEGORIAS (Métodos omitidos por brevidade)
    // -----------------------------------------------------------------
     private static void gerenciarCategorias() { /* ... Seu código existente ... */ }
     private static void adicionarCategoria() throws ValidacaoException { /* ... Seu código existente ... */ }
     private static void listarCategorias() { /* ... Seu código existente ... */ }
     private static void buscarCategoriaPorId() throws EntidadeNaoEncontradaException { /* ... Seu código existente ... */ }
     private static void atualizarCategoria() throws EntidadeNaoEncontradaException, ValidacaoException { /* ... Seu código existente ... */ }
     private static void removerCategoria() throws EntidadeNaoEncontradaException { /* ... Seu código existente ... */ }


    // -----------------------------------------------------------------
    // NOVO: GERENCIAMENTO DE EMPRÉSTIMOS
    // -----------------------------------------------------------------

    private static void gerenciarEmprestimos() {
        while (true) {
            System.out.println("\n--- Gerenciar Empréstimos ---");
            System.out.println("1. Realizar Empréstimo");
            System.out.println("2. Realizar Devolução (Concluir Empréstimo)");
            System.out.println("3. Listar Empréstimos Ativos");
            System.out.println("4. Listar Histórico Completo");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = lerOpcao();
                switch (opcao) {
                    case 1:
                        realizarEmprestimo();
                        break;
                    case 2:
                        realizarDevolucao();
                        break;
                    case 3:
                        listarEmprestimosAtivos();
                        break;
                    case 4:
                        listarTodosEmprestimos();
                        break;
                    case 0:
                        return; // Volta ao menu principal
                    default:
                        System.err.println("Opção inválida.");
                }
            } catch (ValidacaoException | EntidadeNaoEncontradaException e) {
                System.err.println("Erro: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Erro inesperado: " + e.getMessage());
            }
        }
    }

    private static void realizarEmprestimo() throws ValidacaoException, EntidadeNaoEncontradaException {
        System.out.println("Realizar Novo Empréstimo:");
        int idLivro = lerInt("ID do Livro: ");
        
        // Lógica de Negócio (App): Verifica se o livro existe e está disponível
        Livro livro = livroRepo.buscaPorId(idLivro);
        if (livro.getStatus() == Livro.Status.EMPRESTADO) {
            throw new ValidacaoException("Livro com ID " + idLivro + " já está emprestado.");
        }

        int idUsuario = lerInt("ID do Usuário: ");
        // Verifica se o usuário existe (automaticamente lança EntidadeNaoEncontradaException se falhar)
        usuarioRepo.buscaPorId(idUsuario); 

        int idEmprestimo = lerInt("ID do novo Empréstimo: ");
        LocalDate dataEmprestimo = LocalDate.now();
        LocalDate dataPrevista = dataEmprestimo.plusDays(7); // Regra: 7 dias

        Emprestimo novoEmprestimo = new Emprestimo(idEmprestimo, idLivro, idUsuario, dataEmprestimo, dataPrevista);
        
        // 1. Adiciona o empréstimo
        emprestimoRepo.adicionar(novoEmprestimo);
        
        // 2. Atualiza o status do livro
        livro.setStatus(Livro.Status.EMPRESTADO);
        livroRepo.atualizar(livro);

        System.out.println("Empréstimo realizado com sucesso! Devolução prevista para: " + dataPrevista);
    }

    private static void realizarDevolucao() throws EntidadeNaoEncontradaException, ValidacaoException {
        System.out.println("Realizar Devolução:");
        int idEmprestimo = lerInt("ID do Empréstimo a ser concluído: ");

        Emprestimo emprestimo = emprestimoRepo.buscaPorId(idEmprestimo);
        
        if (emprestimo.getStatus() == Emprestimo.Status.CONCLUIDO) {
            throw new ValidacaoException("Este empréstimo já foi concluído.");
        }

        // 1. Atualiza o empréstimo
        emprestimo.setStatus(Emprestimo.Status.CONCLUIDO);
        emprestimo.setDataDevolucaoReal(LocalDate.now());
        emprestimoRepo.atualizar(emprestimo);

        // 2. Atualiza o status do livro
        try {
            Livro livro = livroRepo.buscaPorId(emprestimo.getIdLivro());
            livro.setStatus(Livro.Status.DISPONIVEL);
            livroRepo.atualizar(livro);
        } catch (EntidadeNaoEncontradaException e) {
             System.err.println("Aviso: O livro associado (ID: " + emprestimo.getIdLivro() + ") a este empréstimo não foi encontrado, mas a devolução foi registrada.");
        }

        System.out.println("Devolução do livro (ID: " + emprestimo.getIdLivro() + ") registrada com sucesso.");
    }

    private static void listarEmprestimosAtivos() {
        System.out.println("Listando empréstimos ativos:");
        List<Emprestimo> ativos = emprestimoRepo.listaTodos().stream()
                .filter(e -> e.getStatus() == Emprestimo.Status.ATIVO)
                .collect(Collectors.toList());
        
        if (ativos.isEmpty()) {
            System.out.println("Nenhum empréstimo ativo no momento.");
        } else {
            ativos.forEach(System.out::println);
        }
    }

    private static void listarTodosEmprestimos() {
        System.out.println("Histórico de todos os empréstimos:");
        List<Emprestimo> todos = emprestimoRepo.listaTodos();
        if (todos.isEmpty()) {
            System.out.println("Nenhum empréstimo registrado.");
        } else {
            todos.forEach(System.out::println);
        }
    }

    // -----------------------------------------------------------------
    // NOVO: GERENCIAMENTO DE RESERVAS
    // -----------------------------------------------------------------

    private static void gerenciarReservas() {
         while (true) {
            System.out.println("\n--- Gerenciar Reservas ---");
            System.out.println("1. Criar Reserva");
            System.out.println("2. Cancelar Reserva");
            System.out.println("3. Listar Reservas Pendentes");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = lerOpcao();
                switch (opcao) {
                    case 1:
                        criarReserva();
                        break;
                    case 2:
                        cancelarReserva();
                        break;
                    case 3:
                        listarReservasPendentes();
                        break;
                    case 0:
                        return;
                    default:
                        System.err.println("Opção inválida.");
                }
            } catch (ValidacaoException | EntidadeNaoEncontradaException e) {
                System.err.println("Erro: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Erro inesperado: " + e.getMessage());
            }
        }
    }

    private static void criarReserva() throws ValidacaoException, EntidadeNaoEncontradaException {
        System.out.println("Criar Nova Reserva:");
        int idLivro = lerInt("ID do Livro: ");
        
        // Lógica de Negócio (App): Só pode reservar se estiver EMPRESTADO
        Livro livro = livroRepo.buscaPorId(idLivro);
        if (livro.getStatus() == Livro.Status.DISPONIVEL) {
            throw new ValidacaoException("Não é possível reservar um livro que está DISPONÍVEL.");
        }

        int idUsuario = lerInt("ID do Usuário: ");
        usuarioRepo.buscaPorId(idUsuario); // Valida usuário

        int idReserva = lerInt("ID da nova Reserva: ");

        Reserva novaReserva = new Reserva(idReserva, idLivro, idUsuario, LocalDate.now());
        reservaRepo.adicionar(novaReserva);

        System.out.println("Reserva criada com sucesso!");
    }

    private static void cancelarReserva() throws EntidadeNaoEncontradaException {
        System.out.println("Cancelar Reserva:");
        int idReserva = lerInt("ID da Reserva a ser cancelada: ");
        
        // Valida se existe antes de remover
        reservaRepo.buscaPorId(idReserva);
        
        reservaRepo.remover(idReserva);
        System.out.println("Reserva cancelada com sucesso.");
        
        // Opcional: Poderíamos mudar o status para CANCELADA em vez de remover
        /*
        Reserva r = reservaRepo.buscaPorId(idReserva);
        r.setStatus(Reserva.Status.CANCELADA);
        reservaRepo.atualizar(r);
        */
    }

    private static void listarReservasPendentes() {
        System.out.println("Listando reservas pendentes:");
        List<Reserva> pendentes = reservaRepo.listaTodos().stream()
                .filter(r -> r.getStatus() == Reserva.Status.PENDENTE)
                .collect(Collectors.toList());
        
        if (pendentes.isEmpty()) {
            System.out.println("Nenhuma reserva pendente.");
        } else {
            pendentes.forEach(System.out::println);
        }
    }


    // -----------------------------------------------------------------
    // MÉTODOS UTILITÁRIOS (Helpers) - (sem alterações)
    // -----------------------------------------------------------------

    private static int lerOpcao() {
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consome o "Enter" (nova linha)
        return opcao;
    }

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

    private static String lerString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}