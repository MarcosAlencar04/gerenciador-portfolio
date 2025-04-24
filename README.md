## 1. Configuração banco de dados

- **Criar um banco de dados PostgreSQL**
- Realizar as migrations das tabelas a partir da pasta `migrations` do projeto

## 2. Mock API
  1. **Instalar json-server**:
     ```bash
     npm install -g json-server
     ```
2. **Na pasta `mock-api`** rode o json-server
   ```bash
   json-server --watch db.json --port 3000
   ```
