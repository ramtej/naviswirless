@echo off
 
SET CLASSPATH=C:\CSTech2008\projects\NetBeansProjects\naviswireless\lib\axis.jar;C:\CSTech2008\projects\NetBeansProjects\naviswireless\lib\axis-ant.jar;C:\CSTech2008\projects\NetBeansProjects\naviswireless\lib\commons-discovery-0.2.jar;C:\CSTech2008\projects\NetBeansProjects\naviswireless\lib\commons-logging-1.0.4.jar;C:\CSTech2008\projects\NetBeansProjects\naviswireless\lib\jaxrpc.jar;C:\CSTech2008\projects\NetBeansProjects\naviswireless\lib\log4j-1.2.8.jar;C:\CSTech2008\projects\NetBeansProjects\naviswireless\lib\saaj.jar;C:\CSTech2008\projects\NetBeansProjects\naviswireless\lib\wsdl4j-1.5.1.jar

echo %CLASSPATH%
 
java -classpath "%CLASSPATH%" org.apache.axis.wsdl.WSDL2Java -v -W -a -o ../src -p navisaxiswsclient http://192.168.1.2:8080/clientservices/clientservices?wsdl
 
:end