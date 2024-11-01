package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.UCSBOrganizations;
import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.UCSBOrganizationsRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * This is a REST controller for UCSBOrganizations
 */

@Tag(name = "UCSBOrganizations")
@RequestMapping("/api/ucsborganizations")
@RestController
@Slf4j

public class UCSBOrganizationsController extends ApiController {

    @Autowired

    UCSBOrganizationsRepository ucsbOrganizationsRepository;

    @Operation(summary= "List all ucsb organizations")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<UCSBOrganizations> allOrganizations() {
        Iterable<UCSBOrganizations> organizations = ucsbOrganizationsRepository.findAll();
        return organizations;
    }

    /**
     * This method returns a single organizations.
     * @param code code of the organizations
     * @return a single organizations
     */
    @Operation(summary= "Get a single organization")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public UCSBOrganizations getById(
            @Parameter(name="id") @RequestParam String id) {
        UCSBOrganizations organizations = ucsbOrganizationsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(UCSBOrganizations.class, id));

        return organizations;
    }

    /**
     * This method creates a new organizations. Accessible only to users with the role "ROLE_ADMIN".
     * @param code code of the organizations
     * @param name name of the organizations
     * @param hasSackMeal whether or not the commons has sack meals
     * @param hasTakeOutMeal whether or not the commons has take out meals
     * @param hasDiningCam whether or not the commons has a dining cam
     * @param latitude latitude of the commons
     * @param longitude logitude of the commons
     * @return the save organizations
     */
    @Operation(summary= "Create a new organizations")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public UCSBOrganizations postOrganizations(
        @Parameter(name="orgCode") @RequestParam String orgCode,
        @Parameter(name="orgTranslationShort") @RequestParam String orgTranslationShort,
        @Parameter(name="orgTranslation") @RequestParam String orgTranslation,
        @Parameter(name="inactive") @RequestParam boolean inactive
        )
        {

        UCSBOrganizations organizations = new UCSBOrganizations();
        organizations.setOrgCode(orgCode);
        organizations.setOrgTranslationShort(orgTranslationShort);
        organizations.setOrgTranslation(orgTranslation);
        organizations.setInactive(inactive);

        UCSBOrganizations savedorganizations = ucsbOrganizationsRepository.save(organizations);

        return savedorganizations;
    }

    /**
     * Delete an organization. Accessible only to users with the role "ROLE_ADMIN".
     * @param code code of the organization
     * @return a message indiciating the commons was deleted
     */
    @Operation(summary= "Delete a UCSBOrganization")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public Object deleteOrganizations(
            @Parameter(name="id") @RequestParam String id) {
        UCSBOrganizations organizations = ucsbOrganizationsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(UCSBOrganizations.class, id));

        ucsbOrganizationsRepository.delete(organizations);
        return genericMessage("UCSBOrganizations with id %s deleted".formatted(id));
    }
    /**
    **
     * Update a single organizations. Accessible only to users with the role "ROLE_ADMIN".
     * @param code code of the organizations
     * @param incoming the new commons contents
     * @return the updated commons object
     */
    @Operation(summary= "Update a single organization")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public UCSBOrganizations updateOrganization(
            @Parameter(name="id") @RequestParam String id,
            @RequestBody @Valid UCSBOrganizations incoming) {

        UCSBOrganizations organizations = ucsbOrganizationsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(UCSBOrganizations.class, id));


        organizations.setOrgCode(incoming.getOrgCode());
        organizations.setOrgTranslationShort(incoming.getOrgTranslationShort());
        organizations.setOrgTranslation(incoming.getOrgTranslation());
        organizations.setInactive(incoming.getInactive());

        ucsbOrganizationsRepository.save(organizations);

        return organizations;
    }

    
}