@echo off
REM Lanza la aplicacion de consola contra WildFly (EJB remoto).
REM Requisitos: 'mvn install' hecho y el servidor WildFly corriendo con ej1.ear desplegado.

setlocal
if "%JBOSS_HOME%"=="" set JBOSS_HOME=C:\Users\juanm\Desktop\TSI\wildfly

set BASE=%~dp0..
set CP=%~dp0target\classes;%BASE%\ej1-api\target\ej1-api-0.0.1-SNAPSHOT.jar;%JBOSS_HOME%\bin\client\jboss-client.jar

java -Dfile.encoding=UTF-8 -cp "%CP%" tse2026.ej1.consola.ConsolaApp
endlocal
