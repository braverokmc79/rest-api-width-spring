package net.macaronics.restapi.accounts;


import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class AccountService implements UserDetails {

    private  Account account;

    public AccountService(Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getAuthority() {

                return account.getRoles().toString();
            }
        });
        return collect;
    }

    /**
     * 사용자를 인증하는 데 사용된 암호를 반환합니다.
     */
    @Override
    public String getPassword() {
        return account.getPassword();
    }

    /**
     * 사용자를 인증하는 데 사용된 사용자 이름을 반환합니다. null을 반환할 수 없습니다.
     */
    @Override
    public String getUsername() {
        return account.getEmail();
    }

    /**
     * 사용자의 계정이 만료되었는지 여부를 나타냅니다. 만료된 계정은 인증할 수 없습니다.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 사용자가 잠겨 있는지 또는 잠금 해제되어 있는지 나타냅니다. 잠긴 사용자는 인증할 수 없습니다.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 사용자의 자격 증명(암호)이 만료되었는지 여부를 나타냅니다. 만료된 자격 증명은 인증을 방지합니다.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 사용자가 활성화되었는지 비활성화되었는지 여부를 나타냅니다. 비활성화된 사용자는 인증할 수 없습니다.
     */
    @Override
    public boolean isEnabled() {
        // 우리 사이트 1년동안 회원이 로그인을 안하면!! 휴먼 계정으로 하기로 함.
        // 현재시간-로긴시간=>1년을 초과하면 return false;
        return true;
    }

    public boolean isWriteEnabled() {
        if (account.getRoles().equals(AccountRole.USER))
            return false;
        else
            return true;
    }

    public boolean isWriteAdminAndManagerEnabled() {
        if (account.getRoles().equals(AccountRole.ADMIN) || account.getRoles().equals(AccountRole.USER))
            return true;
        else
            return false;
    }

    public void saveAccount(Account keesun) {
    }


}
