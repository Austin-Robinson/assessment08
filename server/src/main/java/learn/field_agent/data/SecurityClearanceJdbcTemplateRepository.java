package learn.field_agent.data;

import learn.field_agent.data.mappers.AgencyAgentMapper;
import learn.field_agent.data.mappers.SecurityClearanceMapper;
import learn.field_agent.models.AgencyAgent;
import learn.field_agent.models.SecurityClearance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SecurityClearanceJdbcTemplateRepository implements SecurityClearanceRepository {

    private final JdbcTemplate jdbcTemplate;

    public SecurityClearanceJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SecurityClearance findById(int securityClearanceId) {

        final String sql = "select security_clearance_id, name security_clearance_name "
                + "from security_clearance "
                + "where security_clearance_id = ?;";

        return jdbcTemplate.query(sql, new SecurityClearanceMapper(), securityClearanceId)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public List<SecurityClearance> findAll() {
        final String sql = "select security_clearance_id, name security_clearance_name "
                + "from security_clearance limit 1000;";
        return jdbcTemplate.query(sql, new SecurityClearanceMapper());
    }

    @Override
    public SecurityClearance add(SecurityClearance securityClearance) {
        if(securityClearance == null){
            //ce n'est pas bon
            return null;
        }
        final String sql = "insert into security_clearance (name) "
                + " values (?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, securityClearance.getName());
            return ps;
        }, keyHolder);

        if(rowsAffected <=0){
            return null;
        }
        securityClearance.setSecurityClearanceId(keyHolder.getKey().intValue());
        return securityClearance;
    }

    @Override
    public boolean update(SecurityClearance securityClearance) {
        if(securityClearance == null){
            return false;
        }
        final String sql = "update security_clearance set" +
                "`name` = ? where security_clearance_id = ?;";
        int rowsUpdated = jdbcTemplate.update(sql,securityClearance.getName(),securityClearance.getSecurityClearanceId());
        return rowsUpdated > 0;
    }

    @Override
    public boolean delete(int securityClearanceId) {


        final String queryString ="select agency_agent.security_clearance_id  FROM " +
                "agency_agent where agency_agent.security_clearance_id = ?;";
        List<Integer> list = new ArrayList<>();
        list = jdbcTemplate.queryForList(queryString, Integer.class, securityClearanceId);
        System.out.println(list.size());
        if(list.size() != 0){
            //this means that the securityClearanceId is being used, and we should not delete it

            return false;
        }
        final String setSQL = "SET FOREIGN_KEY_CHECKS=0;";
        final String sql = "delete from security_clearance where security_clearance_id = ?;";
        final String resetSQL = "SET FOREIGN_KEY_CHECKS=1;";
        jdbcTemplate.execute(setSQL);
        int rowsDeleted = jdbcTemplate.update(sql,securityClearanceId);
        jdbcTemplate.execute(resetSQL);
        return rowsDeleted > 0;
    }
}
