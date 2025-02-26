package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {
    private static final int 상행_하행구간_거리 = 10;
    private Station upStation;
    private Station downStation;
    private Section 상행_하행_구간;
    private Sections sections;

    @BeforeEach
    void setUp() {
        upStation = new Station("상행역");
        downStation = new Station("하행역");
        상행_하행_구간 = Section.of(upStation, downStation, 상행_하행구간_거리);

        sections = new Sections();
        sections.add(상행_하행_구간);
    }

    @Test
    void 구간_중간에_구간추가() {
//        given
        Station middleStation = new Station("중간역");
        Section 상행_중간_구간 = Section.of(upStation, middleStation, 5);

//        when
        sections.add(상행_중간_구간);

//        then
        List<Section> sections = this.sections.getSections();
        assertThat(sections).hasSize(2);
        assertThat(this.sections.totalDistanceLength()).isEqualTo(상행_하행구간_거리);
    }

    @Test
    void 상행구간_구간연장() {
//        given
        Station 상행연장역 = new Station("상행연장역");
        Section 상행_연장_구간 = Section.of(상행연장역, upStation, 5);

//        when
        sections.add(상행_연장_구간);

//        then
        List<Section> sections = this.sections.getSections();
        assertThat(sections).hasSize(2);
        assertThat(this.sections.totalDistanceLength()).isEqualTo(15);
    }

    @Test
    void 하행구간_구간연장() {
//        given
        Station 하행연장역 = new Station("하행연장역");
        Section 하행_연장_구간 = Section.of(downStation, 하행연장역, 5);

//        when
        sections.add(하행_연장_구간);

//        then
        List<Section> sections = this.sections.getSections();
        assertThat(sections).hasSize(2);
        assertThat(this.sections.totalDistanceLength()).isEqualTo(15);
    }

    @Test
    void 상행역_삭제() {
//        given
        Station 상행연장역 = new Station("상행연장역");
        Section 상행_연장_구간 = Section.of(상행연장역, upStation, 5);
        sections.add(상행_연장_구간);

//        when
        sections.remove(상행연장역);

//        then
        List<Section> sections = this.sections.getSections();
        assertThat(sections).hasSize(1);
        assertThat(this.sections.totalDistanceLength()).isEqualTo(상행_하행구간_거리);
    }

    @Test
    void 하행역_삭제() {
//        given
        Station 하행연장역 = new Station("하행연장역");
        Section 하행_연장_구간 = Section.of(downStation, 하행연장역, 5);
        sections.add(하행_연장_구간);

//        when
        sections.remove(하행연장역);

//        then
        List<Section> sections = this.sections.getSections();
        assertThat(sections).hasSize(1);
        assertThat(this.sections.totalDistanceLength()).isEqualTo(상행_하행구간_거리);
    }

    @Test
    void 중간역_삭제() {
//        given
        Station 중간역 = new Station("중간역");
        Section 상행_중간_구간 = Section.of(upStation, 중간역, 5);
        sections.add(상행_중간_구간);

//        when
        sections.remove(중간역);

//        then
        List<Section> sections = this.sections.getSections();
        assertThat(sections).hasSize(1);
        assertThat(this.sections.totalDistanceLength()).isEqualTo(상행_하행구간_거리);
    }

}
