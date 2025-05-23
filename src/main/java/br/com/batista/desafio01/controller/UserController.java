package br.com.batista.desafio01.controller;

import br.com.batista.desafio01.configuration.message.MessageService;
import br.com.batista.desafio01.model.dto.user.UserDTO;
import br.com.batista.desafio01.model.dto.base.BaseReturnDTO;
import br.com.batista.desafio01.model.dto.user.UserSearchDTO;
import br.com.batista.desafio01.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class UserController {

    private final IUserService userService;

    private final MessageService messageService;

    public UserController(IUserService userService, MessageService messageService){
        this.userService = userService;
        this.messageService =messageService;
    }

    @PostMapping(path="create")
    @Operation(
            summary = "user.controller.create.hint",
            description = "user.controller.create.message"
    )
    public ResponseEntity<UserDTO> create(@Schema(description = "user.controller.create.hint")  @Valid @RequestBody UserDTO userDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(userService.create(userDTO)));
    }

    @PatchMapping(path="update")
    @Operation(
            summary = "user.controller.update.hint",
            description = "user.controller.update.message"
    )
    public ResponseEntity<UserDTO> update(@Schema(description = "user.controller.update.hint")  @Valid @RequestBody UserDTO userDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(userService.update(userDTO)));
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
    public Page<UserDTO> get(@ParameterObject UserSearchDTO search, @ParameterObject Pageable pageable) {
        return userService.search(search, pageable);
    }
}
