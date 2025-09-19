package br.com.entrevista.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.entrevista.api.entities.CategoriaTarefa;
import br.com.entrevista.api.entities.Roles;
import br.com.entrevista.api.repositories.CategoriaTarefaRepository;
import br.com.entrevista.api.repositories.RolesRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolesRepository rolesRepository;
    private final CategoriaTarefaRepository categoriaTarefaRepository;

    public DataInitializer(RolesRepository rolesRepository, CategoriaTarefaRepository categoriaTarefaRepository) {
        this.rolesRepository = rolesRepository;
        this.categoriaTarefaRepository = categoriaTarefaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Inicializar roles
        if (rolesRepository.count() == 0) {
            Roles adminRole = new Roles();
            adminRole.setNome("ADMIN");
            rolesRepository.save(adminRole);

            Roles estudanteRole = new Roles();
            estudanteRole.setNome("ESTUDANTE");
            rolesRepository.save(estudanteRole);
        }

        // Inicializar categorias de tarefas
        if (categoriaTarefaRepository.count() == 0) {
            CategoriaTarefa pesquisa = new CategoriaTarefa();
            pesquisa.setCodigo("PESQUISA");
            pesquisa.setNome("Pesquisa");
            categoriaTarefaRepository.save(pesquisa);

            CategoriaTarefa pratica = new CategoriaTarefa();
            pratica.setCodigo("PRATICA");
            pratica.setNome("Prática");
            categoriaTarefaRepository.save(pratica);

            CategoriaTarefa videoaula = new CategoriaTarefa();
            videoaula.setCodigo("ASSISTIR_VIDEOAULA");
            videoaula.setNome("Assistir Videoaula");
            categoriaTarefaRepository.save(videoaula);
        }
    }
}
