package com.example.demo.model.request;

import com.example.demo.model.BaseRequest;
import com.example.demo.util.constant.RegExPattern;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest implements BaseRequest {

    @NotBlank
    @Length(max = 20)
    private String username;

    @NotBlank
    @Length(min = 8)
    private String password;

    @Pattern(regexp = RegExPattern.EMAIL_VALIDATION_PATTERN)
    private String email;

    @NotBlank
    @Length(max = 100)
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank
    @Length(max = 100)
    @JsonProperty("last_name")
    private String lastName;

    @NotNull
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @NotBlank
    @Length(max = 2000)
    private String address;

    @NotBlank
    @Length(max = 13)
    @Pattern(regexp = "^[0-9]{9,10}$")
    @JsonProperty("tel_no")
    private String telNo;


}
