package com.practice.boardpratice.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberForm {

    @NotBlank(message = "비어있으면 안돼")
    @Column(unique = true)
    @Email
    private String email;
    @NotBlank(message = "비어있으면 안돼용")
    @Size(min = 4, message = "4글자 이상이어야합니다.")
    private String password;
    @NotBlank
    @Column(unique = true)
    @Size(min = 2,max = 15, message = "2글자 이상 15글자 이하이어야합니다.")
    private String nickname;
}
