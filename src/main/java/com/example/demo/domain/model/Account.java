package com.example.demo.domain.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    private String username;

    @Size(max = 50)
    @Email
    private String email;

    @Size(max = 120)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Role role;

    @Column(name = "is_block")
    private Boolean isBLock;

    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "card_id")
    private String cardId;
    @Column(name = "gender")
    private String gender;
    @Column(name = "point")
    private Long point;

    @Column(name = "bod")
    private LocalDate bod;

    @OneToMany(mappedBy = "account")
    private List<Vehicle> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Booking> bookings = new ArrayList<>();

    @Column(name = "image")
    private String image;


    public Account(String username, String email, String password, Role role, Boolean isBLock, String name, String phone, String address, String cardId, String gender, Long point, LocalDate bod) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isBLock = isBLock;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.cardId = cardId;
        this.gender = gender;
        this.point = point;
        this.bod = bod;
    }
}
