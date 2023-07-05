package net.macaronics.restapi.accounts;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class PrincipalDetailsService implements UserDetailsService{


    private final AccountRepository accountRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public Account saveAccount(Account account){
        String passwordEncode=this.passwordEncoder.encode(account.getPassword());
        log.info("패스워드 암화화 :   {} ",passwordEncode );
        account.setPassword(passwordEncode);
        return this.accountRepository.save(account);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account= accountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return  new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
    }

    private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles){
        return  roles.stream().map(r->new SimpleGrantedAuthority("ROLE_"+r.name()))
                .collect(Collectors.toSet());
    }


}



