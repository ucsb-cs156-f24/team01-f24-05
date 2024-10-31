package edu.ucsb.cs156.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is a JPA entity that represents a UCSBDiningCommonsMenuItem, i.e. an entry
 * that comes from the UCSB API for dining hall menu items.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "ucsbdiningcommonsmenuitem")
public class UCSBDiningCommonsMenuItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String diningCommonsCode;
  private String name;
  private String station;
}
