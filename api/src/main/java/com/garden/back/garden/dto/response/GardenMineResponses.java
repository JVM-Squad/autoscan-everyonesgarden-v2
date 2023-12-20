package com.garden.back.garden.dto.response;

import com.garden.back.garden.service.dto.response.GardenMineResults;

import java.util.List;

public record GardenMineResponses(
        List<GardenMineResponse> gardenMineResponses
) {
    public static GardenMineResponses to(GardenMineResults gardenMineResults) {
        return new GardenMineResponses(
                gardenMineResults.gardenMineResults().stream()
                        .map(GardenMineResponse::to)
                        .toList()
        );
    }

    public record GardenMineResponse(
            Long gardenId,
            String size,
            String gardenName,
            String price,
            String gardenStatus,
            List<String> imageUrls
    ) {
        public static GardenMineResponse to(GardenMineResults.GardenMineResult gardenMineResult) {
            return new GardenMineResponse(
                    gardenMineResult.gardenId(),
                    gardenMineResult.size(),
                    gardenMineResult.gardenName(),
                    gardenMineResult.price(),
                    gardenMineResult.gardenStatus(),
                    gardenMineResult.imageUrls()
            );
        }
    }

}
