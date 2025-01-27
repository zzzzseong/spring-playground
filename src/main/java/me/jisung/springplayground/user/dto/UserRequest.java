package me.jisung.springplayground.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import me.jisung.springplayground.common.constant.ValidationConst;

@Getter
public class UserRequest {

    public interface Create {}
    public interface Token  {}

    @NotNull(groups = {Create.class, Token.class}, message = ValidationConst.MESSAGE_NOT_NULL + " (email)")
    @Pattern(groups = {Create.class, Token.class}, regexp = ValidationConst.REGEXP_EMAIL, message = ValidationConst.MESSAGE_REGEXP_INVALID + " (email)")
    private String email;

    @NotNull(groups = {Create.class, Token.class}, message = ValidationConst.MESSAGE_NOT_NULL + " (password)")
    @Pattern(groups = {Create.class, Token.class}, regexp = ValidationConst.REGEXP_PASSWORD, message = ValidationConst.MESSAGE_REGEXP_INVALID + " (password)")
    private String password;

    @NotNull(groups = {Create.class}, message = ValidationConst.MESSAGE_NOT_NULL + " (name)")
    private String name;
}
