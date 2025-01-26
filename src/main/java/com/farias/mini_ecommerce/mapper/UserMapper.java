package com.farias.mini_ecommerce.mapper;

import com.farias.mini_ecommerce.domain.User;
import com.farias.mini_ecommerce.dto.request.UserRequest;
import com.farias.mini_ecommerce.dto.response.UserResponse;
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
    User toUser(UserRequest userRequest);

    UserResponse toUserResponse(User user);
}
