package com.example.demo.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class AdultNIC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userPhotoName;

    private String PhotoType;

    //columnDefinition = "LONGBLOB"
    @Lob
    @Column(name = "Data", columnDefinition = "LONGBLOB" )
    private byte[] data;

    @OneToOne(mappedBy = "adultNIC")
    private User user;
}
