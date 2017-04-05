# Software #


It will be necessary to configure environment variables. Here you can learn how to define them: [Mac OS X](http://www.mkyong.com/mac/how-to-set-environment-variables-on-mac-os-x/),  [Linux](http://www.cyberciti.biz/faq/set-environment-variable-linux/)  

## JDK

Obtain [JDK 8u72](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and install


Configure:
* Define the environment variable JAVA_HOME with the path to the folder where the JDK was installed
* Add JAVA_HOME/bin to the environment variable PATH
* Execute the command *javac -version* to confirm


## Apache Maven, MVN
Obtain [MVN 3.3.9](http://maven.apache.org/download.cgi) and install


Configure:
* Define the environment variable M2_HOME with the path to the folder where the MVN was installed
* Add M2_HOME/bin to the environment variable PATH
* Execute the command *mvn --version* to confirm


## Git
Obtain [Git 2.x](https://git-scm.com/download/) and [install](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)


Configure:
* Execute the command *git --version* to confirm


## Apache jUDDI
Obtain [jUDDI 3.3.2](http://disciplinas.tecnico.ulisboa.pt/leic-sod/2015-2016/download/juddi-3.3.2_tomcat-7.0.64_9090.zip)


Configure:
* Make the scripts executable: *chmod +x juddi-3.3.2_tomcat-7.0.64_9090/bin/\*.sh*
* Launch the server: *./juddi-3.3.2_tomcat-7.0.64_9090/bin/startup.sh*
* Access to the indice page of jUDDI to confirm it is working properly [http://localhost:9090/juddiv3/](http://localhost:9090/juddiv3/)
