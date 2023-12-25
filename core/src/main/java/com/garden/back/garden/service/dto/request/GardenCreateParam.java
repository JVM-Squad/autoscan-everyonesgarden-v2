package com.garden.back.garden.service.dto.request;

import com.garden.back.garden.model.Garden;
import com.garden.back.garden.model.vo.GardenStatus;
import com.garden.back.global.GeometryUtil;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public record GardenCreateParam(
        List<MultipartFile> gardenImages,
        String gardenName,
        String price,
        String size,
        GardenStatus gardenStatus,
        String linkForRequest,
        String contact,
        String address,
        Double latitude,
        Double longitude,
        GardenFacility gardenFacility,
        String gardenDescription,
        LocalDate recruitStartDate,
        LocalDate recruitEndDate,
        LocalDate useStartDate,
        LocalDate useEndDate,
        Long writerId

) {
    public static Garden toEntity(GardenCreateParam param ){
        return Garden.createPrivateGarden(
                param.address(),
                param.latitude(),
                param.longitude(),
                GeometryUtil.createPoint(param.latitude(),param.longitude()),
                param.gardenName(),
                param.gardenStatus(),
                param.linkForRequest(),
                param.price(),
                param.contact(),
                param.size(),
                param.gardenDescription(),
                param.recruitStartDate(),
                param.recruitEndDate(),
                param.useStartDate(),
                param.useEndDate(),
                param.gardenFacility().isToilet(),
                param.gardenFacility().isWaterway(),
                param.gardenFacility().isEquipment(),
                param.writerId()
        );
    }

    public record GardenFacility(
            Boolean isToilet,
            Boolean isWaterway,
            Boolean isEquipment
    ){

    }
}
