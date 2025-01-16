package controller;

import dominio.Pet;
import dominio.Servico;
import dominio.Agendamento;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseConnection;

import java.time.Instant;
import java.time.ZoneId;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class GerenciamentoController {

    //Campos do Pet
    @FXML
    private TextField campo_cadastro_nome;
    @FXML
    private TextField campo_cadastro_especie;
    @FXML
    private TextField campo_cadastro_idade;
    @FXML
    private Button botao_cadastrar_pet;
    @FXML
    private Button botao_atualizar_pet;
    @FXML
    private Button botao_excluir_pet;
    @FXML
    private TableView<Pet> tabela_lista_pet;
    @FXML
    private TableColumn<Pet, Integer> clid_listapet;
    @FXML
    private TableColumn<Pet, String> clnome_listapet;
    @FXML
    private TableColumn<Pet, String> clespecie_listapet;
    @FXML
    private TableColumn<Pet, Integer> clidade_listapet;

    private List<Pet> pets = new ArrayList<>();
    private Pet petSelecionado;

    // Campos do Serviço
    @FXML
    private TextField campo_descricao_rgservicos;
    @FXML
    private TextField campo_preco_rgservicos;
    @FXML
    private Button botao_cadastrar_servico;
    @FXML
    private Button botao_editar_servico;
    @FXML
    private Button botao_excluir_servico;
    @FXML
    private TableView<Servico> tabela_lista_servicos;
    @FXML
    private TableColumn<Servico, String> cldescricao_servicos;
    @FXML
    private TableColumn<Servico, Double> clpreco_servicos;

    private List<Servico> servicos = new ArrayList<>();
    private Servico servicoSelecionado;

    // Campos do Agendamento
    @FXML
    private DatePicker campo_data_agendamento;
    @FXML
    private TextField campo_hora_agendamento;
    @FXML
    private ComboBox<Pet> campo_pet_agendamento;
    @FXML
    private ComboBox<Servico> campo_servico_agendamento;
    @FXML
    private Button butao_adicionar_agendamento;
    @FXML
    private Button butao_editar_agendamento;
    @FXML
    private Button butao_excluir_agendamento;
    @FXML
    private TableView<Agendamento> tabela_lista_agendamento;
    @FXML
    private TableColumn<Agendamento, Integer> clid_agendamento;
    @FXML
    private TableColumn<Agendamento, LocalDate> cldata_agendamento;
    @FXML
    private TableColumn<Agendamento, String> clhora_agendamento;
    @FXML
    private TableColumn<Agendamento, String> clpet_agendamento;
    @FXML
    private TableColumn<Agendamento, String> clservico_agendamento;

    private List<Agendamento> agendamentos = new ArrayList<>();
    private Agendamento agendamentoSelecionado;

    @FXML
    public void initialize() {
        // Configuração da tabela de Pets
        clid_listapet.setCellValueFactory(new PropertyValueFactory<>("id"));
        clnome_listapet.setCellValueFactory(new PropertyValueFactory<>("nome"));
        clidade_listapet.setCellValueFactory(new PropertyValueFactory<>("idade"));
        clespecie_listapet.setCellValueFactory(new PropertyValueFactory<>("especie"));

        tabela_lista_pet.setOnMouseClicked(event -> {
            petSelecionado = tabela_lista_pet.getSelectionModel().getSelectedItem();
            if (petSelecionado != null) {
                campo_cadastro_nome.setText(petSelecionado.getNome());
                campo_cadastro_especie.setText(petSelecionado.getEspecie());
                campo_cadastro_idade.setText(String.valueOf(petSelecionado.getIdade()));
            }
        });

        // Configuração da tabela de Serviços
        cldescricao_servicos.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        clpreco_servicos.setCellValueFactory(new PropertyValueFactory<>("preco"));

        tabela_lista_servicos.setOnMouseClicked(event -> {
            servicoSelecionado = tabela_lista_servicos.getSelectionModel().getSelectedItem();
            if (servicoSelecionado != null) {
                campo_descricao_rgservicos.setText(servicoSelecionado.getDescricao());
                campo_preco_rgservicos.setText(String.valueOf(servicoSelecionado.getPreco()));
            }
        });

        // Configuração da tabela de Agendamentos
        clid_agendamento.setCellValueFactory(new PropertyValueFactory<>("id"));
        cldata_agendamento.setCellValueFactory(new PropertyValueFactory<>("data"));
        clhora_agendamento.setCellValueFactory(new PropertyValueFactory<>("hora"));
        clpet_agendamento.setCellValueFactory(new PropertyValueFactory<>("petNome"));
        clservico_agendamento.setCellValueFactory(new PropertyValueFactory<>("servicoNome"));

        tabela_lista_agendamento.setOnMouseClicked(event -> {
            agendamentoSelecionado = tabela_lista_agendamento.getSelectionModel().getSelectedItem();
            if (agendamentoSelecionado != null) {
                campo_data_agendamento.setValue(agendamentoSelecionado.getData());
                campo_hora_agendamento.setText(agendamentoSelecionado.getHora());
                campo_pet_agendamento.setValue(agendamentoSelecionado.getPet());
                campo_servico_agendamento.setValue(agendamentoSelecionado.getServico());
            }
        });

        // Carregar os pets do banco de dados
        carregarPetsDoBanco();

        // Carregar os Srviços do banco de dados
        carregarServicosDoBanco();

        // Carregar os Agendamentos do banco de dados
       carregarAgendamentosDoBanco();

        // Preencher ComboBox de Pets
        campo_pet_agendamento.getItems().addAll(pets);

        // Preencher ComboBox de Serviços
        campo_servico_agendamento.getItems().addAll(servicos);

        // Atualizar os ComboBoxes quando inicializa
        atualizarComboBoxPets();
        atualizarComboBoxServicos();
    }

    private void atualizarComboBoxPets() {
        campo_pet_agendamento.getItems().clear();
        campo_pet_agendamento.getItems().addAll(pets);
    }

    private void atualizarComboBoxServicos() {
        campo_servico_agendamento.getItems().clear();
        campo_servico_agendamento.getItems().addAll(servicos);
    }

    private void carregarPetsDoBanco() {
        pets.clear();

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM pet";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int idade = rs.getInt("idade");
                String especie = rs.getString("especie");
                Pet pet = new Pet(id, nome, idade, especie);
                pets.add(pet);
            }
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar pets do banco: " + e.getMessage());
        }

        atualizarTabela();
    }

    @FXML
    public void adicionarPet() {
        String nome = campo_cadastro_nome.getText();
        String especie = campo_cadastro_especie.getText();
        String idadeStr = campo_cadastro_idade.getText();

        // Validar campos
        if (nome.isEmpty() || especie.isEmpty() || idadeStr.isEmpty()) {
            mostrarAlerta("Todos os campos devem ser preenchidos.");
            return;
        }

        int idade = Integer.parseInt(idadeStr);

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "INSERT INTO pet (nome, idade, especie) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nome);
            pstmt.setInt(2, idade);
            pstmt.setString(3, especie);
            pstmt.executeUpdate();

            carregarPetsDoBanco();
            limparCampos();

            // Atualiza o Combobox dos Pets assim que adicionar o novo
            atualizarComboBoxPets();

        } catch (SQLException e) {
            mostrarAlerta("Erro ao adicionar pet: " + e.getMessage());
        }
    }

    @FXML
    public void atualizarPet() {
        if (petSelecionado == null) {
            mostrarAlerta("Selecione um pet na tabela.");
            return;
        }

        String nome = campo_cadastro_nome.getText();
        String especie = campo_cadastro_especie.getText();
        String idadeStr = campo_cadastro_idade.getText();

        // Validar campos
        if (nome.isEmpty() || especie.isEmpty() || idadeStr.isEmpty()) {
            mostrarAlerta("Todos os campos devem ser preenchidos.");
            return;
        }

        int idade = Integer.parseInt(idadeStr);
        int id = petSelecionado.getId();

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "UPDATE pet SET nome = ?, idade = ?, especie = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nome);
            pstmt.setInt(2, idade);
            pstmt.setString(3, especie);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();

            carregarPetsDoBanco();
            limparCampos();

            // Atualiza o Combobox dos Pets assim que atualizar um novo
            atualizarComboBoxPets();

        } catch (SQLException e) {
            mostrarAlerta("Erro ao atualizar pet: " + e.getMessage());
        }
    }

    @FXML
    public void excluirPet() {
        if (petSelecionado == null) {
            mostrarAlerta("Selecione um pet na tabela.");
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "DELETE FROM pet WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, petSelecionado.getId());
            pstmt.executeUpdate();

            carregarPetsDoBanco();
            limparCampos();

            // Atualiza o Combobox dos Pets assim que excluir
            atualizarComboBoxPets();

            petSelecionado = null;
        } catch (SQLException e) {
            mostrarAlerta("Erro ao excluir pet: " + e.getMessage());
        }
    }

    // Atualizar a tabela
    private void atualizarTabela() {
        tabela_lista_pet.getItems().clear();
        tabela_lista_pet.getItems().addAll(pets);
    }

    // Limpar campos
    private void limparCampos() {
        campo_cadastro_nome.clear();
        campo_cadastro_especie.clear();
        campo_cadastro_idade.clear();
        petSelecionado = null; // Limpa a seleção ao limpar campos
    }

    // Mostrar alertas
    private void mostrarAlerta(String mensagem) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Informação");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    // Métodos do CRUD de Serviços
    private void carregarServicosDoBanco() {
        servicos.clear();

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM servico";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String descricao = rs.getString("descricao");
                double preco = rs.getDouble("preco");
                Servico servico = new Servico(id, descricao, preco);
                servicos.add(servico);
            }
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar serviços do banco: " + e.getMessage());
        }

        atualizarTabelaServicos();
    }

    @FXML
    public void adicionarServico() {
        String descricao = campo_descricao_rgservicos.getText();
        String precoStr = campo_preco_rgservicos.getText();

        if (descricao.isEmpty() || precoStr.isEmpty()) {
             mostrarAlerta("Todos os campos devem ser preenchidos.");
            return;
        }

        double preco = Double.parseDouble(precoStr);

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "INSERT INTO servico (descricao, preco) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, descricao);
            pstmt.setDouble(2, preco);
            pstmt.executeUpdate();

            carregarServicosDoBanco();
            limparCamposServicos();

            // Atualiza o Combobox dos Serviços assim que adicionar
            atualizarComboBoxServicos();

        } catch (SQLException e) {
            mostrarAlerta("Erro ao adicionar serviço: " + e.getMessage());
        }
    }

    @FXML
    public void editarServico() {

        String descricao = campo_descricao_rgservicos.getText();
        String precoStr = campo_preco_rgservicos.getText();

        if (descricao.isEmpty() || precoStr.isEmpty()) {
             mostrarAlerta("Todos os campos devem ser preenchidos.");
            return;
        }

        double preco = Double.parseDouble(precoStr);
        int id = servicoSelecionado.getId();

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "UPDATE servico SET descricao = ?, preco = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, descricao);
            pstmt.setDouble(2, preco);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();

            carregarServicosDoBanco();
            limparCamposServicos();

            // Atualiza o Combobox dos Serviços assim que atualizar o serviço
            atualizarComboBoxServicos();

        } catch (SQLException e) {
            mostrarAlerta("Erro ao atualizar serviço: " + e.getMessage());
        }
    }

    @FXML
    public void excluirServico() {

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "DELETE FROM servico WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, servicoSelecionado.getId());
            pstmt.executeUpdate();

            carregarServicosDoBanco();
            limparCamposServicos();

            // Atualiza o Combobox dos Serviços assim que excluir o serviço
            atualizarComboBoxServicos();

        } catch (SQLException e) {
            mostrarAlerta("Erro ao excluir serviço: " + e.getMessage());
        }

        servicoSelecionado = null;
    }

    private void atualizarTabelaServicos() {
        tabela_lista_servicos.getItems().clear();
        tabela_lista_servicos.getItems().addAll(servicos);
    }

    private void limparCamposServicos() {
        campo_descricao_rgservicos.clear();
        campo_preco_rgservicos.clear();
        servicoSelecionado = null;
    }

    // Consulta para auxiliar o método de agendamento
    private Pet buscarPetPorId(int id) {
        Pet pet = null;

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM pet WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int petId = rs.getInt("id");
                String nome = rs.getString("nome");
                int idade = rs.getInt("idade");
                String especie = rs.getString("especie");
                pet = new Pet(petId, nome, idade, especie);
            }
        } catch (SQLException e) {
            mostrarAlerta("Erro ao buscar pet: " + e.getMessage());
        }

        return pet;
    }

    private Servico buscarServicoPorId(int id) {
        Servico servico = null;

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM servico WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int servicoId = rs.getInt("id");
                String descricao = rs.getString("descricao");
                double preco = rs.getDouble("preco");
                servico = new Servico(servicoId, descricao, preco);
            }
        } catch (SQLException e) {
            mostrarAlerta("Erro ao buscar serviço: " + e.getMessage());
        }

        return servico;
    }

    // Métodos do CRUD de Agendamento
    private void carregarAgendamentosDoBanco() {
        agendamentos.clear();

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM agendamento";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int idAgendamento = rs.getInt("id");
                long timestamp = rs.getLong("data");
                LocalDate data = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
                String hora = rs.getString("hora");
                int petId = rs.getInt("petId");
                int servicoId = rs.getInt("servicoId");

                Pet pet = buscarPetPorId(petId);
                Servico servico = buscarServicoPorId(servicoId);

                Agendamento agendamento = new Agendamento(idAgendamento,data, hora, pet, servico);
                agendamentos.add(agendamento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao carregar agendamentos do banco: " + e.getMessage());
        }

        atualizarTabelaAgendamentos();
    }

    @FXML
    public void adicionarAgendamento() {
        LocalDate data = campo_data_agendamento.getValue();
        String hora = campo_hora_agendamento.getText();
        Pet pet = campo_pet_agendamento.getValue();
        Servico servico = campo_servico_agendamento.getValue();

        if (data == null || hora.isEmpty() || pet == null || servico == null) {
            mostrarAlerta("Todos os campos devem ser preenchidos.");
            return;
        }

        long timestamp = data.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "INSERT INTO agendamento (data, hora, petId, servicoId) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, timestamp);
            pstmt.setString(2, hora);
            pstmt.setInt(3, pet.getId());
            pstmt.setInt(4, servico.getId());
            pstmt.executeUpdate();

            carregarAgendamentosDoBanco();
            limparCamposAgendamento();
            mostrarAlerta("Agendamento cadastrado com sucesso!");
        } catch (SQLException e) {
            mostrarAlerta("Erro ao adicionar agendamento: " + e.getMessage());
        }
    }

    @FXML
    public void editarAgendamento() {
        if (agendamentoSelecionado == null) {
            mostrarAlerta("Selecione um agendamento na tabela.");
            return;
        }

        LocalDate data = campo_data_agendamento.getValue();
        String hora = campo_hora_agendamento.getText();
        Pet pet = campo_pet_agendamento.getValue();
        Servico servico = campo_servico_agendamento.getValue();

        if (data == null || hora.isEmpty() || pet == null || servico == null) {
            mostrarAlerta("Todos os campos devem ser preenchidos.");
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "UPDATE agendamento SET data = ?, hora = ?, petId = ?, servicoId = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, java.sql.Date.valueOf(data));
            pstmt.setString(2, hora);
            pstmt.setInt(3, pet.getId());
            pstmt.setInt(4, servico.getId());
            pstmt.setInt(5, agendamentoSelecionado.getId());
            pstmt.executeUpdate();

            carregarAgendamentosDoBanco();
            limparCamposAgendamento();
            mostrarAlerta("Agendamento atualizado com sucesso!");
        } catch (SQLException e) {
            mostrarAlerta("Erro ao atualizar agendamento: " + e.getMessage());
        }
    }

    @FXML
    public void excluirAgendamento() {
        if (agendamentoSelecionado == null) {
            mostrarAlerta("Selecione um agendamento na tabela.");
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "DELETE FROM agendamento WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, agendamentoSelecionado.getId());
            pstmt.executeUpdate();

            carregarAgendamentosDoBanco();
            limparCamposAgendamento();
            mostrarAlerta("Agendamento excluído com sucesso!");
        } catch (SQLException e) {
            mostrarAlerta("Erro ao excluir agendamento: " + e.getMessage());
        }

        agendamentoSelecionado = null;
    }

    private void atualizarTabelaAgendamentos() {
        tabela_lista_agendamento.getItems().clear();
        tabela_lista_agendamento.getItems().addAll(agendamentos);
    }

    private void limparCamposAgendamento() {
        campo_data_agendamento.setValue(null);
        campo_hora_agendamento.clear();
        campo_pet_agendamento.getSelectionModel().clearSelection();
        campo_servico_agendamento.getSelectionModel().clearSelection();
        agendamentoSelecionado = null;
    }
}

