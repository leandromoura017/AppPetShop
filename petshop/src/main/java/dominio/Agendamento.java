package dominio;

import java.time.LocalDate;

public class Agendamento {
    private int id;
    private LocalDate data;
    private String hora;
    private Pet pet;
    private Servico servico;

    public Agendamento(int id, LocalDate data, String hora, Pet pet, Servico servico) {
        this.id = id;
        this.data = data;
        this.hora = hora;
        this.pet = pet;
        this.servico = servico;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public String getPetNome() {
        return pet.getNome();
    }

    public String getServicoNome() {
        return servico.getDescricao();
    }
}