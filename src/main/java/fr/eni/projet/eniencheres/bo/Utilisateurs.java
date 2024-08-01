package fr.eni.projet.eniencheres.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "UTILISATEURS")
public class Utilisateurs implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "actif", nullable = false)
    private boolean actif = true;

    @Column(nullable = false, length = 30)
    @Size(min = 5, max = 20, message = "{utilisateur.pseudo.size}")
    private String pseudo;

    @Column(nullable = false, length = 30)
    private String nom;

    @Column(nullable = false, length = 30)
    private String prenom;

    @Column(nullable = false, length = 50)
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "{utilisateur.email.pattern}")
    private String email;

    @Column(length = 15)
    private String telephone;

    @ManyToOne
    @JoinColumn(name = "no_adresse", nullable = false)
    private Adresse adresse;

    @Column(nullable = false, length = 30)
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$",
            message = "{utilisateur.motDePasse.pattern}"
    )
    private String motDePasse;

    @Column(nullable = false)
    private BigDecimal credit;

    @Column(nullable = false)
    private boolean admin;

    @OneToMany(mappedBy = "vendeur", cascade = CascadeType.ALL)
    private List<ArticleAVendre> articlesVendus;

    @OneToMany(mappedBy = "acheteur", cascade = CascadeType.ALL)
    private List<Enchere> encheres;

    private String resetPasswordToken;
    private LocalDateTime tokenExpiryDate;

    public Utilisateurs() {
    }

    public Utilisateurs(String pseudo, String nom, String prenom, String email, String telephone, Adresse adresse,
                        String motDePasse, BigDecimal credit, boolean admin, List<ArticleAVendre> articlesVendus) {
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.motDePasse = motDePasse;
        this.credit = credit;
        this.admin = admin;
        this.articlesVendus = articlesVendus;
    }

    public Utilisateurs(Long id, String pseudo, String nom, String prenom, String email, String telephone,
                        Adresse adresse, String motDePasse, boolean admin, BigDecimal credit,
                        List<ArticleAVendre> articlesVendus) {
        this.id = id;
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.motDePasse = motDePasse;
        this.admin = admin;
        this.credit = credit;
        this.articlesVendus = articlesVendus;
    }

    public Utilisateurs(Long id, boolean actif, String pseudo, String nom, String prenom, String telephone,
                        String email, Adresse adresse, BigDecimal credit, String motDePasse, boolean admin,
                        List<ArticleAVendre> articlesVendus, List<Enchere> encheres, String resetPasswordToken,
                        LocalDateTime tokenExpiryDate) {
        this.id = id;
        this.actif = actif;
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
        this.credit = credit;
        this.motDePasse = motDePasse;
        this.admin = admin;
        this.articlesVendus = articlesVendus;
        this.encheres = encheres;
        this.resetPasswordToken = resetPasswordToken;
        this.tokenExpiryDate = tokenExpiryDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Utilisateurs{");
        sb.append("id=").append(id);
        sb.append(", pseudo='").append(pseudo).append('\'');
        sb.append(", nom='").append(nom).append('\'');
        sb.append(", prenom='").append(prenom).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", telephone='").append(telephone).append('\'');
        sb.append(", adresse=").append(adresse);
        sb.append(", credit=").append(credit);
        sb.append(", admin=").append(admin);
        sb.append(", articlesVendus=").append(articlesVendus);
        sb.append(", encheres=").append(encheres);
        sb.append('}');
        return sb.toString();
    }

    public List<Enchere> getEncheres() {
        return encheres;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
        this.tokenExpiryDate = LocalDateTime.now().plusHours(1);
    }

    public LocalDateTime getTokenExpiryDate() {
        return tokenExpiryDate;
    }

    public void setTokenExpiryDate(LocalDateTime tokenExpiryDate) {
        this.tokenExpiryDate = tokenExpiryDate;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public void setEncheres(List<Enchere> encheres) {
        this.encheres = encheres;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public List<ArticleAVendre> getArticlesVendus() {
        return articlesVendus;
    }

    public void setArticlesVendus(List<ArticleAVendre> articlesVendus) {
        this.articlesVendus = articlesVendus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateurs that = (Utilisateurs) o;
        return admin == that.admin && Objects.equals(id, that.id) && Objects.equals(pseudo, that.pseudo) && Objects.equals(nom, that.nom) && Objects.equals(prenom, that.prenom) && Objects.equals(email, that.email) && Objects.equals(telephone, that.telephone) && Objects.equals(adresse, that.adresse) && Objects.equals(motDePasse, that.motDePasse) && Objects.equals(credit, that.credit) && Objects.equals(articlesVendus, that.articlesVendus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pseudo, nom, prenom, email, telephone, adresse, motDePasse, credit, admin, articlesVendus);
    }
}
