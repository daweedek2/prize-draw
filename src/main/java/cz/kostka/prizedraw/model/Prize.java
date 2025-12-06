package cz.kostka.prizedraw.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Prize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nazev;

    @Column(nullable = false)
    private boolean assigned = false;

    // Pořadí losování (nižší číslo = dřív). Může být null => bere se jako 0.
    @Column
    private Integer orderIndex = 0;

    public Prize() {}

    public Prize(String nazev) {
        this.nazev = nazev;
    }

    public Long getId() {
        return id;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public Integer getOrderIndex() {
        return orderIndex == null ? 0 : orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prize prize)) return false;
        return Objects.equals(id, prize.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}