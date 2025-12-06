package cz.kostka.prizedraw.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
public class DrawResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Vazby
    @ManyToOne(optional = false)
    private Person person;

    @ManyToOne(optional = false)
    private Prize prize;

    // Denormalizované pro rychlé zobrazení (nepovinné, ale užitečné)
    @Column(nullable = false)
    private String clovek; // person.jmeno v čase losování

    @Column(nullable = false)
    private String cena;   // prize.nazev v čase losování

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public DrawResult() {}

    public DrawResult(Person person, Prize prize) {
        this.person = person;
        this.prize = prize;
        this.clovek = person.getJmeno();
        this.cena = prize.getNazev();
    }

    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Prize getPrize() {
        return prize;
    }

    public String getClovek() {
        return clovek;
    }

    public String getCena() {
        return cena;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrawResult that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}