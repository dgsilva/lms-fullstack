# 🏗️ Diagramas de Arquitetura - LMS

## 1. Arquitetura Geral do Sistema

```mermaid
graph TB
    subgraph "Frontend Layer"
        A[Angular App]
        B[Components]
        C[Services]
        D[Guards]
        E[Interceptors]
    end
    
    subgraph "API Gateway"
        F[Spring Boot]
        G[Controllers]
        H[Security Filter]
        I[CORS Config]
    end
    
    subgraph "Business Layer"
        J[Services]
        K[DTOs]
        L[Validators]
    end
    
    subgraph "Data Layer"
        M[Repositories]
        N[JPA Entities]
        O[PostgreSQL]
    end
    
    A --> F
    B --> C
    C --> G
    G --> H
    H --> I
    G --> J
    J --> K
    K --> L
    J --> M
    M --> N
    N --> O
```

## 2. Fluxo de Autenticação JWT

```mermaid
sequenceDiagram
    participant C as Client (Angular)
    participant A as AuthController
    participant S as AuthService
    participant U as UserDetailsService
    participant J as JwtUtil
    participant DB as Database
    
    C->>A: POST /auth/login
    A->>S: login(credentials)
    S->>U: loadUserByUsername(email)
    U->>DB: findByEmail(email)
    DB-->>U: User details
    U-->>S: UserDetails
    S->>J: generateToken(userDetails)
    J-->>S: JWT Token
    S-->>A: AuthResponse
    A-->>C: Token + User Info
    
    Note over C: Store token in localStorage
    
    C->>A: GET /cursos (with Bearer token)
    A->>H: JwtAuthenticationFilter
    H->>J: extractUsername(token)
    J-->>H: username
    H->>U: loadUserByUsername(username)
    U-->>H: UserDetails
    H->>J: validateToken(token, userDetails)
    J-->>H: valid
    H-->>A: Authenticated request
    A-->>C: Response
```

## 3. Arquitetura de Banco de Dados

```mermaid
erDiagram
    USUARIOS {
        bigint id PK
        varchar primeiro_nome
        varchar ultimo_nome
        date data_nascimento
        varchar email UK
        varchar telefone
        varchar senha
    }
    
    ROLES {
        bigint id PK
        varchar nome UK
    }
    
    USUARIO_PAPEIS {
        bigint usuario_id FK
        bigint papel_id FK
    }
    
    CURSOS {
        bigint id PK
        varchar nome UK
        text descricao
        date data_inicio
        date data_fim
        bigint criado_por_id FK
    }
    
    MATRICULAS {
        bigint id PK
        bigint usuario_id FK
        bigint curso_id FK
        varchar status
        timestamp data_matricula
    }
    
    CATEGORIAS_TAREFAS {
        bigint id PK
        varchar codigo UK
        varchar nome
    }
    
    REGISTROS_ESTUDO {
        bigint id PK
        bigint matricula_id FK
        date data
        bigint categoria_id FK
        text descricao
        timestamp horario_inicio
        integer duracao_minutos
        timestamp criado_em
        timestamp atualizado_em
    }
    
    USUARIOS ||--o{ USUARIO_PAPEIS : "has"
    ROLES ||--o{ USUARIO_PAPEIS : "assigned to"
    USUARIOS ||--o{ CURSOS : "creates"
    USUARIOS ||--o{ MATRICULAS : "enrolls"
    CURSOS ||--o{ MATRICULAS : "has"
    MATRICULAS ||--o{ REGISTROS_ESTUDO : "contains"
    CATEGORIAS_TAREFAS ||--o{ REGISTROS_ESTUDO : "categorizes"
```

## 4. Fluxo de Negócio - Matrícula em Curso

```mermaid
flowchart TD
    A[Estudante solicita matrícula] --> B{Estudante autenticado?}
    B -->|Não| C[Redirecionar para login]
    B -->|Sim| D{Curso existe?}
    D -->|Não| E[Erro: Curso não encontrado]
    D -->|Sim| F{Estudante já matriculado?}
    F -->|Sim| G[Erro: Já matriculado]
    F -->|Não| H{Máximo 3 matrículas?}
    H -->|Sim| I[Erro: Limite excedido]
    H -->|Não| J[Criar matrícula]
    J --> K[Salvar no banco]
    K --> L[Retornar sucesso]
    
    C --> M[Login]
    M --> A
```

## 5. Arquitetura de Segurança

```mermaid
graph TB
    subgraph "Security Layers"
        A[CORS Filter]
        B[JWT Authentication Filter]
        C[Authorization Filter]
        D[Method Security]
    end
    
    subgraph "Authentication Flow"
        E[Login Request]
        F[Validate Credentials]
        G[Generate JWT]
        H[Return Token]
    end
    
    subgraph "Authorization Flow"
        I[Protected Request]
        J[Extract Token]
        K[Validate Token]
        L[Check Roles]
        M[Allow/Deny]
    end
    
    A --> B
    B --> C
    C --> D
    
    E --> F
    F --> G
    G --> H
    
    I --> J
    J --> K
    K --> L
    L --> M
```

## 6. Estrutura de Camadas do Backend

```mermaid
graph TB
    subgraph "Presentation Layer"
        A[Controllers]
        B[DTOs]
        C[Exception Handlers]
    end
    
    subgraph "Business Layer"
        D[Services]
        E[Validators]
        F[Business Rules]
    end
    
    subgraph "Data Access Layer"
        G[Repositories]
        H[JPA Entities]
        I[Database]
    end
    
    subgraph "Infrastructure Layer"
        J[Security Config]
        K[CORS Config]
        L[JWT Utils]
    end
    
    A --> D
    B --> A
    C --> A
    D --> G
    E --> D
    F --> D
    G --> H
    H --> I
    J --> A
    K --> A
    L --> J
```

## 7. Fluxo de Registro de Estudo

```mermaid
sequenceDiagram
    participant S as Student
    participant RC as RegistroController
    participant RS as RegistroService
    participant MR as MatriculaRepository
    participant CR as CategoriaRepository
    participant RR as RegistroRepository
    participant DB as Database
    
    S->>RC: POST /registros-estudo
    RC->>RS: criarRegistro(dto, estudanteId)
    RS->>MR: findById(matriculaId)
    MR->>DB: SELECT matricula
    DB-->>MR: Matricula data
    MR-->>RS: Matricula entity
    
    RS->>RS: validarMatriculaDoEstudante()
    
    RS->>CR: findByCodigo(categoriaCodigo)
    CR->>DB: SELECT categoria
    DB-->>CR: Categoria data
    CR-->>RS: Categoria entity
    
    RS->>RS: validarDuracaoIncrementos()
    
    RS->>RR: save(registro)
    RR->>DB: INSERT registro
    DB-->>RR: Saved registro
    RR-->>RS: Registro entity
    RS-->>RC: Registro entity
    RC-->>S: HTTP 201 Created
```

## 8. Configuração de Ambiente

```mermaid
graph LR
    subgraph "Development"
        A[application-dev.properties]
        B[PostgreSQL Local]
        C[Logs Detalhados]
    end
    
    subgraph "Production"
        D[application-prod.properties]
        E[PostgreSQL Cloud]
        F[Logs Otimizados]
    end
    
    subgraph "Docker"
        G[docker-compose.yml]
        H[Container PostgreSQL]
        I[Container App]
    end
    
    A --> B
    A --> C
    D --> E
    D --> F
    G --> H
    G --> I
```

## 9. Integração Frontend-Backend

```mermaid
graph TB
    subgraph "Angular Frontend"
        A[Components]
        B[Services]
        C[HTTP Interceptor]
        D[Auth Guard]
    end
    
    subgraph "Spring Boot Backend"
        E[REST Controllers]
        F[Security Filter]
        G[CORS Config]
        H[JWT Filter]
    end
    
    subgraph "Communication"
        I[HTTP Requests]
        J[JWT Tokens]
        K[CORS Headers]
    end
    
    A --> B
    B --> C
    C --> I
    I --> F
    F --> G
    G --> H
    H --> E
    
    C --> J
    J --> H
    
    C --> K
    K --> G
```

## 10. Monitoramento e Logs

```mermaid
graph TB
    subgraph "Application Logs"
        A[INFO: Business Operations]
        B[DEBUG: Development Details]
        C[ERROR: Exception Handling]
        D[WARN: Security Issues]
    end
    
    subgraph "Security Logs"
        E[Authentication Attempts]
        F[JWT Token Validation]
        G[Authorization Checks]
        H[CORS Requests]
    end
    
    subgraph "Performance Metrics"
        I[Response Times]
        J[Database Queries]
        K[Memory Usage]
        L[CPU Usage]
    end
    
    A --> M[Log Files]
    B --> M
    C --> M
    D --> M
    E --> M
    F --> M
    G --> M
    H --> M
    I --> N[Metrics Dashboard]
    J --> N
    K --> N
    L --> N
```
