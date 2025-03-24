@echo on
set JMETER_HOME=..\..\..\apache-jmeter-5.6.3
set JMETER_CCS_THREADS=15
start cmd.exe /c %JMETER_HOME%\bin\jmeter -n -Jthreads=%JMETER_CCS_THREADS% -t ccs-load-test.jmx -l CCS_Test_Results_15.jtl
