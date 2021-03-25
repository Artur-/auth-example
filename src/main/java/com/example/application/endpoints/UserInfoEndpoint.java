package com.example.application.endpoints;

import com.example.application.security.vaadinspring.SecurityUtils;
import com.vaadin.flow.server.connect.Endpoint;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Endpoint
public class UserInfoEndpoint {

    public UserInfo getUserInfo() {
        UserDetails user = SecurityUtils.getAuthenticatedUser();
        if (user == null) {
            return null;
        }

        String imageUrl;
        if (user.getUsername().equals("John")) {
            imageUrl = "https://flxt.tmsimg.com/v9/AllPhotos/32672/32672_v9_bb.jpg";
        } else {
            imageUrl = "https://i.pinimg.com/564x/3f/bb/b8/3fbbb8bafe3485d18520fe077eba674f.jpg";
        }
        UserInfo userInfo = new UserInfo(user.getUsername(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority), imageUrl);
        return userInfo;
    }
}
