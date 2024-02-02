package world.grendel.cringeit.models;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * User
 */
@Entity
@JsonIgnoreProperties({"email", "passwordHash", "isAdmin", "createdAt", "updatedAt", "cringe", "cringeRatings", "comments", "commentRatings", "whines"})
@Table(name = "springe_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    public static final String sessionKey = "currentUserId";
    @Transient
    public static final String modelKey = "currentUser";

    @NotEmpty
    @Length(min = 3, max = 30)
    private String username;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 60, max = 60)
    @Column(columnDefinition = "CHAR(60)")
    private String passwordHash;

    @NotNull
    private Boolean isAdmin = false;

    @Column(updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Cringe> cringe;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<CringeRating> cringeRatings;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Comment> comments;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<CommentRating> commentRatings;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "springe_whines",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "cringe_id")
    )
    private Set<Cringe> whines;

    public User() {
    }

    public Integer getTotalCringe() {
        return
        cringe.stream()
            .map(c -> c.getTotalRating())
            .reduce(0, (sum, rating) -> sum + rating)
        +
        comments.stream()
            .map(com -> com.getTotalRating())
            .reduce(0, (sum, rating) -> sum + rating);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    public List<Cringe> getCringe() {
        return cringe;
    }

    public void setCringe(List<Cringe> cringe) {
        this.cringe = cringe;
    }

    public Set<CringeRating> getCringeRatings() {
        return cringeRatings;
    }

    public void setCringeRatings(Set<CringeRating> cringeRatings) {
        this.cringeRatings = cringeRatings;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<CommentRating> getCommentRatings() {
        return commentRatings;
    }

    public void setCommentRatings(Set<CommentRating> commentRatings) {
        this.commentRatings = commentRatings;
    }

    public Set<Cringe> getWhines() {
        return whines;
    }

    public void setWhines(Set<Cringe> whines) {
        this.whines = whines;
    }
}
