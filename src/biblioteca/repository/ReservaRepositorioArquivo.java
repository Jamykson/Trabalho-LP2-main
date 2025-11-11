package biblioteca.repository;

import biblioteca.exception.EntidadeNaoEncontradaException;
import biblioteca.exception.ValidacaoException;
import biblioteca.model.Reserva;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaRepositorioArquivo implements Repositorio<Reserva> {

    private static final String FILE_PATH = "data/reservas.csv";

    public ReservaRepositorioArquivo() {
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

    private Reserva deCsv(String linhaCsv) {
        String[] campos = linhaCsv.split(";");
        if (campos.length < 5) return null;

        int id = Integer.parseInt(campos[0]);
        int idLivro = Integer.parseInt(campos[1]);
        int idUsuario = Integer.parseInt(campos[2]);
        LocalDate dataReserva = LocalDate.parse(campos[3]);
        Reserva.Status status = Reserva.Status.valueOf(campos[4]);

        Reserva reserva = new Reserva(id, idLivro, idUsuario, dataReserva);
        reserva.setStatus(status);
        return reserva;
    }

    private String paraCsv(Reserva reserva) {
        return reserva.getId() + ";" +
               reserva.getIdLivro() + ";" +
               reserva.getIdUsuario() + ";" +
               reserva.getDataReserva().toString() + ";" +
               reserva.getStatus().name() + "\n";
    }

    @Override
    public void adicionar(Reserva reserva) throws ValidacaoException {
        try {
            buscaPorId(reserva.getId());
            throw new ValidacaoException("Já existe uma reserva com o ID " + reserva.getId());
        } catch (EntidadeNaoEncontradaException e) {
            // OK
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(paraCsv(reserva));
        } catch (IOException e) {
            System.err.println("Erro ao adicionar reserva no arquivo: " + e.getMessage());
        }
    }

    @Override
    public Reserva buscaPorId(int id) throws EntidadeNaoEncontradaException {
        for (Reserva r : listaTodos()) {
            if (r.getId() == id) {
                return r;
            }
        }
        throw new EntidadeNaoEncontradaException("Reserva com ID " + id + " não encontrada.");
    }

    @Override
    public List<Reserva> listaTodos() {
        List<Reserva> reservas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Reserva r = deCsv(linha);
                if (r != null) {
                    reservas.add(r);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de reservas: " + e.getMessage());
        }
        return reservas;
    }

    @Override
    public void atualizar(Reserva entidade) throws EntidadeNaoEncontradaException, ValidacaoException {
        List<Reserva> reservas = listaTodos();
        boolean encontrou = false;
        
        for (int i = 0; i < reservas.size(); i++) {
            if (reservas.get(i).getId() == entidade.getId()) {
                reservas.set(i, entidade);
                encontrou = true;
                break;
            }
        }

        if (!encontrou) {
            throw new EntidadeNaoEncontradaException("Reserva com ID " + entidade.getId() + " não encontrada para atualizar.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Reserva r : reservas) {
                writer.write(paraCsv(r));
            }
        } catch (IOException e) {
            System.err.println("Erro ao atualizar o arquivo de reservas: " + e.getMessage());
        }
    }

    @Override
    public void remover(int id) throws EntidadeNaoEncontradaException {
        List<Reserva> reservas = listaTodos();
        boolean removeu = reservas.removeIf(r -> r.getId() == id);

        if (!removeu) {
            throw new EntidadeNaoEncontradaException("Reserva com ID " + id + " não encontrada para remover.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Reserva r : reservas) {
                writer.write(paraCsv(r));
            }
        } catch (IOException e) {
            System.err.println("Erro ao re-escrever o arquivo de reservas: " + e.getMessage());
        }
    }
}