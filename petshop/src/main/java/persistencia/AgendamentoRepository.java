package persistencia;

import dominio.Agendamento;

import java.util.ArrayList;
import java.util.List;

public class AgendamentoRepository {
    private List<Agendamento> agendamentos = new ArrayList<>();

    public void adicionarAgendamento(Agendamento agendamento) {
        agendamentos.add(agendamento);
    }

    public List<Agendamento> listarAgendamentos() {
        return agendamentos;
    }
}
