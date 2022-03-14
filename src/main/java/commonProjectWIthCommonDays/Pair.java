package commonProjectWIthCommonDays;

import java.util.Objects;

public class Pair {
    int firstEmployeeId;
    int secondEmployeeId;

    public Pair(int firstEmployeeId, int secondEmployeeId) {
        this.firstEmployeeId = firstEmployeeId;
        this.secondEmployeeId = secondEmployeeId;
    }


    public void print(long days) {
        StringBuilder sb = new StringBuilder();
        System.out.println("firstEmployeeId | secondEmployeeId | daysInCommonProjects");
        sb.append("\t")
                .append(firstEmployeeId).append("\t\t\t\t\t")
                .append(secondEmployeeId).append("\t\t\t\t\t")
                .append(days);
        System.out.println(sb);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return firstEmployeeId == pair.firstEmployeeId && secondEmployeeId == pair.secondEmployeeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstEmployeeId, secondEmployeeId);
    }
}
