package learn.field_agent.data;

import learn.field_agent.models.Alias;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AliasRepository {

    @Transactional
    List<Alias> findByAgentId(int AgentId);

    Alias findById(int id);

    Alias add(Alias alias);

    boolean update(Alias alias);

    @Transactional
    boolean delete(int id);

    List<Alias> findAll();
}
