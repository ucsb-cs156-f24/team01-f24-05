package edu.ucsb.cs156.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "ucsborganizations")
public class UCSBOrganizations {
  @Id
  private String orgCode;
  private String orgTranslationShort;
  private String orgTranslation;
  private boolean inactive;
}