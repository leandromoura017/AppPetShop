package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInit {

    private static final String URL = "jdbc:sqlite:mydatabase.db";

    public static void init() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                // Tabela Cliente
                String createClienteTable = "CREATE TABLE IF NOT EXISTS cliente (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "usuario TEXT NOT NULL, " +
                        "senha TEXT NOT NULL" +
                        ");";
                stmt.execute(createClienteTable);

                // Tabela Pet
                String createPetTable = "CREATE TABLE IF NOT EXISTS pet (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nome TEXT NOT NULL, " +
                        "idade INTEGER NOT NULL, " +
                        "especie TEXT NOT NULL, " +
                        "clienteId INTEGER, " +
                        "FOREIGN KEY (clienteId) REFERENCES cliente(id) ON DELETE CASCADE" +
                        ");";
                stmt.execute(createPetTable);

                // Tabela Serviço
                String createServicoTable = "CREATE TABLE IF NOT EXISTS servico (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "descricao TEXT NOT NULL, " +
                        "preco REAL NOT NULL" +
                        ");";
                stmt.execute(createServicoTable);

                // Tabela Agendamento
                String createAgendamentoTable = "CREATE TABLE IF NOT EXISTS agendamento (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "data INTEGER, " +
                        "hora TEXT NOT NULL, " +
                        "petId INTEGER, " +
                        "servicoId INTEGER, " +
                        "FOREIGN KEY (petId) REFERENCES pet(id) ON DELETE CASCADE, " +
                        "FOREIGN KEY (servicoId) REFERENCES servico(id) ON DELETE CASCADE" +
                        ");";
                stmt.execute(createAgendamentoTable);

                System.out.println("Banco de dados e tabelas criados com sucesso.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }
}
