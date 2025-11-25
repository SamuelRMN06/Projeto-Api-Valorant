# 🏪 Projeto Integração - API de Lojas e Produtos

## 📘 Visão Geral
Esta API foi desenvolvida em **Spring Boot** e tem como objetivo gerenciar **lojas** e **produtos**.  
Ela permite criar, listar, atualizar e excluir lojas, além de cadastrar produtos vinculados a cada loja.
Link deploy: https://projetointegracao.onrender.com
---

## ⚙️ Tecnologias Utilizadas
- **Java 17+**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **Hibernate**
- **Jakarta Validation**
- **Lombok**
- **Banco de dados relacional (PostgreSQL)**

---

## 🧱 Estrutura de Modelos

### 🏬 LojaModel
| Campo | Tipo | Obrigatório | Descrição |
|--------|------|-------------|------------|
| `id` | Long | Sim (auto) | Identificador único da loja |
| `nome` | String | ✅ Sim | Nome da loja |
| `endereco` | String | Não | Endereço físico |
| `telefone` | String | Não | Telefone de contato |
| `email` | String | Não | E-mail da loja |
| `descricao` | String | Não | Descrição da loja |
| `cnpj` | String | Não | CNPJ da loja |
| `produtos` | List<ProdutoModel> | Não | Produtos vinculados |

---

### 📦 ProdutoModel
| Campo | Tipo | Obrigatório | Descrição |
|--------|------|-------------|------------|
| `id` | Long | Sim (auto) | Identificador do produto |
| `nome` | String | ✅ Sim | Nome do produto |
| `descricao` | String | Não | Detalhes do produto |
| `preco` | Double | Não | Valor unitário |
| `marca` | String | Não | Marca |
| `peso` | Double | Não | Peso |
| `quantidade` | Integer | Não | Quantidade em estoque |
| `loja` | LojaModel | ✅ Sim (no POST) | Loja vinculada |

---

## 🧭 Endpoints

# 🏬 Lojas

### **Listar todas as lojas**
**GET** `/lojas`
```json
[
  {
    "id": 1,
    "nome": "Loja Central",
    "endereco": "Rua A, 123",
    "telefone": "99999-9999",
    "email": "contato@lojacentral.com",
    "descricao": "Loja principal",
    "cnpj": "12.345.678/0001-90",
    "produtos": [
        {
            "id": 1,
            "nome": "Notebook Gamer ASUS TUF",
            "descricao": "Notebook gamer com processador Ryzen 7 e placa RTX 3060",
            "preco": 7499.99,
            "marca": "ASUS",
            "peso": 2.3,
            "quantidade": 10
        }
    ]
  }
]
```
**Status:** `200 OK`

---

### **Buscar loja por ID**
**GET** `/lojas/{id}`
```json
{
    "id": 1,
    "nome": "Loja Central",
    "endereco": "Rua A, 123",
    "telefone": "99999-9999",
    "email": "contato@lojacentral.com",
    "descricao": "Loja principal",
    "cnpj": "12.345.678/0001-90",
    "produtos": [
        {
            "id": 1,
            "nome": "Notebook Gamer ASUS TUF",
            "descricao": "Notebook gamer com processador Ryzen 7 e placa RTX 3060",
            "preco": 7499.99,
            "marca": "ASUS",
            "peso": 2.3,
            "quantidade": 10
        }
    ]
  }
```
**Status:** `200 OK`  
**Erros:** `404 NOT_FOUND – Loja com o ID {id} não encontrada.`

---

### **Criar loja**
**POST** `/lojas`
```json
{
  "nome": "Nova Loja",
  "endereco": "Av. Principal, 456",
  "telefone": "88888-8888",
  "email": "nova@loja.com",
  "descricao": "Filial zona sul",
  "cnpj": "98.765.432/0001-09"
}
```
**Status:** `201 CREATED`  
**Erros:** 
- `400 BAD_REQUEST – O campo 'nome' é obrigatório.`
- `400 BAD_REQUEST – Erro ao criar loja.`


---

### **Atualizar loja (PUT)**
**PUT** `/lojas/{id}`
```json
{
  "nome": "Nova Loja",
  "endereco": "Av. Principal, 456",
  "telefone": "88888-8888",
  "email": "nova@loja.com",
  "descricao": "Filial zona sul",
  "cnpj": "98.765.432/0001-09"
}
```
**Status:** `200 OK`

**Erros:** 
- `400 BAD_REQUEST – O campo 'nome' é obrigatório.`
- `400 BAD_REQUEST – Erro ao atualizar a loja.`

---

### **Atualizar loja (PATCH)**
**PATCH** `/lojas/{id}`
```json
{
  "telefone": "99999-1111"
}
```
**Status:** `200 OK`

**Erros:** 
- `404 NOT_FOUND – Loja com o ID {id} não encontrada.`
- `400 BAD_REQUEST – Erro ao atualizar parcialmente loja.`

---

### **Excluir loja**
**DELETE** `/lojas/{id}`  
**Status:** `204 NO CONTENT`

**Erros:** `404 NOT_FOUND – Loja com o ID {id} não encontrada.`

---

# 📦 Produtos

### **Listar todos os produtos**
**GET** `/produtos`
```json
[
  {
    "id": 1,
    "nome": "Notebook Gamer ASUS TUF",
    "descricao": "Notebook gamer com processador Ryzen 7 e placa RTX 3060",
    "preco": 7499.99,
    "marca": "ASUS",
    "peso": 2.3,
    "quantidade": 10
  }
]
```
**Status:** `200 OK`

---

### **Buscar produto por ID**
**GET** `/produtos/{id}`
```json
{
  "id": 1,
  "nome": "Notebook Gamer ASUS TUF",
  "descricao": "Notebook gamer com processador Ryzen 7 e placa RTX 3060",
  "preco": 7499.99,
  "marca": "ASUS",
  "peso": 2.3,
  "quantidade": 10
}
```
**Status:** `200 OK`  
**Erros:** `404 NOT_FOUND – Produto com o id {id} não encontrado.`

---

### **Criar produto**
**POST** `/produtos`
```json
{
  "nome": "Notebook Gamer ASUS TUF",
  "descricao": "Notebook gamer com processador Ryzen 7 e placa RTX 3060",
  "preco": 7499.99,
  "marca": "ASUS",
  "peso": 2.3,
  "quantidade": 10,
  "loja": {
    "id": 1
  }
}
```
**Status:** `201 CREATED`  
**Erros:**  
- `400 BAD_REQUEST – O campo 'nome' é obrigatório.`  
- `400 BAD_REQUEST – Loja não encontrada.`

---

### **Atualizar produto (PUT)**
**PUT** `/produtos/{id}`
```json
{
  "nome": "Notebook Gamer ASUS TUF",
  "descricao": "Notebook gamer com processador Ryzen 7 e placa RTX 3060",
  "preco": 7599.99,
  "marca": "ASUS",
  "peso": 2.3,
  "quantidade": 12,
  "loja": {
    "id": 1
  }
}
```
**Status:** `200 OK`  
**Erros:**  
- `400 BAD_REQUEST – O campo 'nome' é obrigatório.`  
- `404 NOT_FOUND – Produto não encontrado.`

---

### **Atualizar produto (PATCH)**
**PATCH** `/produtos/{id}`
```json
{
  "descricao": "Notebook gamer com processador Ryzen 7, placa RTX 3060 e 16GB RAM"
}
```
**Status:** `200 OK`  
**Erros:** `404 NOT_FOUND – Produto com o id {id} não encontrado.`

---

### **Excluir produto**
**DELETE** `/produtos/{id}`  
**Status:** `204 NO CONTENT`  
**Erros:** `404 NOT_FOUND – Produto com o id {id} não encontrado.`

---

## ⚠️ Códigos de Erro Comuns
| Código | Descrição |
|--------|------------|
| `400 BAD_REQUEST` | Dados inválidos ou campos obrigatórios ausentes |
| `404 NOT_FOUND` | Recurso não encontrado |

---

## 🧩 Relacionamentos
- Uma **Loja** possui **vários Produtos** (`@OneToMany`)
- Um **Produto** pertence a **uma Loja** (`@ManyToOne`)

---

