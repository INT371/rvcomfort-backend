package com.example.demo.mapper;

import com.example.demo.model.entity.User;
import com.example.demo.model.request.UserRegistrationRequest;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.ZonedDateTime;

@Mapper
public interface UserRegistrationMapper {

    UserRegistrationMapper INSTANCE = Mappers.getMapper(UserRegistrationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "dateOfBirth", source = "user.dateOfBirth")
    @Mapping(target = "address", source = "user.address")
    @Mapping(target = "telNo", source = "user.telNo")
    @Mapping(target = "userType", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "createdAt", ignore = true)
    User from(UserRegistrationRequest user, String password);

    @AfterMapping
    default void after(@MappingTarget User.UserBuilder target, UserRegistrationRequest user, String password) {
        target.id(0);
        target.userType("user");
        target.createdAt(ZonedDateTime.now());
        target.isEnabled(Boolean.TRUE);
        target.isNonLocked(Boolean.TRUE);
        target.updatedAt(null);
    }

}
