package github.eked.emission.service;

import github.eked.emission.repo.ApplicationUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {
    private ApplicationUserRepo applicationUserRepo;

    public UserDetailsServiceImpl(ApplicationUserRepo applicationUserRepo) {
        this.applicationUserRepo = applicationUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(" loadUserByUsername  {} ",username);
        User user = Optional.ofNullable(applicationUserRepo.findByUserName(username))
                .map(a -> new User(a.getUserName(), a.getPassword(), getAuthorities(username)))
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
        log.info(" loadUserByUsername returning {} ",user);
        return user;

    }
    //todo: get from ldap
    private Collection<? extends GrantedAuthority> getAuthorities(String userName) {
        return Arrays.<GrantedAuthority>asList(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("READ_PRIVILEGE"));
    }
}
