package edu.ucsb.cs156.example.repositories;

import edu.ucsb.cs156.example.entities.Article;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The ArticlesRepository is a repository for Articles entities.
 */

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
  /**
   * This method returns all Articles entities with a given quarterYYYYQ.
   * @param quarterYYYYQ quarter in the format YYYYQ (e.g. 20241 for Winter 2024, 20242 for Spring 2024, 20243 for Summer 2024, 20244 for Fall 2024)
   * @return all Articles entities with a given quarterYYYYQ
   */
  Iterable<Article> findAllByQuarterYYYYQ(String quarterYYYYQ);
}