package dominio;

public class Pet {
    private int id;
    private String nome;
    private int idade;
    private String especie;

    public Pet(int id, String nome, int idade, String especie) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.especie = especie;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public String getEspecie() {
        return especie;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}