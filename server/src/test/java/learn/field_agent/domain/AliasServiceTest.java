package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

class AliasServiceTest {
    @Autowired
    AliasService service;

    @MockBean
    AliasRepository repo;


    @Test
    void shouldAdd(){
        Alias alias = makeAlias();
        Alias mockOut = makeAlias();
        mockOut.setAliasId(2);

        when(repo.add(alias)).thenReturn(mockOut);
        Result<Alias> result = service.add(alias);

        successAssertions(result);
    }

    @Test
    void shouldAddSansPersona(){
        Alias alias = new Alias();
        alias.setName("NO PERSONA");
        alias.setPersona(null);
        alias.setAgentId(1);
        Alias mock = alias;
        mock.setAliasId(3);

        when(repo.add(alias)).thenReturn(mock);
        List<Alias> mockList = new ArrayList<>();
        Alias bond = new Alias();
        bond.setName("Bond");
        mockList.add(bond);
        when(repo.findByAgentId(1)).thenReturn(mockList);

        Result<Alias> result = service.add(alias);
        successAssertions(result);
    }

    @Test
    void shouldFailSansPersona(){
        Alias alias = new Alias();
        alias.setName("Bond");
        alias.setPersona(null);
        alias.setAgentId(1);
        List<Alias> mockList = new ArrayList<>();
        Alias bond = new Alias();
        bond.setName("Bond");
        mockList.add(bond);
        when(repo.findByAgentId(1)).thenReturn(mockList);

        Result<Alias> result = service.add(alias);
        failAssertionsInvalid(result);
    }

    @Test
    void shouldNotAdd(){
        Result<Alias> result = service.add(null);
        failAssertionsInvalid(result);

        List<Alias> mockList = new ArrayList<>();
        mockList.add(makeAlias());

        when(repo.findByAgentId(1)).thenReturn(mockList);
        Alias duplicate = makeAlias();
        result = service.add(duplicate);
        failAssertionsInvalid(result);

        Alias blankName = makeAlias();
        blankName.setName("");
        result = service.add(blankName);
        failAssertionsInvalid(result);

        Alias blankPersona = makeAlias();
        blankPersona.setPersona("");
        result = service.add(blankPersona);
        failAssertionsInvalid(result);




    }

    @Test
    void shouldUpdate(){
        Alias mock = makeAlias();
        when(repo.update(mock)).thenReturn(true);
        when(repo.findById(mock.getAliasId())).thenReturn(mock);
        Result<Alias> result = service.update(mock);

        successAssertions(result);


    }
    @Test
    void shouldNotUpdateNotFound(){
        Alias mock = makeAlias();
        when(repo.update(mock)).thenReturn(false);
        when(repo.findById(makeAlias().getAliasId())).thenReturn(null);

        Result<Alias> result = service.update(mock);
        failAssertionsNotFound(result);

    }
    @Test
    void shouldNotUpdateBadFields(){
        Alias mock = makeAlias();
        when(repo.findById(mock.getAliasId())).thenReturn(mock);

        Result<Alias> result = service.update(null);
        failAssertionsInvalid(result);

        mock.setName("");
        failAssertionsInvalid(service.update(mock));


    }
    @Test
    void shouldNotUpdateWithDuplicate(){
        Alias mock = makeAlias();
        Alias toChange = new Alias();
        toChange.setName("temp");
        toChange.setAgentId(1);
        toChange.setAliasId(10);
        List<Alias> mockList = new ArrayList<>();
        mockList.add(mock);
        mockList.add(toChange);
        when(repo.findAll()).thenReturn(mockList);


        Alias failure = makeAlias();
        failure.setAliasId(10);
        Result<Alias> result = service.update(failure);
        failAssertionsInvalid(result);
    }

    @Test
    void shouldDelete(){
        int idToDelete = 1;
        when(repo.delete(idToDelete)).thenReturn(true);
        when(repo.findById(idToDelete)).thenReturn(makeAlias());

        Result<Alias> result = service.delete(idToDelete);

        successAssertions(result);
    }
    @Test
    void shouldNotDelete(){
        int idToDelete = 1;
        when(repo.delete(idToDelete)).thenReturn(false);
        when(repo.findById(idToDelete)).thenReturn(null);

        Result<Alias> result = service.delete(idToDelete);
        failAssertionsNotFound(result);
    }

    private Alias makeAlias(){
        Alias alias = new Alias();
        alias.setAliasId(1);
        alias.setName("TEST");
        alias.setPersona("PERSONA");
        alias.setAgentId(1);
        return alias;
    }

    private void failAssertionsInvalid(Result<Alias> result) {
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }

    private void failAssertionsNotFound(Result<Alias> result) {
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    private void successAssertions(Result<Alias> result) {
        assertNotNull(result.getPayload());
        assertTrue(result.isSuccess());
        assertEquals(ResultType.SUCCESS, result.getType());
    }

}