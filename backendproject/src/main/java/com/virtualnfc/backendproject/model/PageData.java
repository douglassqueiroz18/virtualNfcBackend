package com.virtualnfc.backendproject.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cartoes")

public class PageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String nomeCartao;
    private String instagram;
    private String whatsapp;
    private String facebook;
    private String linkedin;
    private String tiktok;
    private String youtube;
    private String site;
    private String prototipo;
    public PageData() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getNomeCartao() { return nomeCartao; }
    public void setNomeCartao(String nomeCartao) { this.nomeCartao = nomeCartao; }

    public String getInstagram() { return instagram; }
    public void setInstagram(String instagram) { this.instagram = instagram; }

    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }

    public String getFacebook() { return facebook; }
    public void setFacebook(String facebook) { this.facebook = facebook; }

    public String getLinkedin() { return linkedin; }
    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }

    public String getTiktok() { return tiktok; }
    public void setTiktok(String tiktok) { this.tiktok = tiktok; }

    public String getYoutube() { return youtube; }
    public void setYoutube(String youtube) { this.youtube = youtube; }

    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }

    public String getPrototipo() { return prototipo; }
    public void setPrototipo(String prototipo) { this.prototipo = prototipo; }
}
