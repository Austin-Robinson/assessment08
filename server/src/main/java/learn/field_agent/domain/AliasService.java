package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AliasService {
    private final AliasRepository repo;

    public AliasService(AliasRepository repo){
        this.repo = repo;
    }

    public List<Alias> findAliasesByAgent(int agentId){
        return repo.findByAgentId(agentId);
    }

    public Result<Alias> add(Alias alias){
        Result<Alias> result = validate(alias);
        if(!result.isSuccess()){
            return result;
        }
        result.setPayload(repo.add(alias));
        return result;
    }

    public Result<Alias> update(Alias alias){
        Result<Alias> result = validate(alias);
        if(!result.isSuccess()){
            return result;
        }
        boolean isSuccess = repo.update(alias);
        if(isSuccess) {
            result.setPayload(repo.findById(alias.getAliasId()));
        }
        else{
            result.addMessage("Alias to update not found", ResultType.NOT_FOUND);
        }
        return result;
    }

    public Result<Alias> delete(int aliasId) {
        Result<Alias> result = new Result<>();
        Alias temp = repo.findById(aliasId);
        if (temp == null) {
            result.addMessage("Alias to Delete Not Found", ResultType.NOT_FOUND);
            return result;
        }
        boolean isSuccess = repo.delete(aliasId);
        if (isSuccess) {
            result.setPayload(temp);
        } else {
            result.addMessage("Deletion failed", ResultType.NOT_FOUND);
        }
        return result;
    }
        private Result<Alias> validate(Alias alias){
        Result<Alias> result = new Result<>();
        ResultType invalid = ResultType.INVALID;
        if(alias == null){
            result.addMessage("Alias cannot be null", invalid);
            return result;
        }
        if(alias.getName() == null || alias.getName().isBlank() || alias.getName().isBlank()){
            result.addMessage("Alias name cannot be left blank", invalid);
            return result;
        }
        List<Alias> all = repo.findAll();
        for (Alias a : all){
            if(a.getName().equalsIgnoreCase(alias.getName())){
                if(alias.getPersona() == null || alias.getPersona().isBlank() || alias.getPersona().isEmpty()){
                    result.addMessage("Persona cannot be left blank since there is a duplication with Alias name",invalid);
                    return result;
                }
            }
        }
        for(Alias a : all){
            if(a.getPersona() != null && alias.getPersona() != null){
                if (a.getName().equalsIgnoreCase(alias.getName()) && a.getPersona().equalsIgnoreCase(alias.getPersona())) {
                    result.addMessage("Alias is a duplicate", invalid);
                    return result;
                }else if(a.getPersona() == null && alias.getPersona() == null){
                    if (a.getName().equals(alias.getName()) ) {
                        result.addMessage("Alias is a duplicate", invalid);
                        return result;
                }
            }
        }
        }
        return result;
    }

}
