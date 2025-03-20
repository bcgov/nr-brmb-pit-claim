The load-test folder contains automated load tests for the claims app.

The test cases have to be tailored to the environment they are executed in.

ccs-load-test.bat starts the load test in openshift dlvr
ccs-load-test-on-prem.bat starts the load test in the on prem dlvr environment

The .bat files might have to be modified to match the JMETER_HOME of the machine it's executed

Test Plan: Claims Calculator 1.7.0 Load Test Plan 

Test Scenarios: Claims Calculator 1.7.0 Load Test Scenarios 

Tricentis Tests: PIM-1814: Claims Calculator Load Tests
