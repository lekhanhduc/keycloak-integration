package vn.khanhduc.integratekeycloak.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "profiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String profileId;

    private String userId;

    private String email;

    private String username;

    private String firstName;

    private String lastName;

    private LocalDate dob;

}
