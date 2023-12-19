package com.garden.back.garden.service;

import com.garden.back.garden.repository.garden.GardenRepository;
import com.garden.back.garden.repository.garden.dto.GardenGetAll;
import com.garden.back.garden.repository.garden.dto.GardensByComplexes;
import com.garden.back.garden.repository.garden.dto.response.GardenDetailRepositoryResponse;
import com.garden.back.garden.service.dto.request.*;
import com.garden.back.garden.service.dto.response.GardenAllResults;
import com.garden.back.garden.service.dto.response.GardenByComplexesResults;
import com.garden.back.garden.service.dto.response.GardenByNameResults;
import com.garden.back.garden.service.dto.response.GardenDetailResult;
import com.garden.back.garden.service.recentview.GardenHistoryManager;
import com.garden.back.garden.service.recentview.RecentViewGarden;
import com.garden.back.garden.util.PageMaker;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GardenReadService {
    private final GardenRepository gardenRepository;
    private final GardenHistoryManager gardenHistoryManager;

    public GardenReadService(GardenRepository gardenRepository, GardenHistoryManager gardenHistoryManager) {
        this.gardenRepository = gardenRepository;
        this.gardenHistoryManager = gardenHistoryManager;
    }

    public GardenByNameResults getGardensByName(GardenByNameParam gardenByNameParam) {
        return GardenByNameResults.to(gardenRepository.findGardensByName(
                gardenByNameParam.gardenName(),
                PageMaker.makePage(gardenByNameParam.pageNumber())));
    }

    public GardenAllResults getAllGarden(GardenGetAllParam param) {
        Slice<GardenGetAll> gardens = gardenRepository.getAllGardens(
                PageMaker.makePage(param.pageNumber()),
                param.memberId());

        return GardenAllResults.of(gardens);
    }

    public GardenByComplexesResults getGardensByComplexes(GardenByComplexesParam param) {
        GardensByComplexes gardensByComplexes
                = gardenRepository.getGardensByComplexes(GardenByComplexesParam.to(param));

        return GardenByComplexesResults.of(gardensByComplexes);
    }

    public GardenDetailResult getGardenDetail(GardenDetailParam param) {
        List<GardenDetailRepositoryResponse> gardenDetail = gardenRepository.getGardenDetail(
                param.memberId(),
                param.gardenId());
        GardenDetailResult gardenDetailResult = GardenDetailResult.to(gardenDetail);

        gardenHistoryManager.addRecentViewGarden(
                param.memberId(),
                RecentViewGarden.to(gardenDetailResult));

        return gardenDetailResult;
    }

}
