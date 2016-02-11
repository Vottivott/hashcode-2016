package hashcode;

public class Point {

    private int r, c;

    public Point(int r, int y) {
        this.r = r;
        this.c = y;
    }

    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (r != point.r) return false;
        return c == point.c;

    }

    @Override
    public int hashCode() {
        int result = r;
        result = 31 * result + c;
        return result;
    }
}
