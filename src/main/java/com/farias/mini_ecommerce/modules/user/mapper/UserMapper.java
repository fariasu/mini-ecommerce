package com.farias.mini_ecommerce.modules.user.mapper;

import com.farias.mini_ecommerce.modules.user.entity.User;
import com.farias.mini_ecommerce.modules.user.dto.UserRegisterRequest;
import com.farias.mini_ecommerce.modules.user.dto.UserRegisteredResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userRole", ignore = true)
    User toUser(UserRegisterRequest userRequest);

    UserRegisteredResponse toUserResponse(User user);
}
