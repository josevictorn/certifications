
# Certification API

Uma aplicação Java desenvolvida com Spring Boot, destinada a fornecer uma API Rest de certificação para usuários que desejam validar seus conhecimentos em tecnologias específicas. A API permite aos usuários escolher uma tecnologia e responder a perguntas relacionadas, fornecendo feedback sobre o desempenho e mantendo um ranking dos melhores alunos.

## Funcionalidades

- Mostra as questões e suas alternativas.
- Permite que os usuários façam uma certificação por tecnologia.
- Valida as respostas do usuário e apresenta o total de acertos.
- Exibe um ranking das 10 melhores notas.

## Classes do projeto

### Questions
- UUID id
- String description
- String technology
- List<Alternatives>

### Alternatives
- UUID id
- String description
- String questionId
- boolean isCorrect

### Students
- UUID id
- String email
- List<Certifications>
- LocalDateTime createdAt

### Certifications
- UUID id
- String technology
- int grade
- UUID studentId
- List<AnswersCertifications>

### AnswersCertifications
- UUID id
- UUID certificationId
- UUID studentId
- UUID questionId
- UUID answerId
- boolean isCorrect
- LocalDateTime createdAt
## Aprendizados

- Utilização do Spring Boot para criar uma Rest API com Java.
- Integração de banco de dados PostgreSQL (rodando no Docker) com Spring JPA.
- Aplicação dos princípios do SOLID.
## Requisitos

- Docker;
- Java 17;
- Maven;

## Instalação

- Clone o respositório;
- Instale as dependências com ```npm install```;
- Configure o banco PostgreSQL com ```docker compose up -d```;
- Configure o banco com as seguintes propriedades:
```bash
  server.port=8085
  spring.datasource.url=jdbc:postgresql://localhost:5434/pg_nlw
  spring.datasource.password=admin
  spring.datasource.username=admin
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.show-sql=true
```
- Rode a aplicação com ```mvn spring-boot:run```;

    
## Documentação da API

#### POST /students/verifyIfHasCertification
- cria novo usuário atráves do email e valida se o usuário já fez a certificação de uma tecnologia específica;
- request body: 
```json
{
	"email": "josevictor@email.com",
	"technology": "JAVA"
}
```
#### GET /questions/technology/{technology}
- exibe as perguntas da certificação de uma determinada tecnologia, seguida de suas respectivas alternativas;
- exemplo de teste para tecnologia JAVA: ```/questions/technology/JAVA```
- response body: 
```json
[
	{
		"id": "c5f02721-6dc3-4fa6-b46d-6f2e8dca9c66",
		"technology": "JAVA",
		"description": "Como criar uma classe em Java?",
		"alternatives": [
			{
				"id": "bafdf631-6edf-482a-bda9-7dce1efb1890",
				"description": "Usando a palavra-chave \"class\""
			},
			{
				"id": "98f6891b-5f14-4b8e-bb87-46456a2610d4",
				"description": "Definindo uma interface em Java"
			},
			{
				"id": "993a7d37-62a0-4040-810d-d667e3f7a891",
				"description": "Utilizando mÃ©todos estÃ¡ticos"
			},
			{
				"id": "98bf8d0f-dc1c-4db0-b09c-94246089aa02",
				"description": "Criando um construtor padrÃ£o"
			}
		]
	},
	{
		"id": "b0ec9e6b-721c-43c7-9432-4d0b6eb15b01",
		"technology": "JAVA",
		"description": "Explique o conceito de polimorfismo em Java.",
		"alternatives": [
			{
				"id": "1da0f5dd-7a02-4c34-8c60-4648b55141f2",
				"description": "HeranÃ§a simples"
			},
			{
				"id": "c4fd1c23-8993-4972-92d5-b8364b78d1cf",
				"description": "Encapsulamento em Java"
			},
			{
				"id": "9da03a4e-3c8d-4a32-87e1-9898938435c2",
				"description": "Sobrecarga de mÃ©todos"
			},
			{
				"id": "f8e6e9b3-199b-4f0d-97ce-7e5bdc080da9",
				"description": "Capacidade de um objeto de assumir vÃ¡rias formas"
			}
		]
	},
	{
		"id": "f85e9434-1711-4e02-9f9e-7831aa5c743a",
		"technology": "JAVA",
		"description": "Como lidar com exceÃ§Ãµes em Java?",
		"alternatives": [
			{
				"id": "3528a132-9c12-4c8a-aa93-9f6e813c43d1",
				"description": "Ignorando a exceÃ§Ã£o"
			},
			{
				"id": "d3e51a56-9b97-4bb8-9827-8bcf89f4b276",
				"description": "Utilizando blocos try-catch"
			},
			{
				"id": "63c0210c-2a9a-4e93-98ec-8d9f3984e2b0",
				"description": "Declarando uma exceÃ§Ã£o sem tratamento"
			},
			{
				"id": "e4dbf524-0a54-428a-b57c-853996fc8e19",
				"description": "Usando a palavra-chave \"finally\""
			}
		]
	}
]
```

#### POST /students/certification/answer
- cria uma resposta a uma certificação de uma tecnologia específica pra um usuário específico;
- request body:
```json
{
	"email": "marcos@email.com",
	"technology": "JAVA",
	"questionsAnswer": [
		{
			"questionId": "c5f02721-6dc3-4fa6-b46d-6f2e8dca9c66",
			"alternativeId": "bafdf631-6edf-482a-bda9-7dce1efb1890"
		},
		{
			"questionId": "b0ec9e6b-721c-43c7-9432-4d0b6eb15b01",
			"alternativeId": "f8e6e9b3-199b-4f0d-97ce-7e5bdc080da9"
		},
		{
			"questionId": "f85e9434-1711-4e02-9f9e-7831aa5c743a",
			"alternativeId": "d3e51a56-9b97-4bb8-9827-8bcf89f4b276"
		}
	]
}
- response body:
{
	"id": "46678786-fe1a-4541-b5f2-40731fdc74eb",
	"technology": "JAVA",
	"grade": 3,
	"studentId": "73667408-fab1-4130-96f1-04d403b05a06",
	"answersCertificationsEntity": [
		{
			"id": "0a4b442d-4585-4dfc-bbd3-d48e922c6365",
			"certificationId": "46678786-fe1a-4541-b5f2-40731fdc74eb",
			"studentId": null,
			"studentEntity": null,
			"questionId": "c5f02721-6dc3-4fa6-b46d-6f2e8dca9c66",
			"answerId": "bafdf631-6edf-482a-bda9-7dce1efb1890",
			"createdAt": "2024-02-10T15:04:36.405739",
			"correct": true
		},
		{
			"id": "525c28df-a1d9-4939-bd76-bf9e2c7645e3",
			"certificationId": "46678786-fe1a-4541-b5f2-40731fdc74eb",
			"studentId": null,
			"studentEntity": null,
			"questionId": "b0ec9e6b-721c-43c7-9432-4d0b6eb15b01",
			"answerId": "f8e6e9b3-199b-4f0d-97ce-7e5bdc080da9",
			"createdAt": "2024-02-10T15:04:36.409115",
			"correct": true
		},
		{
			"id": "eab25b68-ac9b-4b98-bfac-4c9b352e96ae",
			"certificationId": "46678786-fe1a-4541-b5f2-40731fdc74eb",
			"studentId": null,
			"studentEntity": null,
			"questionId": "f85e9434-1711-4e02-9f9e-7831aa5c743a",
			"answerId": "d3e51a56-9b97-4bb8-9827-8bcf89f4b276",
			"createdAt": "2024-02-10T15:04:36.412444",
			"correct": true
		}
	],
	"createdAt": "2024-02-10T15:04:36.393236"
}
```

#### GET /ranking/top10
- exibe um ranking dos 10 melhores alunos
- response body: 
```json
[
	{
		"id": "46678786-fe1a-4541-b5f2-40731fdc74eb",
		"technology": "JAVA",
		"grade": 3,
		"studentId": "73667408-fab1-4130-96f1-04d403b05a06",
		"studentEntity": {
			"id": "73667408-fab1-4130-96f1-04d403b05a06",
			"email": "marcos@email.com",
			"createdAt": "2024-02-10T15:04:36.381938"
		},
		"answersCertificationsEntity": [],
		"createdAt": "2024-02-10T15:04:36.393236"
	}
]
```
## Autores

- [@josevictorn](https://github.com/josevictorn)

