package com.garden.back.docs.garden;

import com.garden.back.crop.service.response.MonthlyRecommendedCropsResponse;
import com.garden.back.docs.RestDocsSupport;
import com.garden.back.docs.crop.CropFixture;
import com.garden.back.garden.GardenController;
import com.garden.back.garden.dto.response.GardenByNameResponses;
import com.garden.back.garden.service.GardenCommandService;
import com.garden.back.garden.service.GardenReadService;
import com.garden.back.garden.service.dto.response.*;
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
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GardenRestDocs extends RestDocsSupport {

    GardenReadService gardenReadService = mock(GardenReadService.class);
    GardenCommandService gardenCommandService = mock(GardenCommandService.class);

    @Override
    protected Object initController() {
        return new GardenController(gardenReadService, gardenCommandService);
    }

    @DisplayName("텃밭 이름으로 검색이 가능하다.")
    @Test
    void getGardenByName() throws Exception {
        GardenByNameResults gardenByNameResults = GardenFixture.gardenByNameResults();
        given(gardenReadService.getGardensByName(any())).willReturn(gardenByNameResults);

        mockMvc.perform(get("/v2/gardens")
                        .param("gardenName", "도연")
                        .param("pageNumber", "0")
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
                        .param("pageNumber", "0")
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
                        .param("gardenType", "PUBLIC")
                        .param("pageNumber", "0")
                        .param("startLat", "37.2449168")
                        .param("startLong", "127.1288684")
                        .param("endLat", "37.4449168")
                        .param("endLong", "127.1388684")
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

    @DisplayName("텃밭을 상세 조회할 수 있다.")
    @Test
    void getGardenDetail() throws Exception {
        Long gardenId = 1L;
        GardenDetailResult gardenDetailResult = GardenFixture.gardenDetailResult();
        given(gardenReadService.getGardenDetail(any())).willReturn(gardenDetailResult);

        mockMvc.perform(get("/v2/gardens/{gardenId}",gardenId))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("detail-garden",
                        pathParameters(
                                parameterWithName("gardenId").description("텃밭의 id")
                        ),
                        responseFields(
                                fieldWithPath("gardenId").type(JsonFieldType.NUMBER).description("텃밭 아이디"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("텃밭 주소"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("텃밭 위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("텃밭 경도"),
                                fieldWithPath("gardenName").type(JsonFieldType.STRING).description("텃밭 이름"),
                                fieldWithPath("gardenType").type(JsonFieldType.STRING).description("텃밭 타입 : PRIVATE(민간), PUBLIC(공공)"),
                                fieldWithPath("linkForRequest").type(JsonFieldType.STRING).description("텃밭 신청 홈페이지"),
                                fieldWithPath("price").type(JsonFieldType.STRING).description("텃밭 가격"),
                                fieldWithPath("contact").type(JsonFieldType.STRING).description("연락처"),
                                fieldWithPath("size").type(JsonFieldType.STRING).description("텃밭 크기"),
                                fieldWithPath("gardenStatus").type(JsonFieldType.STRING).description("텃밭 상태 : ACTIVE(모집중), INACTIVE(마감)"),
                                fieldWithPath("writerId").type(JsonFieldType.NUMBER).description("작성자 아이디"),
                                fieldWithPath("recruitStartDate").type(JsonFieldType.STRING).description("모집 시작일"),
                                fieldWithPath("recruitEndDate").type(JsonFieldType.STRING).description("모집 마감일"),
                                fieldWithPath("useStartDate").type(JsonFieldType.STRING).description("사용 시작일"),
                                fieldWithPath("useEndDate").type(JsonFieldType.STRING).description("사용 종료일"),
                                fieldWithPath("gardenDescription").type(JsonFieldType.STRING).description("텃밭 설명"),
                                fieldWithPath("images").type(JsonFieldType.ARRAY).description("텃밭 사진"),
                                fieldWithPath("gardenFacility").type(JsonFieldType.OBJECT).description("텃밭 설비 정보"),
                                fieldWithPath("gardenFacility.isToilet").type(JsonFieldType.BOOLEAN).description("텃밭 화장실 제공 여부"),
                                fieldWithPath("gardenFacility.isWaterway").type(JsonFieldType.BOOLEAN).description("텃밭 수로 제공 여부"),
                                fieldWithPath("gardenFacility.isEquipment").type(JsonFieldType.BOOLEAN).description("농기구 제공 여부"),
                                fieldWithPath("isLiked").type(JsonFieldType.BOOLEAN).description("좋아요 여부")
                        )));
    }

    @DisplayName("최근 본 텃밭 정보를 조회할 수 있다.")
    @Test
    void getRecentGardens() throws Exception {
        RecentGardenResults recentGardenResults = GardenFixture.recentGardenResults();
        given(gardenReadService.getRecentGardens(any())).willReturn(recentGardenResults);

        mockMvc.perform(get("/v2/gardens/recent"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("recent-gardens",
                        responseFields(
                                fieldWithPath("recentGardenResponses").type(JsonFieldType.ARRAY).description("최근 본 텃밭 최대 10개"),
                                fieldWithPath("recentGardenResponses[].gardenId").type(JsonFieldType.NUMBER).description("텃밭 아이디"),
                                fieldWithPath("recentGardenResponses[].size").type(JsonFieldType.STRING).description("텃밭 크기"),
                                fieldWithPath("recentGardenResponses[].gardenName").type(JsonFieldType.STRING).description("텃밭 이름"),
                                fieldWithPath("recentGardenResponses[].price").type(JsonFieldType.STRING).description("텃밭 가격"),
                                fieldWithPath("recentGardenResponses[].images").type(JsonFieldType.STRING).description("텃밭 이미지"),
                                fieldWithPath("recentGardenResponses[].gardenStatus").type(JsonFieldType.STRING).description("텃밭 상태 : ACTIVE(모집중), INACTIVE(마감)"),
                                fieldWithPath("recentGardenResponses[].gardenType").type(JsonFieldType.STRING).description("텃밭 타입 : PRIVATE(민간), PUBLIC(공공)")
                        )));
    }


}

