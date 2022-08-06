package learn.field_agent.data;

import learn.field_agent.models.Alias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasJdbcTemplateRepositoryTest {
    @Autowired
    AliasJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindBond() {
        Alias bond = repository.findById(1);
        assertEquals("Bond", bond.getName());
        assertEquals("International Spy", bond.getPersona());
        assertEquals(1, bond.getAliasId());
        assertEquals(1, bond.getAgentId());
    }

    @Test
    void shouldFindNone() {
        assertNull(repository.findById(-1));
    }

    @Test
    void shouldFindAllForAgent1() {
        int agentId = 1;
        List<Alias> result = repository.findByAgentId(agentId);

        assertEquals(2, result.size());
    }

    @Test
    void shouldFindNoneForAgentBad() {
        int badAgentId = -10;
        List<Alias> result = repository.findByAgentId(badAgentId);

        assertEquals(0, result.size());
    }


    @Test
    void shouldAddAlias() {
        Alias toAdd = new Alias();
        toAdd.setAliasId(0);
        toAdd.setName("Powers");
        toAdd.setPersona("International man of mystery");
        toAdd.setAgentId(1);
        Alias result = repository.add(toAdd);

        assertNotNull(result);
        assertEquals(toAdd.getName(),result.getName());
        assertNotEquals(0, result.getAliasId());
    }

    @Test
    void shouldNotAddNull() {
        Alias bad = repository.add(null);
        assertNull(bad);
    }

    @Test
    void shouldNotAddToNoneExistingAgent() {
        Alias toAdd = new Alias();
        toAdd.setAliasId(0);
        toAdd.setName("Powers");
        toAdd.setPersona("International man of mystery");
        toAdd.setAgentId(-1000);
        Alias result = repository.add(toAdd);

        assertNull(result);
    }



    @Test
    void shouldUpdate(){
        Alias toUpdate = new Alias();
        toUpdate.setAliasId(1);
        toUpdate.setName("Person");
        toUpdate.setPersona("Someone");
        toUpdate.setAgentId((1));

        assertTrue(repository.update(toUpdate));
        assertEquals(repository.findById(1).getName(),toUpdate.getName());
    }

    @Test
    void shouldNotUpdate(){

        assertFalse(repository.update(null));
        Alias toUpdate = new Alias();
        toUpdate.setAliasId(-1000);
        toUpdate.setName("Person");
        toUpdate.setPersona("Someone");
        toUpdate.setAgentId((1));

        assertFalse(repository.update(toUpdate));
    }

    @Test
    void shouldDelete(){
        Alias toAdd = new Alias();
        toAdd.setAliasId(0);
        toAdd.setName("Powers");
        toAdd.setPersona("International man of mystery");
        toAdd.setAgentId(1);
        Alias toDelete = repository.add(toAdd);
        int toDeleteId = toDelete.getAliasId();

        boolean result = repository.delete(toDeleteId);
        assertTrue(result);

    }

    @Test
    void shouldNotDelete(){
        boolean result = repository.delete(-10);
        assertFalse(result);
    }

}