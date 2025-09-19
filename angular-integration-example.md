# Integração Angular com LMS Backend

## Configuração do Angular

### 1. Instalar dependências necessárias

```bash
npm install @angular/common @angular/http
# ou se usando HttpClient
npm install @angular/common
```

### 2. Configurar HttpClient no app.module.ts

```typescript
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { AuthInterceptor } from './interceptors/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
```

### 3. Criar interceptor para JWT

```typescript
// interceptors/auth.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');
    
    if (token) {
      const authReq = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
      return next.handle(authReq);
    }
    
    return next.handle(req);
  }
}
```

### 4. Criar service para autenticação

```typescript
// services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface AuthResponse {
  token: string;
  email: string;
  primeiroNome: string;
  ultimoNome: string;
  roles: Array<{id: number, nome: string}>;
}

export interface RegisterRequest {
  primeiroNome: string;
  ultimoNome: string;
  dataNascimento: string;
  email: string;
  telefone: string;
  senha: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081';
  private currentUserSubject = new BehaviorSubject<AuthResponse | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    // Verificar se há token salvo ao inicializar
    const token = localStorage.getItem('token');
    if (token) {
      // Opcional: validar token com backend
      this.validateToken();
    }
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, credentials)
      .pipe(
        tap(response => {
          localStorage.setItem('token', response.token);
          this.currentUserSubject.next(response);
        })
      );
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/usuarios/registro`, userData)
      .pipe(
        tap(response => {
          localStorage.setItem('token', response.token);
          this.currentUserSubject.next(response);
        })
      );
  }

  logout(): void {
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  private validateToken(): void {
    // Implementar validação de token se necessário
    // Por exemplo, fazer uma requisição para um endpoint protegido
  }
}
```

### 5. Criar service para cursos

```typescript
// services/curso.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Curso {
  id: number;
  nome: string;
  descricao: string;
  dataInicio: string;
  dataFim: string;
  criadoPor: any;
}

export interface CriarCursoRequest {
  nome: string;
  descricao: string;
  dataInicio: string;
  dataFim: string;
}

@Injectable({
  providedIn: 'root'
})
export class CursoService {
  private apiUrl = 'http://localhost:8081';

  constructor(private http: HttpClient) { }

  listarCursos(): Observable<Curso[]> {
    return this.http.get<Curso[]>(`${this.apiUrl}/cursos`);
  }

  buscarCurso(id: number): Observable<Curso> {
    return this.http.get<Curso>(`${this.apiUrl}/cursos/${id}`);
  }

  criarCurso(curso: CriarCursoRequest): Observable<Curso> {
    return this.http.post<Curso>(`${this.apiUrl}/cursos`, curso);
  }

  atualizarCurso(id: number, curso: CriarCursoRequest): Observable<Curso> {
    return this.http.put<Curso>(`${this.apiUrl}/cursos/${id}`, curso);
  }

  removerCurso(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/cursos/${id}`);
  }
}
```

### 6. Criar service para matrículas

```typescript
// services/matricula.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Matricula {
  id: number;
  estudante: any;
  curso: any;
  status: string;
  dataMatricula: string;
}

export interface MatricularRequest {
  cursoId: number;
}

@Injectable({
  providedIn: 'root'
})
export class MatriculaService {
  private apiUrl = 'http://localhost:8081';

  constructor(private http: HttpClient) { }

  matricular(request: MatricularRequest): Observable<Matricula> {
    return this.http.post<Matricula>(`${this.apiUrl}/matriculas`, request);
  }

  listarMatriculasDoEstudante(): Observable<Matricula[]> {
    return this.http.get<Matricula[]>(`${this.apiUrl}/matriculas/estudante`);
  }

  cancelarMatricula(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/matriculas/${id}`);
  }
}
```

### 7. Criar service para registros de estudo

```typescript
// services/registro-estudo.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RegistroEstudo {
  id: number;
  matricula: any;
  data: string;
  categoria: any;
  descricao: string;
  horarioInicio: string;
  duracaoMinutos: number;
  criadoEm: string;
  atualizadoEm?: string;
}

export interface CriarRegistroRequest {
  matriculaId: number;
  data: string;
  categoriaCodigo: string;
  descricao: string;
  horarioInicio: string;
  duracaoMinutos: number;
}

export interface CategoriaTarefa {
  id: number;
  codigo: string;
  nome: string;
}

@Injectable({
  providedIn: 'root'
})
export class RegistroEstudoService {
  private apiUrl = 'http://localhost:8081';

  constructor(private http: HttpClient) { }

  criarRegistro(request: CriarRegistroRequest): Observable<RegistroEstudo> {
    return this.http.post<RegistroEstudo>(`${this.apiUrl}/registros-estudo`, request);
  }

  listarRegistrosDoEstudante(): Observable<RegistroEstudo[]> {
    return this.http.get<RegistroEstudo[]>(`${this.apiUrl}/registros-estudo`);
  }

  listarRegistrosPorMatricula(matriculaId: number): Observable<RegistroEstudo[]> {
    return this.http.get<RegistroEstudo[]>(`${this.apiUrl}/registros-estudo/matricula/${matriculaId}`);
  }

  atualizarRegistro(id: number, request: CriarRegistroRequest): Observable<RegistroEstudo> {
    return this.http.put<RegistroEstudo>(`${this.apiUrl}/registros-estudo/${id}`, request);
  }

  removerRegistro(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/registros-estudo/${id}`);
  }

  listarCategorias(): Observable<CategoriaTarefa[]> {
    return this.http.get<CategoriaTarefa[]>(`${this.apiUrl}/categorias-tarefas`);
  }
}
```

### 8. Exemplo de componente de login

```typescript
// components/login/login.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  template: `
    <div class="login-container">
      <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
        <h2>Login</h2>
        
        <div class="form-group">
          <label for="email">Email:</label>
          <input 
            type="email" 
            id="email" 
            formControlName="email"
            class="form-control"
            [class.error]="loginForm.get('email')?.invalid && loginForm.get('email')?.touched"
          >
          <div *ngIf="loginForm.get('email')?.invalid && loginForm.get('email')?.touched" class="error-message">
            Email é obrigatório
          </div>
        </div>

        <div class="form-group">
          <label for="senha">Senha:</label>
          <input 
            type="password" 
            id="senha" 
            formControlName="senha"
            class="form-control"
            [class.error]="loginForm.get('senha')?.invalid && loginForm.get('senha')?.touched"
          >
          <div *ngIf="loginForm.get('senha')?.invalid && loginForm.get('senha')?.touched" class="error-message">
            Senha é obrigatória
          </div>
        </div>

        <button 
          type="submit" 
          [disabled]="loginForm.invalid || loading"
          class="btn btn-primary"
        >
          {{ loading ? 'Entrando...' : 'Entrar' }}
        </button>

        <div *ngIf="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
      </form>
    </div>
  `,
  styles: [`
    .login-container {
      max-width: 400px;
      margin: 50px auto;
      padding: 20px;
      border: 1px solid #ddd;
      border-radius: 8px;
    }
    
    .form-group {
      margin-bottom: 15px;
    }
    
    .form-control {
      width: 100%;
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }
    
    .form-control.error {
      border-color: #dc3545;
    }
    
    .error-message {
      color: #dc3545;
      font-size: 12px;
      margin-top: 5px;
    }
    
    .btn {
      width: 100%;
      padding: 10px;
      background-color: #007bff;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    
    .btn:disabled {
      background-color: #6c757d;
      cursor: not-allowed;
    }
  `]
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loading = true;
      this.errorMessage = '';

      this.authService.login(this.loginForm.value).subscribe({
        next: (response) => {
          this.loading = false;
          console.log('Login realizado com sucesso:', response);
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          this.loading = false;
          this.errorMessage = 'Email ou senha inválidos';
          console.error('Erro no login:', error);
        }
      });
    }
  }
}
```

## Configuração do Backend

### 1. Executar o backend

```bash
# Usando Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Ou usando o JAR
java -jar target/lms-service-1.0.jar --spring.profiles.active=dev
```

### 2. URLs importantes

- **Backend**: http://localhost:8081
- **Swagger**: http://localhost:8081/swagger-ui/index.html
- **Angular**: http://localhost:4200 (padrão)

### 3. Testar CORS

```bash
# Teste simples com curl
curl -X GET http://localhost:8081/categorias-tarefas \
  -H "Origin: http://localhost:4200" \
  -H "Access-Control-Request-Method: GET" \
  -H "Access-Control-Request-Headers: X-Requested-With" \
  -v
```

## Próximos Passos

1. **Criar componentes Angular** para cada funcionalidade
2. **Implementar guards** para proteger rotas
3. **Adicionar tratamento de erros** global
4. **Implementar loading states** nos componentes
5. **Adicionar validações** nos formulários
6. **Criar testes unitários** para os services
