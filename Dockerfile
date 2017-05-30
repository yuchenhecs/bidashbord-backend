#Deploy BI Backend Microservice
#Adds WAR file to webapps
#Copies tomcat-users to conf folder
FROM tomcat:8.5
COPY ./target/bibackend.war /usr/local/tomcat/webapps
ADD tomcat-users.xml /usr/local/tomcat/conf/