package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private static final int MIN_SECTIONS_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section newSection) {
        if (!sections.isEmpty()) {
            sectionValidate(newSection);
            sections.forEach(section -> section.separate(newSection));
        }

        sections.add(newSection);
    }

    private void sectionValidate(Section newSection) {
        sections.forEach(
                section -> {
                    List<Station> upDownStations = section.upDownStationPair();
                    if (upDownStations.contains(newSection.getUpStation()) && upDownStations.contains(newSection.getDownStation())) {
                        throw new IllegalArgumentException("상행역과 하행역이 이미 등록되어있습니다.");
                    }
                    if (!upDownStations.contains(newSection.getUpStation()) && !upDownStations.contains(newSection.getDownStation())) {
                        throw new IllegalArgumentException("상행역과 하행역 중 하나라도 등록되어 있지 않아 추가할 수 없습니다.");
                    }
                }
        );
    }

    public int totalDistanceLength() {
        return sections.stream()
                .mapToInt(Section::distanceValue)
                .sum();
    }

    public void remove(Station station) {
        removeValidate(station);
        Optional<Section> upSectionOptional = upSectionOptional(station);
        Optional<Section> downSectionOptional = downSectionOptional(station);

        if (upSectionOptional.isPresent() && downSectionOptional.isPresent()) {
            sections.add(upSectionOptional.get().mergeSection(downSectionOptional.get()));
        }

        removeUpStation(station);
        removeDownStation(station);
    }

    private void removeValidate(Station station) {
        if (!isContain(station)) {
            throw new IllegalArgumentException("노선에 없는 역입니다.");
        }
        if (getSections().size() <= MIN_SECTIONS_SIZE) {
            throw new IllegalArgumentException("구간이 1개이면 역을 제거할 수 없습니다.");
        }
    }

    private Optional<Section> downSectionOptional(Station station) {
        return sections.stream()
                .filter(section -> section.isDownStation(station))
                .findFirst();
    }

    private Optional<Section> upSectionOptional(Station station) {
        return sections.stream()
                .filter(section -> section.isUpStation(station))
                .findFirst();
    }

    private void removeDownStation(Station station) {
        downSectionOptional(station)
                .ifPresent(section -> sections.remove(section));
    }

    private void removeUpStation(Station station) {
        upSectionOptional(station)
                .ifPresent(section -> sections.remove(section));
    }

    public boolean isContain(Station station) {
        return allStations().contains(station);
    }

    private List<Station> allStations() {
        return sections.stream()
                .map(section -> section.upDownStationPair())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
