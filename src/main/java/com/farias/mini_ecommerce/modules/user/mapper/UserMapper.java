package com.farias.mini_ecommerce.modules.user.mapper;

import com.farias.mini_ecommerce.modules.user.dto.UserProfileResponse;
import com.farias.mini_ecommerce.modules.user.entity.User;
import com.farias.mini_ecommerce.modules.user.dto.UserRegisterRequest;
import com.farias.mini_ecommerce.modules.user.dto.UserRegisteredResponse;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userRole", ignore = true)
    User toUser(UserRegisterRequest userRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userRole", ignore = true)
    void updateUser(UserRegisterRequest userRequest, @MappingTarget User user);

    UserRegisteredResponse toUserResponse(User user);

    UserProfileResponse toUserProfileResponse(User user);
}
