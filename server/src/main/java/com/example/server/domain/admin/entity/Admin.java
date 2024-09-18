package com.example.server.domain.admin.entity;

import com.example.server.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Entity(name = "admin")
@Setter
@Getter
@NoArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column
    private String loginId;

    @Column
    private String password;

    @OneToOne(mappedBy = "admin")
    private User user;

}
