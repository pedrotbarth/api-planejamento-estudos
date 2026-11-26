# Evidências de execução da E2

Execução em 26/11/2026 com Java 21, Spring Boot 3.5.14 e PostgreSQL 16.14. O Flyway aplicou a migração inicial em um banco vazio. Os dados usados na demonstração são fictícios.

Foram verificadas 45 requisições HTTP contra a API em execução. O backend foi reiniciado durante a verificação de persistência.

| Método | Caminho | Status | Verificação |
| --- | --- | ---: | --- |
| POST | `/estudantes` | 201 | Cria o primeiro estudante |
| POST | `/estudantes` | 201 | Cria outro estudante para paginação |
| GET | `/estudantes/1` | 200 | Busca o estudante pelo ID |
| PUT | `/estudantes/2` | 200 | Altera o nome do estudante |
| GET | `/estudantes?page=0&size=1&sort=id,desc` | 200 | Pagina e ordena estudantes |
| POST | `/disciplinas` | 201 | Cria disciplina vinculada |
| POST | `/disciplinas` | 201 | Cria disciplina em outro estudante |
| GET | `/disciplinas/1` | 200 | Busca disciplina com seu vínculo |
| PUT | `/disciplinas/2` | 200 | Altera disciplina e troca seu vínculo |
| GET | `/disciplinas?page=1&size=1&sort=id,asc` | 200 | Consulta segunda página de disciplinas |
| POST | `/atividades` | 201 | Cria atividade pendente e devolve Location |
| POST | `/atividades` | 201 | Cria atividade com prazo anterior |
| POST | `/atividades` | 201 | Cria atividade em outra disciplina |
| POST | `/atividades` | 201 | Cria atividade para demonstrar o filtro de status |
| PUT | `/atividades/4` | 200 | Marca atividade como concluída |
| GET | `/atividades` | 200 | Lista todas as atividades, ordenadas pelo prazo |
| GET | `/atividades?page=0&disciplinaId=1&status=PENDENTE&size=1&sort=dataLimite,asc` | 200 | Aplica dois filtros, paginação e ordem crescente |
| GET | `/atividades?page=1&disciplinaId=1&status=PENDENTE&size=1&sort=dataLimite,asc` | 200 | Consulta a segunda página do mesmo filtro |
| GET | `/atividades?status=CONCLUIDA` | 200 | Filtra apenas pelo status |
| GET | `/atividades?disciplinaId=2` | 200 | Filtra apenas pela disciplina |
| GET | `/atividades?sort=dataLimite,desc` | 200 | Ordena atividades de forma decrescente |
| GET | `/atividades/1` | 200 | Busca atividade com seu vínculo |
| POST | `/estudantes` | 400 | Valida nome em branco |
| POST | `/estudantes` | 400 | Rejeita JSON malformado |
| POST | `/atividades` | 400 | Valida data de calendário |
| POST | `/atividades` | 400 | Rejeita tipo numérico |
| GET | `/atividades?size=0` | 400 | Rejeita tamanho de página inválido |
| GET | `/atividades?sort=outro,desc` | 400 | Rejeita campo de ordenação inválido |
| GET | `/atividades?status=OUTRO` | 400 | Rejeita filtro de status inválido |
| GET | `/estudantes/999999` | 404 | Retorna erro para estudante inexistente |
| POST | `/disciplinas` | 404 | Rejeita vínculo com estudante inexistente |
| POST | `/atividades` | 404 | Rejeita vínculo com disciplina inexistente |
| DELETE | `/estudantes/1` | 409 | Impede excluir estudante com dependências |
| DELETE | `/disciplinas/1` | 409 | Impede excluir disciplina com atividades |
| GET | `/atividades/4` | 200 | Confirma persistência após reiniciar o backend |
| DELETE | `/atividades/1` | 204 | Exclui atividade |
| DELETE | `/atividades/2` | 204 | Exclui atividade |
| DELETE | `/atividades/3` | 204 | Exclui atividade |
| DELETE | `/atividades/4` | 204 | Exclui atividade |
| GET | `/atividades/1` | 404 | Confirma que atividade foi excluída |
| DELETE | `/disciplinas/1` | 204 | Exclui disciplina sem dependências |
| DELETE | `/disciplinas/2` | 204 | Exclui disciplina sem dependências |
| DELETE | `/estudantes/1` | 204 | Exclui estudante sem dependências |
| DELETE | `/estudantes/2` | 204 | Exclui estudante sem dependências |
| GET | `/estudantes` | 200 | Confirma listagem vazia após excluir estudantes |

## Listagem filtrada, paginada e ordenada

`GET /atividades?page=0&disciplinaId=1&status=PENDENTE&size=1&sort=dataLimite,asc` retornou **200**.

```json
{
  "content": [
    {
      "id": 2,
      "titulo": "Exercício preparatório",
      "tipo": "TRABALHO",
      "dataLimite": "2026-11-30",
      "status": "PENDENTE",
      "disciplinaId": 1
    }
  ],
  "page": 0,
  "size": 1,
  "totalElements": 2,
  "totalPages": 2
}
```

## Validação de dados

`POST /estudantes` retornou **400**.

```json
{
  "type": "about:blank",
  "title": "Dados inválidos",
  "status": 400,
  "detail": "Corrija os campos indicados e envie a requisição novamente.",
  "instance": "/estudantes",
  "erros": [
    {
      "campo": "nome",
      "mensagem": "O nome é obrigatório."
    }
  ]
}
```

## Conflito ao excluir disciplina vinculada

`DELETE /disciplinas/1` retornou **409**.

```json
{
  "type": "about:blank",
  "title": "Conflito de integridade",
  "status": 409,
  "detail": "A operação entra em conflito com registros vinculados ou com uma restrição do banco.",
  "instance": "/disciplinas/1"
}
```

## Leitura após reiniciar a API

`GET /atividades/4` retornou **200**.

```json
{
  "id": 4,
  "titulo": "Apresentação entregue",
  "tipo": "TRABALHO",
  "dataLimite": "2026-11-29",
  "status": "CONCLUIDA",
  "disciplinaId": 1
}
```
