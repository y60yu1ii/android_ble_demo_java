package de.fishare.bioappjava.listadapter;
public class IndexPath {
    Integer section;
    Integer row;
    public IndexPath(Integer section, Integer row) {
        super();
        this.section = section;
        this.row = row;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getSection() {
        return section;
    }
}
