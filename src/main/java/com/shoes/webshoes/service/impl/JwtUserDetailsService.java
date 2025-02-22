package com.shoes.webshoes.service.impl;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shoes.webshoes.common.enums.RoleEnum;
import com.shoes.webshoes.dao.UserDao;
import com.shoes.webshoes.entity.Users;

@Service("UserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = null;
		try {
			user = userDao.findUsersByUsersName(username);

		} catch (Exception e) {
			e.printStackTrace();
		}
		String roleString = "";
		if (user.getRole() == RoleEnum.USER.getValue()) {
			roleString = "USER";
		}  else if (user.getRole() == RoleEnum.ADMIN.getValue()) {
			roleString = "ADMIN";
		}

		if (user.getUserName().equals(username)) {
			return new User(user.getUserName(), user.getPassword(), getAuthorities(roleString));
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return Collections.singletonList(new SimpleGrantedAuthority(role));
	}
}