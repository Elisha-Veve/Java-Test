package com.vmorg.buildrequest;

import com.vmorg.auth.AuthorisingService;
import com.vmorg.build.SystemBuildService;
import com.vmorg.exception.MachineNotCreatedException;
import com.vmorg.exception.UserNotEntitledException;
import com.vmorg.machine.Machine;
import com.vmorg.machine.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class VirtualMachineRequestorImplTest {

    VirtualMachineRequestorImpl underTest;
    Machine machine;
    String username;

    @Mock
    AuthorisingService authorisingService;
    @Mock
    SystemBuildService systemBuildService;


    @BeforeEach
    void setup() {
        authorisingService = mock(AuthorisingService.class);
        systemBuildService = mock(SystemBuildService.class);
        machine = new Server();
        username = "Mary";
        underTest = new VirtualMachineRequestorImpl(authorisingService, systemBuildService);
    }



    //    Checks user's entitlements, and if appropriate creates a new request for a virtual machine build.
    @Test
    void ChecksUserEntitlement() throws MachineNotCreatedException, UserNotEntitledException {
        //arrange or setup
        when(authorisingService.isAuthorised(username)).thenReturn(true);
        when(systemBuildService.createNewMachine(machine)).thenReturn("host20230429");

        //act
        underTest.createNewRequest(machine, username);

        //assert or verify
        verify(authorisingService, times(1)).isAuthorised(username);

    }


    //    UserNotEntitledException – thrown when a user is not entitled to make a request
    @Test
    void ThrowsUserNotEntitledException () {
        //arrange or setup
        when(authorisingService.isAuthorised(username)).thenReturn(false);

        //act
        //assert
        Assertions.assertThrows(UserNotEntitledException.class, () -> underTest.createNewRequest(machine, username));
    }

    //    MachineNotCreatedException – thrown when a machine build is not successful
    @Test
    void ThrowsMachineNotCreatedException () {
        //setup
        when(authorisingService.isAuthorised(username)).thenReturn(true);
        when(systemBuildService.createNewMachine(machine)).thenReturn("");

        //act
        //verify
        Assertions.assertThrows(MachineNotCreatedException.class, () -> underTest.createNewRequest(machine, username));
    }

//Reports on the number of failed request builds for today
//Returns:
//The total number of failed builds for today

    @Test
    void ReturnsFailedBuilds () {
        //setup
        when(authorisingService.isAuthorised(username)).thenReturn(true);
        when(systemBuildService.createNewMachine(machine)).thenReturn("");

        //act
        Assertions.assertThrows(MachineNotCreatedException.class, ()-> underTest.createNewRequest(machine, username));
        Assertions.assertThrows(MachineNotCreatedException.class, ()-> underTest.createNewRequest(machine, username));
        Assertions.assertThrows(MachineNotCreatedException.class, ()-> underTest.createNewRequest(machine, username));
        int totalFailedBuilds = underTest.totalFailedBuildsForDay();

        // verify
        Assertions.assertEquals(3,totalFailedBuilds);
    }

}