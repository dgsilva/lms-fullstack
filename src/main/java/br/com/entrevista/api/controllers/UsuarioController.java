package br.com.entrevista.api.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.entrevista.api.dto.RegistrarUsuarioDTO;
import br.com.entrevista.api.dto.UsuarioResponseDTO;
import br.com.entrevista.api.entities.Usuario;
import br.com.entrevista.api.repositories.UsuarioRepository;
import br.com.entrevista.api.servicies.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	
	private final UsuarioService usuarioService;
	private final UsuarioRepository usuarioRepository;
	private final ModelMapper modelMapper;

    public UsuarioController(UsuarioService usuarioService, ModelMapper modelMapper, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.modelMapper = modelMapper;
        this.usuarioRepository = usuarioRepository;
    }
    
	@PostMapping("/registro")
	public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody RegistrarUsuarioDTO dto) {
		Usuario u = usuarioService.registrarEstudante(dto);
		UsuarioResponseDTO response = modelMapper.map(u, UsuarioResponseDTO.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PostMapping("/admin")
	public ResponseEntity<UsuarioResponseDTO> criarAdmin(@RequestBody RegistrarUsuarioDTO dto) {
		Usuario u = usuarioService.criarAdmin(dto);
		UsuarioResponseDTO response = modelMapper.map(u, UsuarioResponseDTO.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	

}
