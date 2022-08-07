package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

class SecurityClearanceServiceTest {
    @Autowired
    SecurityClearanceService service;

    @MockBean
    SecurityClearanceRepository repository;

    @Test
    void shouldAdd(){
        SecurityClearance toAdd = new SecurityClearance();
        toAdd.setName("UNIQUE NAME");
        toAdd.setSecurityClearanceId(0);

        SecurityClearance mockOut = createMock();

        when(repository.add(toAdd)).thenReturn(mockOut);

        Result<SecurityClearance> result = service.add(toAdd);

        successAssertions(result);


    }


    @Test
    void shouldNotAddDuplicate(){
        SecurityClearance duplicate = new SecurityClearance();
        duplicate.setName("Secret");
        duplicate.setSecurityClearanceId(0);
        List<SecurityClearance> mockList = new ArrayList<>();
        mockList.add(duplicate);
        when(repository.findAll()).thenReturn(mockList);

        failAssertions(service.add(duplicate));

    }

    @Test
    void shouldNotAddNull(){
       failAssertions(service.add(null));
    }
    @Test
    void shouldNotAddWithBlankName(){
        SecurityClearance blank = new SecurityClearance();
        blank.setName(" ");
        blank.setSecurityClearanceId(0);

        failAssertions(service.add(blank));

        blank.setName("");

        failAssertions(service.add(blank));

        blank.setName(null);

        failAssertions(service.add(blank));
    }

    @Test
    void shouldNotAddWithExtraWhiteSpace() {
        SecurityClearance duplicate = new SecurityClearance();
        duplicate.setName("Secret");
        duplicate.setSecurityClearanceId(0);
        List<SecurityClearance> mockList = new ArrayList<>();
        mockList.add(duplicate);
        duplicate.setName("Secret         ");
        when(repository.findAll()).thenReturn(mockList);

        failAssertions(service.add(duplicate));
    }

    @Test
    void shouldUpdate(){
        SecurityClearance toUpdate = createMock();
        when(repository.update(toUpdate)).thenReturn(true);
        when(repository.findById(toUpdate.getSecurityClearanceId())).thenReturn(toUpdate);

        Result<SecurityClearance> result = service.update(toUpdate);

       successAssertions(result);

    }
    @Test
    void shouldNotUpdateWithDuplicate(){
        SecurityClearance duplicate = new SecurityClearance();
        duplicate.setName("Secret");
        duplicate.setSecurityClearanceId(0);
        List<SecurityClearance> mockList = new ArrayList<>();
        mockList.add(duplicate);
        when(repository.findAll()).thenReturn(mockList);

        Result<SecurityClearance> result = service.update(duplicate);

        assertNull(result.getPayload());
        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());

        duplicate.setName("Secret  ");

        failAssertions(service.update(duplicate));
    }

    @Test
    void shouldNotUpdateWithBadFields(){
        Result<SecurityClearance> result = service.update(null);

        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
        assertFalse(result.isSuccess());

        SecurityClearance blank = new SecurityClearance();
        blank.setName(" ");
        blank.setSecurityClearanceId(0);

        failAssertions(service.update(blank));

        blank.setName("");

        failAssertions(service.update(blank));

        blank.setName(null);

        failAssertions(service.update(blank));


    }

    @Test
    void shouldNotUpdateNotFound(){
        SecurityClearance notFound = createMock();

        when(repository.update(notFound)).thenReturn(false);

        Result<SecurityClearance> result = service.update(notFound);

        assertEquals(ResultType.NOT_FOUND,result.getType());
    }

    @Test
    void shouldDelete(){
        SecurityClearance toDelete = createMock();
        when(repository.delete(toDelete.getSecurityClearanceId())).thenReturn(true);
        when(repository.findById(toDelete.getSecurityClearanceId())).thenReturn(toDelete);

        Result<SecurityClearance> result = service.delete(toDelete.getSecurityClearanceId());

        successAssertions(result);

    }

    @Test
    void shouldNotDeleteNotFound(){
        SecurityClearance notFound = createMock();
        Result<SecurityClearance> result = service.delete(notFound.getSecurityClearanceId());

        assertEquals(ResultType.NOT_FOUND,result.getType());
    }

    @Test
    void shouldNotDeleteSCBeingUsed(){
        SecurityClearance used = createMock();
        when(repository.delete(used.getSecurityClearanceId())).thenReturn(false);
        when(repository.findById(used.getSecurityClearanceId())).thenReturn(used);
        failAssertions(service.delete(used.getSecurityClearanceId()));

    }




    //HELPER METHODS__________________________


    // Method to keep all duplicate assertions in one place
    // generally on a failed test we are looking for a null payload
    // invalid resultType
    // and not successfull
    private void failAssertions(Result<SecurityClearance> service) {
        Result<SecurityClearance> result = service;

        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
        assertFalse(result.isSuccess());
    }


    private void successAssertions(Result<SecurityClearance> result) {
        assertEquals(ResultType.SUCCESS, result.getType());
        assertNotNull(result.getPayload());
        assertTrue(result.isSuccess());
    }

    private SecurityClearance createMock(){
        SecurityClearance mockOut = new SecurityClearance();
        mockOut.setName("UNIQUE NAME");
        mockOut.setSecurityClearanceId(3);
        return mockOut;
    }


}