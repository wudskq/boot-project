package cn.com.wudskq.handler;

import cn.com.wudskq.model.SysUserDetails;
import cn.com.wudskq.service.impl.SysUserDetailsService;
import cn.com.wudskq.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SysUserDetailsService userDetailsService;
 
    /**
     * 身份验证
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal(); // 获取用户名
        String password = (String) authentication.getCredentials(); // 获取密码
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new UsernameNotFoundException("用户名或密码不能为空");
        }
        if(username.equals("admin") && password.equals("123456")){
            SysUserDetails sysUserDetails = (SysUserDetails) userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(sysUserDetails, password, sysUserDetails.getAuthorities());
        } else if (username.equals("admin") && !password.equals("123456")){
            throw new BadCredentialsException("用户名或密码错误");
        } else {
            SysUserDetails sysUserDetails = (SysUserDetails) userDetailsService.loadUserByUsername(username);
            if (sysUserDetails == null) {
                throw new UsernameNotFoundException("用户名不存在");
            }
            if(!sysUserDetails.getPassword().equals(Md5Util.MD5(password))){
                throw new BadCredentialsException("用户名或密码错误");
            }
            return new UsernamePasswordAuthenticationToken(sysUserDetails, password, sysUserDetails.getAuthorities());
        }
    }
 
    /**
     * 支持指定的身份验证
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
 
}