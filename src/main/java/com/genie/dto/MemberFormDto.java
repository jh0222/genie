package com.genie.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberFormDto {
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String memberId;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Length(min=8,max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    private String memberPassword;

    @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
    private String memberPasswordCheck;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String memberName;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String memberEmail;
}
