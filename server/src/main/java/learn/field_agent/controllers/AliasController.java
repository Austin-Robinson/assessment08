package learn.field_agent.controllers;

import learn.field_agent.domain.AliasService;
import learn.field_agent.domain.Result;
import learn.field_agent.models.Alias;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/alias")
public class AliasController {
    private final AliasService service;

    public AliasController(AliasService service){
        this.service = service;
    }

    @GetMapping("/{agentId}")
    public List<Alias> findAllForAgent(@PathVariable int agentId) {
        return service.findAliasesByAgent(agentId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Alias alias) {
        System.out.println("Called!!!!!!!!!!!1");
        System.out.println(alias.getName() +" " +alias.getPersona() + " " +alias.getAgentId());
        Result<Alias> result = service.add(alias);
        if (result.isSuccess()) {
            System.out.println("SUCCESS");
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{aliasId}")
    public ResponseEntity<Object> update(@PathVariable int aliasId, @RequestBody Alias alias) {
        if (aliasId != alias.getAliasId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Alias> result = service.update(alias);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{aliasId}")
    public ResponseEntity<Object> deleteById(@PathVariable int aliasId) {
        Result<Alias> result = service.delete(aliasId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

}
