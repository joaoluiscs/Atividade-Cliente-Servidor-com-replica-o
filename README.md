# Sistema Cliente-Servidor com Replicação e Eleição de Líder (Raft)

## Descrição do Projeto

Este projeto implementa um sistema distribuído baseado no modelo **Cliente-Servidor**, utilizando replicação de dados entre múltiplos nós e um mecanismo de **eleição de líder** baseado no algoritmo **Raft**.

O sistema foi desenvolvido como atividade acadêmica para demonstrar conceitos fundamentais de **sistemas distribuídos**, como tolerância a falhas, coordenação entre nós e consistência de dados.

---

## Arquitetura do Sistema

O sistema é composto pelos seguintes componentes:

* **Client App**
  Responsável por enviar requisições ao sistema.

* **Getaway Service**
  Atua como ponto de entrada do sistema e redireciona requisições para o líder atual do cluster.

* **Server Primary / Server Replica**
  Conjunto de servidores que compõem o cluster distribuído e mantêm os dados replicados.

Estrutura simplificada da arquitetura:

Client → Gateway → Leader → Replicas

O líder coordena as operações e replica os dados para os demais servidores.

---

## Tecnologias Utilizadas

* Java
* Spring Boot
* REST API
* Git / GitHub
* Arquitetura Cliente-Servidor
* Algoritmo de consenso Raft (versão simplificada)

---

## Funcionamento do Sistema

O sistema funciona da seguinte forma:

1. O cliente envia requisições para o Gateway.
2. O Gateway identifica qual servidor é o líder atual.
3. O líder processa a requisição.
4. O líder replica os dados para os servidores seguidores (followers).
5. Todos os nós mantêm o mesmo estado de dados.

Caso o líder falhe, os servidores restantes iniciam um processo de eleição para escolher um novo líder automaticamente.

---

## Eleição de Líder (Raft)

O algoritmo Raft é utilizado para garantir que apenas um servidor atue como líder em determinado momento.

O processo de eleição ocorre quando:

* Um servidor detecta que o líder não está mais respondendo.
* Um novo termo de eleição é iniciado.
* Os servidores votam em um candidato.
* O candidato que obtiver a maioria dos votos torna-se o novo líder.

Esse mecanismo garante que o sistema continue operando mesmo na presença de falhas.

---

## Replicação de Dados

Todas as operações de escrita são realizadas através do líder.

O fluxo de escrita ocorre da seguinte forma:

1. Cliente envia requisição
2. Líder registra a operação
3. Líder replica a operação para os seguidores
4. Seguidores armazenam os dados localmente

Esse processo garante consistência entre os nós do cluster.

---

## Demonstração do Sistema

Durante a execução do sistema é possível observar:

* Monitoramento do cluster
* Detecção de falha do líder
* Eleição automática de um novo líder
* Replicação de dados entre os servidores

---

## Autor

Projeto desenvolvido por:

João Luis
