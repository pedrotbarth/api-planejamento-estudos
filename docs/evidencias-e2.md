# Evidências da E2

Testes executados em 26/11/2026 com a API na porta 8081 e PostgreSQL no Docker.

| Teste | Resultado observado |
| --- | --- |
| Cadastro de estudante, disciplina e atividade | `201 Created` |
| Consulta e atualização dos três recursos | `200 OK` |
| Exclusão de registros de teste | `204 No Content` |
| Busca de um registro excluído | `404 Not Found` |
| Persistência no PostgreSQL | A consulta SQL confirmou o nome atualizado do estudante e o vínculo da disciplina com o estudante. |
| Paginação e ordenação de atividades pendentes | Em ordem crescente, a página 0 mostrou `Revisao de SQL` e a página 1 mostrou `Prova final`. Em ordem decrescente, `Prova final` apareceu primeiro. |
| Validação de nome vazio e de página com `size=0` | `400 Bad Request` |
| Exclusão de disciplina com atividades | `409 Conflict`. A consulta seguinte retornou `200 OK`, confirmando que a disciplina continuava cadastrada. |

## Resposta da listagem paginada

`GET /atividades?status=PENDENTE&disciplinaId=1&page=0&size=1&sort=dataLimite,asc` retornou `200 OK`.

```json
{
  "content": [
    {
      "id": 4,
      "titulo": "Revisao de SQL",
      "tipo": "EXERCICIO",
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

## Resposta da exclusão com conflito

`DELETE /disciplinas/1` retornou `409 Conflict`.

```json
{
  "detail": "A operação viola uma restrição do banco. Verifique os registros vinculados.",
  "status": 409,
  "title": "Conflict"
}
```