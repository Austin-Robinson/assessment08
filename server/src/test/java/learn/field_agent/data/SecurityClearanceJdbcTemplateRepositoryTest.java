package learn.field_agent.data;

import learn.field_agent.models.Agent;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceJdbcTemplateRepositoryTest {

    @Autowired
    SecurityClearanceJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        SecurityClearance topSecret = new SecurityClearance(2, "Top Secret");

        SecurityClearance actual = repository.findById(1);
        assertEquals(secret, actual);

        actual = repository.findById(2);
        assertEquals(topSecret, actual);

    }

    @Test
    void shouldFindNone(){
        SecurityClearance fake = new SecurityClearance(-1, "FAKE");
        SecurityClearance actual = repository.findById(fake.getSecurityClearanceId());

        assertNull(actual);
    }

    // seeded with 2 security clearances


    @Test
    void shouldAdd(){
        SecurityClearance securityClearance = makeSecurityClearance();
        SecurityClearance actual = repository.add(securityClearance);

        assertNotNull(actual);
        assertNotEquals(0,actual.getSecurityClearanceId());


    }
    @Test
    void shouldNotAddNull(){
        SecurityClearance bad = null;
        SecurityClearance actual = repository.add(bad);

        assertNull(actual);
    }

    @Test
    void shouldUpdate(){
        SecurityClearance before = repository.findById(1);
        SecurityClearance toUpdate = new SecurityClearance();
        toUpdate.setName("NEW NAME");
        toUpdate.setSecurityClearanceId(1);

        boolean result = repository.update(toUpdate);
        assertTrue(result);

        SecurityClearance actual = repository.findById(1);
        assertNotEquals(before.getName(),actual.getName());
        assertEquals(before.getSecurityClearanceId(), actual.getSecurityClearanceId());
    }
    @Test
    void shouldNotUpdate(){
    SecurityClearance bad = null;
    assertFalse(repository.update(bad));

    SecurityClearance fake = new SecurityClearance();
    fake.setName("fake-aroni");
    fake.setSecurityClearanceId(-1);

    assertFalse(repository.update(fake));
    }

    @Test
    void shouldDelete(){
        SecurityClearance test = new SecurityClearance();
        test.setName("to delete");
        SecurityClearance result = repository.add(test);
        assertTrue(repository.delete(result.getSecurityClearanceId()));

        assertNull(repository.findById(result.getSecurityClearanceId()));
    }

    @Test
    void shouldNotDelete(){
        //id -1 DNE
        assertFalse(repository.delete(-1));
        //one should not be deleted since it is used by other data
        assertFalse(repository.delete(1));
    }

    //SUPPORT METHODS
    private SecurityClearance makeSecurityClearance() {
        SecurityClearance sc = new SecurityClearance();
        sc.setName("TEMP");
        int tempId = 0;
        sc.setSecurityClearanceId(tempId);
        return sc;
    }

}