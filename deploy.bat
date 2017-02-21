@set curPath=%cd%
@cd %~dp0
@call mvn clean deploy -U -Pdev -Dmaven.test.skip=true
@cd %curPath%
@pause