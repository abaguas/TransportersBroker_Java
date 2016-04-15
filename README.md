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
instrucoes para o projeto

para correr a transportadora cliente usar o procedimento semelhante as servidoras, usar a opcao -D (e.g. mvn -Dws.i=2) ao fazer  exec:java para definir a que transportadora servidora ela se deve ligar

-------------------------------------------------------------------------------
**FIM**
