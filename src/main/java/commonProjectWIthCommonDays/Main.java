package commonProjectWIthCommonDays;

import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class Main {
    public static final String[] DATE_FORMATTERS = new String[]{"yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd", "yyyy,MM,dd", "yyyy_MM_dd",};

    public static void main(String[] args) {
        Map<Pair, Long> workedDaysTogether = new HashMap<>();
        List<String> inputs = new ArrayList<>();

        try {
            inputs = Files.readAllLines(Path.of("file.csv"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        List<Record> records = new ArrayList<>();

        for (String input : inputs) {
            String[] splitRecord = input.split(", ");
            try {
                records.add(new Record(splitRecord[0], splitRecord[1], convertToLocalDate(DateUtils.parseDate(splitRecord[2], DATE_FORMATTERS)),
                        splitRecord[3].equals("NULL") ? LocalDate.now() : convertToLocalDate(DateUtils.parseDate(splitRecord[3], DATE_FORMATTERS))));
            } catch (ParseException e) {
                System.out.println("[ " + input + " ] -> Not supported date format! Skipped!\n");
            }
        }
        //Find the pair of employees who have worked together on common projects and all days
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            for (int j = i + 1; j < records.size(); j++) {
                Record tempRecord = records.get(j);
                if (record.getProjectId().equals(tempRecord.getProjectId())) {
                    long days = returnAllDaysWorkedTogether(record, tempRecord);
                    if (days > 0) {
                        addWorkedDaysForPair(record, tempRecord, workedDaysTogether, days);
                    }
                }
            }
        }
        //Find longest period of time
        workedDaysTogether.entrySet().stream()
                .sorted((firstPair, secondPair) -> secondPair.getValue().compareTo(firstPair.getValue()))
                .limit(1)
                .forEach((pair) -> pair.getKey().print(pair.getValue()));
    }

    /**
     * Check if pair exists and add days.
     *
     * @param record1
     * @param record2
     * @param workedDaysTogether contains all pairs with common days worked
     * @param days               common days
     */
    private static void addWorkedDaysForPair(Record record1, Record record2, Map<Pair, Long> workedDaysTogether, long days) {
        int firstEmployeeId = Integer.parseInt(record1.getEmployeeId());
        int secondEmployeeId = Integer.parseInt(record2.getEmployeeId());
        if (firstEmployeeId > secondEmployeeId) {
            int temp = firstEmployeeId;
            firstEmployeeId = secondEmployeeId;
            secondEmployeeId = temp;
        }
        Pair pair = new Pair(firstEmployeeId, secondEmployeeId);
        if (!workedDaysTogether.containsKey(pair)) {
            workedDaysTogether.put(pair, days);
        } else {
            workedDaysTogether.put(pair, workedDaysTogether.get(pair) + days);
        }
    }


    /**
     * Get common days.
     *
     * @param record1
     * @param record2
     * @return
     */
    static long returnAllDaysWorkedTogether(Record record1, Record record2) {
        LocalDate start;
        LocalDate end;

        LocalDate record1StartDate = record1.getStartDate();
        LocalDate record1EndDate = record1.getEndDate();
        LocalDate record2RecordStartDate = record2.getStartDate();
        LocalDate record2EndDate = record2.getEndDate();

        boolean isEqualStartDates = record1StartDate.equals(record2RecordStartDate);
        boolean isEqualEndDates = record1EndDate.equals(record2EndDate);

        if (record2EndDate.isBefore(record1StartDate) || record2RecordStartDate.isAfter(record1EndDate)) {
            return 0;
        } else if ((record1StartDate.isBefore(record2RecordStartDate) || isEqualStartDates) &&
                record1EndDate.isBefore(record2EndDate) || isEqualEndDates) {
            start = record2RecordStartDate;
            end = record1EndDate;
        } else if ((record1StartDate.isAfter(record2RecordStartDate) || isEqualStartDates)
                && (record1EndDate.isAfter(record2EndDate) || isEqualEndDates)) {
            start = record1StartDate;
            end = record2EndDate;
        } else if ((record1StartDate.isBefore(record2RecordStartDate) || isEqualStartDates)
                && (record1EndDate.isAfter(record2EndDate) || isEqualEndDates)) {
            start = record2RecordStartDate;
            end = record2EndDate;
        } else if ((record1StartDate.isAfter(record2RecordStartDate) || isEqualStartDates)
                && (record1EndDate.isBefore(record2EndDate) || isEqualEndDates)) {
            start = record1StartDate;
            end = record2EndDate;
        } else {
            return 0;
        }

        return DAYS.between(start, end);
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return LocalDate.ofInstant(dateToConvert.toInstant(), ZoneId.systemDefault());
    }

}
