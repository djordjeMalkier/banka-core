package common.bankarskiSistem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@DynamicUpdate
public class User implements UserDetails {
    @Id
    @Column(name="personal_id", nullable = false)
    private String personalId;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="surname", nullable = false)
    private String surname;

    @Column(name="address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccount> bankAccounts = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "personal_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String personalId, String name, String surname, String address) {
        this.personalId = personalId;
        this.name = name;
        this.surname = surname;
        this.address = address;
       /* this.bankAccounts = new ArrayList<>();*/
    }

    public User(String personalId, String name, String surname, String address, String password) {
        this.personalId = personalId;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.password = password;
    }


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", ID='" + personalId + '\'' +
                ", address='" + address + '\'' +
                "}";
    }

    public void addAccount(BankAccount bankAccount) {
        bankAccounts.add(bankAccount);
        bankAccount.setUser(this);
    }

    public void remove(BankAccount bankAccount) {
        bankAccounts.remove(bankAccount);
        bankAccount.setUser(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return personalId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //TODO
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; //TODO
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; //TODO
    }

    @Override
    public boolean isEnabled() {
        return true; //TODO
    }

    public void eraseCredentials(){
        this.password = null;
    }
}
