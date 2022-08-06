package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityClearanceService {
    private final SecurityClearanceRepository repo;

    public SecurityClearanceService(SecurityClearanceRepository repo){
        this.repo = repo;
    }

    public List<SecurityClearance> findAll(){
        return repo.findAll();
    }

    public Result<SecurityClearance> findById(int securityId){
        Result<SecurityClearance>result = new Result<>();
        SecurityClearance sc = repo.findById(securityId);
        if(sc == null){
            result.addMessage("Unable to Find Security Clearance with ID provided",ResultType.NOT_FOUND);
        }
        else {
            result.setPayload(sc);
        }
        return result;
    }

    public Result<SecurityClearance> add(SecurityClearance toAdd){
        Result<SecurityClearance> result = validate(toAdd);
        if(result.isSuccess()) {
            result.setPayload(repo.add(toAdd));
        }

        return result;
    }

    public Result<SecurityClearance> update(SecurityClearance toUpdate){
        Result<SecurityClearance> result = validate(toUpdate);
        boolean success = false;
        if(result.isSuccess()){
            success = repo.update(toUpdate);
        }
        else return result;
        if(!success){
            result.addMessage("Unable to update Security Clearance",ResultType.NOT_FOUND);
        }
        else {
            result.setPayload(repo.findById(toUpdate.getSecurityClearanceId()));
        }
        return result;
    }

    public Result<SecurityClearance> delete(int toDelete){
        Result<SecurityClearance> result = new Result<>();
        SecurityClearance found = findById(toDelete).getPayload();
        if(found == null){
             result.addMessage("Security Clearance to delete not found",ResultType.NOT_FOUND);
             return result;
        }
        boolean success = repo.delete(toDelete);
        if (success) result.setPayload(found);
        else result.addMessage("Unable to Delete, Security Clearance is currently being used",ResultType.INVALID);

        return result;
    }

    private Result<SecurityClearance> validate(SecurityClearance securityClearance){
        Result<SecurityClearance> result = new Result<>();
        if(securityClearance == null){
            result.addMessage("Security Clearance cannot be null",ResultType.INVALID);
            return result;
        }
        if(securityClearance.getName() == null || securityClearance.getName().isEmpty() || securityClearance.getName().isBlank()){
            result.addMessage("Security Clearance name cannot be empty", ResultType.INVALID);
        }
        List<SecurityClearance> all = findAll();
        for(SecurityClearance sc : all){
            if(sc.getName().trim().equals(securityClearance.getName().trim())){
                result.addMessage("Security Clearance name cannot be a duplicate", ResultType.INVALID);
            }
        }
        return result;
    }

}
