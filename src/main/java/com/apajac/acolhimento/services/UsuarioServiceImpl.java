package com.apajac.acolhimento.services;

import com.apajac.acolhimento.domain.dtos.UsuarioDTO;
import com.apajac.acolhimento.domain.entities.UsuarioEntity;
import com.apajac.acolhimento.domain.enums.AuditoriaEnum;
import com.apajac.acolhimento.exceptions.BusinessException;
import com.apajac.acolhimento.exceptions.NotFoundException;
import com.apajac.acolhimento.repositories.UsuarioRepository;
import com.apajac.acolhimento.services.interfaces.AuditoriaService;
import com.apajac.acolhimento.services.interfaces.UsuarioService;
import com.apajac.acolhimento.utils.ExtrairMessageErroUsuario;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;

    private final AuditoriaService auditoria;
    @Override
    public void cadastrar(UsuarioDTO usuarioDTO) {
        try {
            validDTO(usuarioDTO);

            Optional<UsuarioEntity> existingUser = repository.findByLogin(usuarioDTO.getLogin());
            if (existingUser.isPresent()) {
                throw new BusinessException("Login já existe.");
            }

            UsuarioEntity entity = new UsuarioEntity();
            entity.setNome(usuarioDTO.getNome());
            entity.setRoles(usuarioDTO.getRoles());
            entity.setPassword(encryptPasswordUser(usuarioDTO.getPassword()));
            entity.setLogin(usuarioDTO.getLogin());

            repository.save(entity);

            auditar(entity.toString(), usuarioDTO.getIdResponsavelPeloCadastro(), AuditoriaEnum.CREATED.getValues());

        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ExtrairMessageErroUsuario.extrairMensagemDeErro(e.getMessage()));
        }
    }

    private void auditar(String body, Long idResponsavel, String tipo) {
        auditoria.inserirDadosDeAuditoria(
                idResponsavel,
                tipo,
                UsuarioService.class.getSimpleName(),
                body);
    }

    private String encryptPasswordUser(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private void validDTO(UsuarioDTO usuario) {
        if (isNull(usuario)) {
            throw new IllegalArgumentException("O DTO do usuário não pode ser nulo.");
        } else if (isNull(usuario.getNome())||usuario.getNome().isBlank()) {
            throw new IllegalArgumentException("O Nome do usuário não pode ser nulo/vazio.");
        } else if (isNull(usuario.getRoles())||usuario.getRoles().isEmpty()) {
            throw new IllegalArgumentException("Os Papéis do usuário não podem ser nulos/vazios.");
        } else if (isNull(usuario.getLogin())||usuario.getLogin().isBlank()) {
            throw new IllegalArgumentException("O Login do usuário não pode ser nulo/vazio.");
        } else if (isNull(usuario.getPassword())||usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("A Senha do usuário não pode ser nula/vazia.");
        }else if (usuario.getPassword().length() < 6) {
            throw new IllegalArgumentException("A Senha do usuário não pode ser menor que 6 dígitos.");
        }else if (isNull(usuario.getIdResponsavelPeloCadastro())) {
            throw new IllegalArgumentException("O ID de Responsável não pode ser nulo.");
        }

    }
    @Override
    public Page<UsuarioEntity> listarUsuarios(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void remover(Long id) {
        Optional<UsuarioEntity> usuario = repository.findById(id);
        if (usuario.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado.");
        }

        validaUsuarioRoot(usuario.get());

        repository.delete(usuario.get());
    }

    @Override
    public UsuarioEntity buscarUsuarioPorId(Long id) {
        Optional<UsuarioEntity> optionalUsuario = repository.findById(id);
        if (optionalUsuario.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado.");
        }
        return optionalUsuario.get();
    }

    @Override
    public Page<UsuarioEntity> buscarUsuariosPorNome(String nome, Pageable pageable) {
        return repository.findAllByNomeContainingIgnoreCase(nome, pageable);
    }

    private void validaUsuarioRoot(UsuarioEntity usuarioEntity) {
        boolean root = usuarioEntity.getRoles().contains("root");
        if (root){
            throw new BusinessException("Usuário ROOT não pode ser removido ou alterado.");
        }
    }

}
