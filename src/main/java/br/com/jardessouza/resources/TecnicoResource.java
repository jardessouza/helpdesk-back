package br.com.jardessouza.resources;

import br.com.jardessouza.domain.Tecnico;
import br.com.jardessouza.domain.dtos.TecnicoRequest;
import br.com.jardessouza.domain.dtos.TecnicoResponse;
import br.com.jardessouza.resources.documentation.TecnicoResourceDocs;
import br.com.jardessouza.service.TecnicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/tecnicos")
@RequiredArgsConstructor
public class TecnicoResource implements TecnicoResourceDocs {

    private final TecnicoService tenicoService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<TecnicoResponse> findById(@PathVariable Integer id){
        var tecnico = this.tenicoService.findById(id);
        return ResponseEntity.ok(TecnicoResponse.toDTO(tecnico));
    }

    @GetMapping
    public ResponseEntity<List<TecnicoResponse>> listAll(){
        var listAllTecnicos = this.tenicoService.listAll().stream()
                .map(TecnicoResponse::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(listAllTecnicos);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TecnicoResponse> create(@RequestBody @Valid TecnicoRequest request){
        var obj = this.tenicoService.create(Tecnico.toModel(request));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid TecnicoRequest request){
        this.tenicoService.update(id, Tecnico.toModel(request));
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        this.tenicoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
