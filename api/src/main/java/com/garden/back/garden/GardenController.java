package com.garden.back.garden;

import com.garden.back.garden.dto.request.GardenByNameRequest;
import com.garden.back.garden.dto.request.GardenByComplexesRequest;
import com.garden.back.garden.dto.request.GardenGetAllRequest;
import com.garden.back.garden.dto.response.GardenByComplexesResponses;
import com.garden.back.garden.dto.response.GardenByNameResponses;
import com.garden.back.garden.dto.response.GardenGetAllResponses;
import com.garden.back.garden.service.GardenReadService;
import com.garden.back.garden.service.dto.request.GardenByNameParam;
import com.garden.back.garden.service.dto.request.GardenGetAllParam;
import com.garden.back.garden.service.dto.response.GardenByComplexesResults;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/gardens")
public class GardenController {

    private final GardenReadService gardenReadService;

    public GardenController(GardenReadService gardenReadService) {
        this.gardenReadService = gardenReadService;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GardenByNameResponses> getGardenNames(
            @Valid @ModelAttribute GardenByNameRequest gardenByNameRequest) {
        GardenByNameParam gardenByNameParam = GardenByNameRequest.to(gardenByNameRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GardenByNameResponses.to(gardenReadService.getGardensByName(gardenByNameParam)));
    }

    @GetMapping(
            path = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GardenGetAllResponses> getGardenAll(
            @Valid @ModelAttribute GardenGetAllRequest gardenGetAllRequest) {
        GardenGetAllParam gardenGetAllParam = GardenGetAllRequest.to(gardenGetAllRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GardenGetAllResponses.to(
                        gardenReadService.getAllGarden(gardenGetAllParam)));
    }

    @GetMapping(
            path = "/by-complexes",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GardenByComplexesResponses> getGardensByComplexes(
            @Valid @ModelAttribute GardenByComplexesRequest request) {
        GardenByComplexesResults gardensByComplexes
                = gardenReadService.getGardensByComplexes(GardenByComplexesRequest.to(request));

        return ResponseEntity.status(HttpStatus.OK)
                .body(GardenByComplexesResponses.to(gardensByComplexes));

    }

}
