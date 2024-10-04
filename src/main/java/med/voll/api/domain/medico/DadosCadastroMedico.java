package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroMedico(
        //validação
        @NotBlank // verifica que nao e nulo e n e vazio
        String nome,
        @NotBlank
        String telefone,
        @NotBlank
        @Email
        String email,
        @NotBlank
                @Pattern(regexp = "\\d{4,6}") //verifica se é um DIGITO //d e a quantidade
        String crm,
        @NotNull
        Especialidade especialidade,
        @NotNull
                @Valid // validar o dto de outro dto, entso ele vai verificar o dto de dadosendereco
        DadosEndereco endereco
) {
}
