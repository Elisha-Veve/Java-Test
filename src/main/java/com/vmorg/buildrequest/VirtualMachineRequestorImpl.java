package com.vmorg.buildrequest;

import com.vmorg.auth.AuthorisingService;
import com.vmorg.build.SystemBuildService;
import com.vmorg.exception.MachineNotCreatedException;
import com.vmorg.exception.UserNotEntitledException;
import com.vmorg.machine.Machine;

import java.util.Map;

public final class VirtualMachineRequestorImpl implements VirtualMachineRequestor{
    private final AuthorisingService authorisingService;
    private final SystemBuildService systemBuildService;
    private int totalFailedBuilds;

    public VirtualMachineRequestorImpl(AuthorisingService authorisingService, SystemBuildService systemBuildService) {
        this.authorisingService = authorisingService;
        this.systemBuildService = systemBuildService;
        this.totalFailedBuilds = 0;
    }


    @Override
    public void createNewRequest(Machine machine, String username) throws UserNotEntitledException, MachineNotCreatedException {
        if (authorisingService.isAuthorised(username)) {
            String hostnameGenerated = systemBuildService.createNewMachine(machine);
            //TODO: CHECK HOSTNAME MATCHES REQUIRED FORMAT
            //host20230429
            if (hostnameGenerated == "host20230429") {
                //TODO: GO ON AND BUILD MACHINE
            } else {
                totalFailedBuilds++;
                throw new MachineNotCreatedException();
            }

        } else {
            throw new UserNotEntitledException();
        }
    }

    @Override
    public Map<String, Map<String, Integer>> totalBuildsByUserForDay() {
        return null;
    }

    @Override
    public int totalFailedBuildsForDay() {
        return totalFailedBuilds;
    }
}
