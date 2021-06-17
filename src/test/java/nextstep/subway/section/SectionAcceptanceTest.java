package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.line.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.section.SectionSteps.*;
import static nextstep.subway.station.StationSteps.지하철_역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private static final Map<String, String> 강남역 = new HashMap<>();
    private static final Map<String, String> 양재역 = new HashMap<>();
    private static final Map<String, String> 판교역 = new HashMap<>();
    private static final Map<String, String> 정자역 = new HashMap<>();
    private static final Long 강남역_ID = 1L;
    private static final Long 양재역_ID = 2L;
    private static final Long 판교역_ID = 3L;
    private static final Long 정자역_ID = 4L;

    // 강남 - 양재 - 양재시민의숲 - 청계산입구 - 판교 - 정자 - 미금 - 동천 - 수지구청 - 성복 - 상현 - 광교중앙 - 광교
    @BeforeEach
    void init() {
        강남역.put("name", "강남역");
        양재역.put("name", "양재역");
        판교역.put("name", "판교역");
        정자역.put("name", "정자역");
        지하철_역_생성_요청(강남역);
        지하철_역_생성_요청(양재역);
        지하철_역_생성_요청(판교역);
        지하철_역_생성_요청(정자역);
    }

    @DisplayName("상행역과 하행역 사이에 새로운 역을 등록한다.")
    @Test
    void addSectionBetweenUpStationAndDownStation() {
        // given
        LineRequest 신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 판교역_ID, 20);
        ExtractableResponse<Response> 생성된_신분당선 = 지하철_노선_생성_요청(신분당선);

        // when
        SectionRequest sectionRequest = new SectionRequest(강남역_ID, 양재역_ID, 10);
        ExtractableResponse<Response> 구간이_추가된_신분당선 = 지하철_구간_생성_요청(생성된_신분당선, sectionRequest);

        // then
        assertThat(구간이_추가된_신분당선.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("새로운 역을 상행 종점으로 등록한다.")
    @Test
    void addSectionBeforeUpStation() {
        // given
        LineRequest 신분당선 = new LineRequest("신분당선", "bg-red-600", 양재역_ID, 판교역_ID, 10);
        ExtractableResponse<Response> 생성된_신분당선 = 지하철_노선_생성_요청(신분당선);

        // when
        SectionRequest sectionRequest = new SectionRequest(강남역_ID, 양재역_ID, 10);
        ExtractableResponse<Response> 구간이_추가된_신분당선 = 지하철_구간_생성_요청(생성된_신분당선, sectionRequest);

        // then
        assertThat(구간이_추가된_신분당선.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("새로운 역을 하행 종점으로 등록한다.")
    @Test
    void addSectionAfterDownStation() {
        // given
        LineRequest 신분당선 = new LineRequest("신분당선", "bg-red-600", 양재역_ID, 판교역_ID, 10);
        ExtractableResponse<Response> 생성된_신분당선 = 지하철_노선_생성_요청(신분당선);

        // when
        SectionRequest sectionRequest = new SectionRequest(판교역_ID, 정자역_ID, 10);
        ExtractableResponse<Response> 구간이_추가된_신분당선 = 지하철_구간_생성_요청(생성된_신분당선, sectionRequest);

        // then
        assertThat(구간이_추가된_신분당선.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없다.")
    @Test
    void addSection_badRequest_case1() {
        // given
        LineRequest 신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 판교역_ID, 20);
        ExtractableResponse<Response> 생성된_신분당선 = 지하철_노선_생성_요청(신분당선);

        // when
        SectionRequest sectionRequest = new SectionRequest(강남역_ID, 양재역_ID, 20);
        ExtractableResponse<Response> 구간이_추가된_신분당선 = 지하철_구간_생성_요청(생성된_신분당선, sectionRequest);

        // then
        assertThat(구간이_추가된_신분당선.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없다.")
    @Test
    void addSection_badRequest_case2() {
        // given
        LineRequest 신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 판교역_ID, 20);
        ExtractableResponse<Response> 생성된_신분당선 = 지하철_노선_생성_요청(신분당선);

        // when
        SectionRequest sectionRequest = new SectionRequest(강남역_ID, 판교역_ID, 20);
        ExtractableResponse<Response> 구간이_추가된_신분당선 = 지하철_구간_생성_요청(생성된_신분당선, sectionRequest);

        // then
        assertThat(구간이_추가된_신분당선.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음")
    @Test
    void addSection_badRequest_case3() {
        // given
        LineRequest 신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 판교역_ID, 20);
        ExtractableResponse<Response> 생성된_신분당선 = 지하철_노선_생성_요청(신분당선);

        // when
        SectionRequest sectionRequest = new SectionRequest(양재역_ID, 정자역_ID, 20);
        ExtractableResponse<Response> 구간이_추가된_신분당선 = 지하철_구간_생성_요청(생성된_신분당선, sectionRequest);

        // then
        assertThat(구간이_추가된_신분당선.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("상행 종점을 제거한다.")
    @Test
    void removeEndPointOfUpStation() {
        // given
        ExtractableResponse<Response> 구간이_추가된_신분당선 = 강남역_양재역_퍈교역_구간이_포함된_신분당선_등록();

        // when
        ExtractableResponse<Response> 구간이_제거된_신분당선 = 지하철_구간_제거_요청(구간이_추가된_신분당선, 강남역_ID);

        // then
        List<StationResponse> 강남역이_제거된_역목록 = 구간이_제거된_신분당선.jsonPath().getObject(".", LineResponse.class).getStations();
        assertAll(
                () -> assertThat(구간이_제거된_신분당선.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(강남역이_제거된_역목록).hasSize(2),
                () -> assertThat(강남역이_제거된_역목록.get(0).getId()).isEqualTo(양재역_ID),
                () -> assertThat(강남역이_제거된_역목록.get(1).getId()).isEqualTo(판교역_ID)
        );
    }

    @DisplayName("하행 종점을 제거한다.")
    @Test
    void removeEndPointOfDownStation() {
        // given
        ExtractableResponse<Response> 구간이_추가된_신분당선 = 강남역_양재역_퍈교역_구간이_포함된_신분당선_등록();

        // when
        ExtractableResponse<Response> 구간이_제거된_신분당선 = 지하철_구간_제거_요청(구간이_추가된_신분당선, 판교역_ID);

        // then
        List<StationResponse> 판교역이_제거된_역목록 = 구간이_제거된_신분당선.jsonPath().getObject(".", LineResponse.class).getStations();
        assertAll(
                () -> assertThat(구간이_제거된_신분당선.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(판교역이_제거된_역목록).hasSize(2),
                () -> assertThat(판교역이_제거된_역목록.get(0).getId()).isEqualTo(강남역_ID),
                () -> assertThat(판교역이_제거된_역목록.get(1).getId()).isEqualTo(양재역_ID)
        );
    }

    @DisplayName("상행 종점과 하행 종점 사이에 중간역을 제거한다.")
    @Test
    void remove_between_상행_종점_and_하행_종점() {
        // given
        ExtractableResponse<Response> 구간이_추가된_신분당선 = 강남역_양재역_퍈교역_구간이_포함된_신분당선_등록();

        // when
        ExtractableResponse<Response> 구간이_제거된_신분당선 = 지하철_구간_제거_요청(구간이_추가된_신분당선, 양재역_ID);

        // then
        List<StationResponse> 양재역이_제거된_역목록 = 구간이_제거된_신분당선.jsonPath().getObject(".", LineResponse.class).getStations();
        assertAll(
                () -> assertThat(구간이_제거된_신분당선.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(양재역이_제거된_역목록).hasSize(2),
                () -> assertThat(양재역이_제거된_역목록.get(0).getId()).isEqualTo(강남역_ID),
                () -> assertThat(양재역이_제거된_역목록.get(1).getId()).isEqualTo(판교역_ID)
        );
    }

    @DisplayName("노선에 등록되어있지 않은 역은 제거할 수 없다.")
    @Test
    void canNotRemoveNotFoundStation() {
        // given
        ExtractableResponse<Response> 구간이_추가된_신분당선 = 강남역_양재역_퍈교역_구간이_포함된_신분당선_등록();

        // when
        ExtractableResponse<Response> 구간이_제거된_신분당선 = 지하철_구간_제거_요청(구간이_추가된_신분당선, 정자역_ID);

        // then
        assertThat(구간이_제거된_신분당선.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @DisplayName("노선에 구간이 1개만 등록되어있다면 제거할 수 없다.")
    @Test
    void canNotRemoveExistOnlyOneSection() {
        // given
        LineRequest 신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 판교역_ID, 20);
        ExtractableResponse<Response> 생성된_신분당선 = 지하철_노선_생성_요청(신분당선);

        // when
        ExtractableResponse<Response> 구간이_제거된_신분당선 = 지하철_구간_제거_요청(생성된_신분당선, 판교역_ID);

        // then
        assertThat(구간이_제거된_신분당선.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 강남역_양재역_퍈교역_구간이_포함된_신분당선_등록() {
        LineRequest 신분당선 = new LineRequest("신분당선", "bg-red-600", 양재역_ID, 판교역_ID, 10);
        ExtractableResponse<Response> 생성된_신분당선 = 지하철_노선_생성_요청(신분당선);

        SectionRequest sectionRequest = new SectionRequest(강남역_ID, 양재역_ID, 10);
        return 지하철_구간_생성_요청(생성된_신분당선, sectionRequest);
    }

}