package med.voll.api.domain.medico;

public record MedicoList(Long id, String nome, String email, String crm, Especialidade especialidade) {


    public MedicoList(Medico medico){
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade());
    }
}
