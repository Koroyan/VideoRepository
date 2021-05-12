package com.example.accessingdatajpa.repository;

import javax.persistence.*;


@Entity
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String vName;
    private String vDescription;
    @Lob
    private byte[] vUsage;

    public Video() {
    }

    public Video(String vName, String vDescription, byte[] vUsage) {
        this.vName = vName;
        this.vDescription = vDescription;
        this.vUsage = vUsage;
    }

    @Override
    public String toString() {
        return String.format(
                "Video[id=%d, name='%s', description='%s']",
                id, vName, vDescription);
    }

    public Long getId() {
        return id;
    }

    public String getvName() {
        return vName;
    }

    public String getvDescription() {
        return vDescription;
    }



    public byte[] getvUsage() {
        return vUsage;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public void setvDescription(String vDescription) {
        this.vDescription = vDescription;
    }

    public void setvUsage(byte[] vUsage) {
        this.vUsage = vUsage;
    }
}
