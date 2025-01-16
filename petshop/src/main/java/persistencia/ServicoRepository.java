package persistencia;

import dominio.Servico;

import java.util.ArrayList;
import java.util.List;

public class ServicoRepository {
    private List<Servico> servicos = new ArrayList<>();

    public void adicionarServico(Servico servico) {
        servicos.add(servico);
    }

    public List<Servico> listarServicos() {
        return servicos;
    }
}