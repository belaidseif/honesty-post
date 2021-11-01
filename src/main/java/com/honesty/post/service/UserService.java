package com.honesty.post.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
public class UserService {

    public UUID getUserUid(HttpServletRequest request){
        String username = String.valueOf(request.getAttribute("username"));
        if(!username.equals("null"))
            return UUID.fromString(username);
        return null;
    }


}
