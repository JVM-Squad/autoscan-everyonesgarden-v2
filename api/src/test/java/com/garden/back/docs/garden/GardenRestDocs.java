package com.garden.back.docs.garden;

import com.garden.back.crop.service.response.MonthlyRecommendedCropsResponse;
import com.garden.back.docs.RestDocsSupport;
import com.garden.back.docs.crop.CropFixture;
import com.garden.back.garden.GardenController;
import com.garden.back.garden.dto.response.GardenByNameResponses;
import com.garden.back.garden.service.GardenCommandService;
import com.garden.back.garden.service.GardenReadService;
import com.garden.back.garden.service.dto.response.GardenAllResults;
import com.garden.back.garden.service.dto.response.GardenByComplexesResults;
import com.garden.back.garden.service.dto.response.GardenByNameResults;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GardenRestDocs extends RestDocsSupport {

    GardenReadService gardenReadService = mock(GardenReadService.class);
    GardenCommandService gardenCommandService = mock(GardenCommandService.class);

    @Override
    protected Object initController() {
        return new GardenController(gardenReadService,gardenCommandService);
    }

    @DisplayName("텃밭 이름으로 검색이 가능하다.")
    @Test
    void getGardenByName() throws Exception {
        GardenByNameResults gardenByNameResults = GardenFixture.gardenByNameResults();
        given(gardenReadService.getGardensByName(any())).willReturn(gardenByNameResults);

        mockMvc.perform(get("/v2/gardens")
                        .param("gardenName","도연")
                        .param("pageNumber","0")
                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-gardens-by-name",
                        queryParameters(
                                parameterWithName("gardenName").description("검색하고자 하는 텃밭 이름"),
                                parameterWithName("pageNumber").description("요구하는 페이지 수")
                        ),
                        responseFields(
                                fieldWithPath("gardenSearchResponses").type(JsonFieldType.ARRAY).description("텃밭 이름 검색 결과"),
                                fieldWithPath("gardenSearchResponses[].gardenId").type(JsonFieldType.NUMBER).description("텃밭 ID"),
                                fieldWithPath("gardenSearchResponses[].gardenName").type(JsonFieldType.STRING).description("텃밭 이름"),
                                fieldWithPath("gardenSearchResponses[].address").type(JsonFieldType.STRING).description("텃밭 주소"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )));

    }

    @DisplayName("모든 텃밭을 조회한다.")
    @Test
    void getAllGardens() throws Exception {
        GardenAllResults gardenAllResults = GardenFixture.gardenAllResults();
        given(gardenReadService.getAllGarden(any())).willReturn(gardenAllResults);

        mockMvc.perform(get("/v2/gardens/all")
                .param("pageNumber","0")
                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-all-gardens",
                        queryParameters(
                                parameterWithName("pageNumber").description("요구하는 페이짓 수")
                        ),
                        responseFields(
                                fieldWithPath("gardenGetAllResponses").type(JsonFieldType.ARRAY).description("텃밭 전체 검색 결과"),
                                fieldWithPath("gardenGetAllResponses[].gardenId").type(JsonFieldType.NUMBER).description("텃밭 아이디"),
                                fieldWithPath("gardenGetAllResponses[].latitude").type(JsonFieldType.NUMBER).description("텃밭 위도"),
                                fieldWithPath("gardenGetAllResponses[].longitude").type(JsonFieldType.NUMBER).description("텃밭 경도"),
                                fieldWithPath("gardenGetAllResponses[].gardenName").type(JsonFieldType.STRING).description("텃밭 이름"),
                                fieldWithPath("gardenGetAllResponses[].gardenType").type(JsonFieldType.STRING).description("텃밭 타입 : PRIVATE(민간), PUBLIC(공공)"),
                                fieldWithPath("gardenGetAllResponses[].price").type(JsonFieldType.STRING).description("텃밭 가격"),
                                fieldWithPath("gardenGetAllResponses[].size").type(JsonFieldType.STRING).description("텃밭 크기"),
                                fieldWithPath("gardenGetAllResponses[].gardenStatus").type(JsonFieldType.STRING).description("텃밭 상태 : ACTIVE(모집중), INACTIVE(마감)"),
                                fieldWithPath("gardenGetAllResponses[].images").type(JsonFieldType.ARRAY).description("텃밭 이미지"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )));
    }

    @DisplayName("해당 화면에 위치한 텃밭 정보를 반환한다.")
    @Test
    void getGardensByComplexes() throws Exception {
        GardenByComplexesResults gardenByComplexesResults = GardenFixture.gardenByComplexesResults();
        given(gardenReadService.getGardensByComplexes(any())).willReturn(gardenByComplexesResults);

        mockMvc.perform(get("/v2/gardens/by-complexes")
                .param("gardenType","PUBLIC")
                .param("pageNumber","0")
                .param("startLat","37.2449168")
                .param("startLong","127.1288684")
                .param("endLat","37.4449168")
                .param("endLong","127.1388684")
                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-gardens-by-complexes",
                        queryParameters(
                                parameterWithName("gardenType").description("텃밭 타입 : ALL(모두),PRIVATE(민간), PUBLIC(공공) "),
                                parameterWithName("pageNumber").description("요청하는 페이지 수"),
                                parameterWithName("startLat").description("북서쪽 위도"),
                                parameterWithName("startLong").description("북서쪽 경도"),
                                parameterWithName("endLat").description("남동쪽 위도"),
                                parameterWithName("endLong").description("남동쪽 경도")
                        ),
                        responseFields(
                                fieldWithPath("gardenByComplexesResponses").type(JsonFieldType.ARRAY).description("위치 및 타입에 따른 텃밭 검색"),
                                fieldWithPath("gardenByComplexesResponses[].gardenId").type(JsonFieldType.NUMBER).description("텃밭 아이디"),
                                fieldWithPath("gardenByComplexesResponses[].size").type(JsonFieldType.STRING).description("텃밭 크기"),
                                fieldWithPath("gardenByComplexesResponses[].gardenName").type(JsonFieldType.STRING).description("텃밭 이름"),
                                fieldWithPath("gardenByComplexesResponses[].price").type(JsonFieldType.STRING).description("텃밭 가격"),
                                fieldWithPath("gardenByComplexesResponses[].images").type(JsonFieldType.ARRAY).description("텃밭 이미지"),
                                fieldWithPath("gardenByComplexesResponses[].gardenStatus").type(JsonFieldType.STRING).description("텃밭 상태 : ACTIVE(모집중), INACTIVE(마감)"),
                                fieldWithPath("gardenByComplexesResponses[].gardenType").type(JsonFieldType.STRING).description("텃밭 타입 : PRIVATE(민간), PUBLIC(공공)"),
                                fieldWithPath("gardenByComplexesResponses[].latitude").type(JsonFieldType.NUMBER).description("텃밭 위도"),
                                fieldWithPath("gardenByComplexesResponses[].longitude").type(JsonFieldType.NUMBER).description("텃밭 경도"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                        )));
    }



}

