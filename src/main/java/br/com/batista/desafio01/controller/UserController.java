package br.com.batista.desafio01.controller;

import br.com.batista.desafio01.configuration.message.MessageService;
import br.com.batista.desafio01.model.dto.user.UserDTO;
import br.com.batista.desafio01.model.dto.base.BaseReturnDTO;
import br.com.batista.desafio01.model.dto.user.UserSearchDTO;
import br.com.batista.desafio01.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    MessageService messageService;

    public UserController(){

    }

    @PostMapping(path="create-update")
    @Operation(
            summary = "user.controller.create.hint",
            description = "user.controller.create.message"
    )
    public ResponseEntity<UserDTO> create(@Schema(description = "user.controller.create.hint")  @Valid @RequestBody UserDTO userDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(userService.createUpdate(userDTO)));
    }
    @DeleteMapping
    @Operation(
            summary = "user.controller.delete.hint"
    )
    public ResponseEntity<BaseReturnDTO> delete(@Schema(description = "user.controller.delete.param.hint") @ParameterObject String param) throws Exception {

        userService.delete(param);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseReturnDTO(
                messageService.getMessage("default.status.success"),
                messageService.getMessage("default.message.success")
        ));
    }

    @GetMapping(path = "search")
    @Operation(
            summary = "user.controller.search.hint"
    )
    public Page<UserDTO> get(@ParameterObject UserSearchDTO search, @ParameterObject Pageable pageable) throws Exception {
        return userService.search(search, pageable);
    }
}
