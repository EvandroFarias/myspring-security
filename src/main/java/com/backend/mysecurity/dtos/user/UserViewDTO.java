package com.backend.mysecurity.dtos.user;

import com.backend.mysecurity.models.RoleModel;
import com.backend.mysecurity.models.UserModel;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserViewDTO {

    private UUID id;
    private String email;
    private List<RoleModel> roles;
    private Date createdAt;
    private Boolean enabled;
    private Date lastUpdateDate;

    public static UserViewDTO modelToDTO(UserModel userModel){
        return UserViewDTO.builder()
                .id(userModel.getUserId())
                .email(userModel.getEmail())
                .createdAt(userModel.getCreatedAt())
                .enabled(userModel.getEnabled())
                .lastUpdateDate(userModel.getLastUpdateDate())
                .roles(userModel.getRoles())
                .build();
    }
}
