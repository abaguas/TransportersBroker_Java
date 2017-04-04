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
Ferramenta de linha de comando para fazer controlo de versões.
Obter
Git 2.x (escolher a versão estável mais recente disponível para a plataforma)
Instalar
Installing Git
Configurar
Executar comando git --version para confirmar


## Eclipse for Java Enterprise Edition, Eclipse JEE
Ambiente integrado de desenvolvimento para a plataforma Java, versão empresarial
Obter o arquivo que contém o executável de Eclipse Mars e ficheiros associados:
Eclipse JEE 4.5.1 - Mars Service Release 1 
Atenção: se tem instalado no seu sistema operativo o JDK 32 bits, deve obter o Eclipse 32 bits; se instalou o JDK 64 bits, deve obter o Eclipse 64 bits.
Nota: o arquivo para Windows vem em formato .zip, para outros sistemas operativos vem em .tar.gz.
Instalar:
Extrair o arquivo para um diretório com permissões de escrita.
Possível resultado: C:\Users\john\Documents\eclipse em Windows, ou /home/john/Documents/eclipse em Linux para um utilizador chamado john
Configurar:
Especificar o JDK como Standard VM
Nota: só deverá ser necessário este passo em Windows. Reveja este passo em outros sistemas operativos se encontrar problemas ao compilar código no Eclipse nos vários exercícios ao longo do semestre
Window -> Preferences -> Java -> Installed JREs -> Add...
Indicar o caminho até ao diretório do JDK: ex. C:\Java\jdk1.8.0_72
Confirmar que as "Installed JREs" apenas faz referência ao JDK instalado nas opções ativadas
Conectores m2e (maven 2 eclipse)
Window -> Preferences -> Maven -> Discovery e clicar em "Open catalog".
Seleccionar:
m2e connector for jaxws-maven-plugin
m2e connector for maven-dependency-plugin
m2e connector for org.codehaus.mojo:jaxb2-maven-plugin and org.jvnet.jaxb2.maven2:maven-jaxb2-plugin
m2e-apt
m2e-egit


## Apache jUDDI
Servidor de nomes para Web Services que segue a norma UDDI (Universal Description, Discovery, and Integration)
Obter
jUDDI 3.3.2 (configured for port 9090)
Instalar
Em Linux ou Mac, tornar os scripts de lançamento executáveis:
chmod +x juddi-3.3.2_tomcat-7.0.64_9090/bin/*.sh
Para lançar o servidor, basta executar o seguinte comando na pasta juddi-3.3.2_tomcat-7.0.64_9090/bin:
startup.sh (Linux e Mac)
startup.bat (Windows)
Para confirmar funcionamento, aceder à página de índice do jUDDI, que dá também acesso à interface de administração
http://localhost:9090/juddiv3/
utilizador:senha uddiadmin:da_password1
