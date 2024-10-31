package edu.ucsb.cs156.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * This is a JPA entity that represents a RecommendationRequest, i.e. an entry
 * that comes from the Recommendation Request API for recommendation requests. 
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "recommendationrequest")
public class RecommendationRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String requesterEmail;
  private String professorEmail;
  private String explanation;
  private LocalDateTime dateRequested;
  private LocalDateTime dateNeeded;
  private boolean done;
}