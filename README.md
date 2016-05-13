# Projeto de Sistemas Distribuídos 2015-2016 #

Grupo de SD 64 - Campus Alameda

André Águas - 78854 - andre.aguas@ist.utl.pt

Rui Sá - 78324 - rui.sa@ist.utl.pt

João Neto - 78745 - joao.b.neto@ist.utl.pt


Repositório:
[tecnico-distsys/A_64-project](https://github.com/tecnico-distsys/A_64-project/)

-------------------------------------------------------------------------------

## Instruções de instalação 


### Ambiente

[0] Iniciar sistema operativo

Linux


[1] Iniciar servidores de apoio

JUDDI:
```
...
```


[2] Criar pasta temporária

```
cd ...
mkdir ...
```


[3] Obter código fonte do projeto (versão entregue)

```
git clone https://github.com/tecnico-distsys/A_64-project.git (via https) 
git clone git@github.com:tecnico-distsys/A_64-project.git (via ssh)
```



[4] Instalar módulos de bibliotecas auxiliares

```
cd uddi-naming
mvn clean install
```

```
cd ...
mvn clean install
```

-------------------------------------------------------------------------------

### Serviço CA

[1] Construir e executar **servidor**

```
cd ...-ws
mvn clean install
mvn exec:java
```

[2] Construir **cliente** e executar testes

```
cd ...-ws-cli
mvn clean install
```

...


-------------------------------------------------------------------------------


-------------------------------------------------------------------------------

### Serviço TRANSPORTER

[1] Construir e executar **servidor**

```
cd ...-ws
mvn clean install
mvn exec:java
```

[2] Construir **cliente** e executar testes

```
cd ...-ws-cli
mvn clean install
```

...


-------------------------------------------------------------------------------

### Serviço BROKER

[1] Construir e executar **servidor**

```
cd ...-ws
mvn clean install
mvn exec:java
```


[2] Construir **cliente** e executar testes

```
cd ...-ws-cli
mvn clean install
```

...

-------------------------------------------------------------------------------
Instrucoes para o projeto:

Nas transportadoras existe uma opção -Dws.i que define qual o seu número.
Foram distribuidos certificados para transportadoras até ao número 5.

Nos brokers existe a opçao -Dws.br que define tambem o numero do broker. A falta de argumentos ou o numero 1 dá o nome ao broker principal: 'UpaBroker'. Qualquer outro número define um broker backup de nome 'UpaBrokerN'
Também existe a opção -Dws.nap que define um tempo de espera no broker, para que tarde a resposta ao cliente.
Para iniciar os brokers as tranportadoras e o CA devem estar publicados. A ordem de publicacao dos brokers é indiferente. Mas depois é necessário fazer um 'enter' no principal depois de o backup estar ativo, e depois enter no backup para que ele inicie o timer que verifica se houve ou não falha no Broker principal


Nos clientes do broker foi adicionada a opcão -Dprocess que envia um sigKill ao processo do broker. Esta opçao serve para os testes de IT a apresentar na demonstração.

-------------------------------------------------------------------------------
**FIM**
