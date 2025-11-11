package biblioteca.model;

import java.time.LocalDate;

public class Reserva implements EntidadeBase {

    private int id;
    private int idLivro;
    private int idUsuario;
    private LocalDate dataReserva;
    private Status status;

    public enum Status {
        PENDENTE,
        CANCELADA
    }

    public Reserva() {}

    public Reserva(int id, int idLivro, int idUsuario, LocalDate dataReserva) {
        this.id = id;
        this.idLivro = idLivro;
        this.idUsuario = idUsuario;
        this.dataReserva = dataReserva;
        this.status = Status.PENDENTE;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", idLivro=" + idLivro +
                ", idUsuario=" + idUsuario +
                ", dataReserva=" + dataReserva +
                ", status=" + status +
                '}';
    }

    // Getters e Setters
    @Override
    public int getId() {
        return id;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }
    public int getIdLivro() {
        return idLivro;
    }
    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public LocalDate getDataReserva() {
        return dataReserva;
    }
    public void setDataReserva(LocalDate dataReserva) {
        this.dataReserva = dataReserva;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}