package learn.field_agent.data;

import learn.field_agent.data.mappers.AgentMapper;
import learn.field_agent.data.mappers.AliasMapper;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
@Repository
public class AliasJdbcTemplateRepository implements AliasRepository{

    private final JdbcTemplate jdbcTemplate;

    public AliasJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public List<Alias> findByAgentId(int agentId) {
        final String sql = "select alias_id, name, persona, agent_id from alias " +
                "where agent_id = ? ;";
        return jdbcTemplate.query(sql, new AliasMapper(), agentId);

    }


    @Override
    @Transactional
    public Alias findById(int id) {
        final String sql = "select alias_id, name, persona, agent_id "
                + "from alias "
                + "where alias_id = ? ;";


        Alias alias = jdbcTemplate.query(sql, new AliasMapper(), id).stream()
                .findFirst().orElse(null);

        return alias;
    }

    @Override
    public Alias add(Alias alias){
        if(alias == null){
            //ce n'est pas bon
            System.out.println("ICI!!!");
            return null;
        }
        System.out.printf("NAME %s, PERSONA %s, Agent_ID %s",alias.getName(),alias.getPersona(),alias.getAgentId());
        final String sqlForAgent = "select agent_id, first_name, middle_name, last_name, dob, height_in_inches "
                + "from agent "
                + "where agent_id = ?;";
        Agent agent = jdbcTemplate.query(sqlForAgent, new AgentMapper(), alias.getAgentId()).stream()
                .findFirst().orElse(null);
        if(agent == null){
            System.out.println("ICI");
            return null;
        }
        final String sql = "insert into alias (name, persona, agent_id) "
                + " values (?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, alias.getName());
            ps.setString(2, alias.getPersona());
            ps.setInt(3, alias.getAgentId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }
        alias.setAliasId(keyHolder.getKey().intValue());
        return alias;
    }

    @Override
    /**
     * Update will only update Name and persona
     * @param alias must not be null and have valid fields.
     * @return returns the alias sent in with proper id.
     */
    public boolean update(Alias alias) {
        if(alias == null){
            return false;
        }
        final String sql = "update alias set" +
                "`name` = ?, persona = ?  where alias_id = ?;";

        return jdbcTemplate.update(sql, alias.getName(), alias.getPersona(), alias.getAliasId()) > 0;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("delete from alias where alias_id = ?;", id) > 0;
    }

    @Override
    public List<Alias> findAll(){
        final String sql = "select alias_id, name, persona, agent_id "
                + "from alias limit 1000;";
        return jdbcTemplate.query(sql, new AliasMapper());

    }
}
