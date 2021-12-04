package nextstep.subway.line.application;

import nextstep.subway.common.exception.NoResultException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineResponses;
import nextstep.subway.section.domain.Distance;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public LineResponse findOne(Long id) {
        Line persistLine = this.findById(id);
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public LineResponses findAll() {
        List<Line> persistLines = lineRepository.findAll();
        return new LineResponses(persistLines);
    }

    public LineResponse saveLine(LineRequest request) {
        validateDuplicatedLine(request.getName());
        Line line = request.toLine(this.getNewSections(request));
        line.getSections().getSections()
                .stream()
                .forEach(section -> section.setLine(line));
        Line persistLine = lineRepository.save(line);
        return LineResponse.of(persistLine);
    }

    private void validateDuplicatedLine(String name) {
        Optional<Line> exist = lineRepository.findByName(name);
        if (exist.isPresent()) {
            throw new DataIntegrityViolationException("중복된 노선을 추가할 수 없습니다.");
        }
    }

    private Sections getNewSections(LineRequest request) {
        Section section = this.getNewSection(request.getUpStationId(), request.getDownStationId(), request.getDistance());
        return new Sections(section);
    }

    private Section getNewSection(final Long upStationId, final Long downStationId, final int distance) {
        Station upStation = this.findSection(upStationId);
        Station downStation = this.findSection(downStationId);
        return new Section(upStation, downStation, new Distance(distance));
    }

    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = this.findById(id);
        Station upStation = this.findSection(request.getUpStationId());
        Station downStation = this.findSection(request.getDownStationId());
        line.update(request.getName(), request.getColor(), upStation, downStation, new Distance(request.getDistance()));
        Line persistLine = lineRepository.save(line);
        return LineResponse.of(persistLine);
    }

    private Station findSection(Long sectionId) {
        return stationRepository.findById(sectionId)
                .orElseThrow(() -> new NoResultException("찾을 수 없는 구간입니다."));
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public SectionResponse saveSection(Long lineId, SectionRequest sectionRequest) {
        Line line = this.findById(lineId);
        Section section = this.getNewSection(line, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
        line.getSections().addSection(section);
        Line persistLine = lineRepository.save(line);
        return SectionResponse.of(section);
    }

    private Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NoResultException("찾을 수 없는 노선입니다."));
    }

    private Section getNewSection(final Line line,final Long upStationId, final Long downStationId, final int distance) {
        Station upStation = this.findSection(upStationId);
        Station downStation = this.findSection(downStationId);
        return new Section(line, upStation, downStation, new Distance(distance));
    }

}
