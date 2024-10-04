package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;
    
    @PostMapping
    public ResponseEntity cadastrarMedico(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
        var medico = new Medico(dados);
        repository.save(medico);

         var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri(); // criar o URI com id do medico

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico)); // res 201 created tem que seguir esses paarams
    }

    // get cm paginação, Page = List com paginação, precisa passar a pag como param.

    @GetMapping
    public ResponseEntity<Page<MedicoList>> obterMedicos(@PageableDefault(size = 10, sort = {"nome"}) Pageable pagina){
       var page =  repository.findAll(pagina).map(MedicoList::new);
       return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoList> obterMedicoPorID(@PathVariable Long id){
       var medico = repository.findById(id);
       if(medico.isPresent()) {
           var medicoList = new MedicoList(medico.get());
           return ResponseEntity.ok(medicoList);
       }
       return ResponseEntity.notFound().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarMedico(@RequestBody @Valid DadosAtualizarMedico dados) {
        var medico = repository.findById(dados.id());
       if(medico.isPresent()){
           medico.get().atualizarInformacoes(dados);
           return ResponseEntity.ok(new DadosDetalhamentoMedico(medico.get()));
       }
       return null;
    }

    @DeleteMapping("/{id}") //pathvariable indica que ta vindo do path da url
    @Transactional
    public ResponseEntity deleteMedico(@PathVariable Long id){
        repository.deleteById(id); // facil assim deletar alguem no db
        return ResponseEntity.noContent().build();
    }
}
