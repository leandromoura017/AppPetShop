package dominio;

public class Cliente {
    private String usuario;
    private String senha;

    public Cliente(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getUsuario() { return usuario; }
    public String getSenha() { return senha; }
}