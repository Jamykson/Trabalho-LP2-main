package biblioteca.repository;

import biblioteca.exception.EntidadeNaoEncontradaException;
import biblioteca.exception.ValidacaoException;
import biblioteca.model.Emprestimo;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoRepositorioArquivo implements Repositorio<Emprestimo> {

    private static final String FILE_PATH = "data/emprestimos.csv";

    public EmprestimoRepositorioArquivo() {
        criarArquivoSeNaoExistir();
    }

    private void criarArquivoSeNaoExistir() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo: " + e.getMessage());
        }
    }

    // Converte linha CSV para objeto Emprestimo
    private Emprestimo deCsv(String linhaCsv) {
        String[] campos = linhaCsv.split(";");
        if (campos.length < 6) return null;

        int id = Integer.parseInt(campos[0]);
        int idLivro = Integer.parseInt(campos[1]);
        int idUsuario = Integer.parseInt(campos[2]);
        LocalDate dataEmprestimo = LocalDate.parse(campos[3]);
        LocalDate dataDevolucaoPrevista = LocalDate.parse(campos[4]);
        
        // Trata data de devolução nula
        String dataDevolucaoRealStr = campos[5];
        LocalDate dataDevolucaoReal = (dataDevolucaoRealStr == null || dataDevolucaoRealStr.isEmpty()) ? null : LocalDate.parse(dataDevolucaoRealStr);
        
        Emprestimo.Status status = Emprestimo.Status.valueOf(campos[6]);

        Emprestimo emprestimo = new Emprestimo(id, idLivro, idUsuario, dataEmprestimo, dataDevolucaoPrevista);
        emprestimo.setDataDevolucaoReal(dataDevolucaoReal);
        emprestimo.setStatus(status);

        return emprestimo;
    }

    // Converte objeto Emprestimo para linha CSV
    private String paraCsv(Emprestimo emprestimo) {
        // Trata data de devolução nula
        String dataDevolucaoRealStr = (emprestimo.getDataDevolucaoReal() == null) ? "" : emprestimo.getDataDevolucaoReal().toString();

        return emprestimo.getId() + ";" +
               emprestimo.getIdLivro() + ";" +
               emprestimo.getIdUsuario() + ";" +
               emprestimo.getDataEmprestimo().toString() + ";" +
               emprestimo.getDataDevolucaoPrevista().toString() + ";" +
               dataDevolucaoRealStr + ";" +
               emprestimo.getStatus().name() + "\n";
    }

    @Override
    public void adicionar(Emprestimo emprestimo) throws ValidacaoException {
        try {
            buscaPorId(emprestimo.getId());
            throw new ValidacaoException("Já existe um empréstimo com o ID " + emprestimo.getId());
        } catch (EntidadeNaoEncontradaException e) {
            // ID não existe, pode adicionar
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(paraCsv(emprestimo));
        } catch (IOException e) {
            System.err.println("Erro ao adicionar empréstimo no arquivo: " + e.getMessage());
        }
    }

    @Override
    public Emprestimo buscaPorId(int id) throws EntidadeNaoEncontradaException {
        for (Emprestimo e : listaTodos()) {
            if (e.getId() == id) {
                return e;
            }
        }
        throw new EntidadeNaoEncontradaException("Empréstimo com ID " + id + " não encontrado.");
    }

    @Override
    public List<Emprestimo> listaTodos() {
        List<Emprestimo> emprestimos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Emprestimo e = deCsv(linha);
                if (e != null) {
                    emprestimos.add(e);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de empréstimos: " + e.getMessage());
        }
        return emprestimos;
    }

    @Override
    public void atualizar(Emprestimo entidade) throws EntidadeNaoEncontradaException, ValidacaoException {
        List<Emprestimo> emprestimos = listaTodos();
        boolean encontrou = false;
        
        for (int i = 0; i < emprestimos.size(); i++) {
            if (emprestimos.get(i).getId() == entidade.getId()) {
                emprestimos.set(i, entidade);
                encontrou = true;
                break;
            }
        }

        if (!encontrou) {
            throw new EntidadeNaoEncontradaException("Empréstimo com ID " + entidade.getId() + " não encontrado para atualizar.");
        }

        // Re-escreve o arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Emprestimo e : emprestimos) {
                writer.write(paraCsv(e));
            }
        } catch (IOException e) {
            System.err.println("Erro ao atualizar o arquivo de empréstimos: " + e.getMessage());
        }
    }

    @Override
    public void remover(int id) throws EntidadeNaoEncontradaException {
        List<Emprestimo> emprestimos = listaTodos();
        boolean removeu = emprestimos.removeIf(e -> e.getId() == id);

        if (!removeu) {
            throw new EntidadeNaoEncontradaException("Empréstimo com ID " + id + " não encontrado para remover.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Emprestimo e : emprestimos) {
                writer.write(paraCsv(e));
            }
        } catch (IOException e) {
            System.err.println("Erro ao re-escrever o arquivo de empréstimos: " + e.getMessage());
        }
    }
}