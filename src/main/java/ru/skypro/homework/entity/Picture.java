package ru.skypro.homework.entity;


import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "picture")
public class Picture {

    @Id
    private Integer id;

    @Column
    private String filePath;

    @Column
    private String mediaType;

    @Column
    private long fileSize;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Ads ads;

}
