package com.example.server.domain.user.Mapper;

import com.example.server.domain.cart.entity.Cart;
import com.example.server.domain.user.dto.UserDto;
import com.example.server.domain.user.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    default User userPostToUser(UserDto.Post postDto){
        if ( postDto == null ) {
            return null;
        }

        User user = new User();
        Cart cart = new Cart();

        user.setLoginId( postDto.getLoginId() );
        user.setName( postDto.getName() );
        user.setPassword( postDto.getPassword() );
        user.setEmail( postDto.getEmail() );
        user.setAddress( postDto.getAddress() );

        cart.setUser(user);
        user.setCart(cart);

        return user;
    }
    User userPatchToUser(UserDto.Patch patchDto);
    UserDto.Response userToUserResponse(User user);
    List<UserDto.Response> usersToUsersResponse(List<User> userList);
}
