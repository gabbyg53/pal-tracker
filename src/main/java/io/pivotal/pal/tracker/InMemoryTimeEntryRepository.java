package io.pivotal.pal.tracker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
        private final List<TimeEntry> timeEntryList = new ArrayList<>();
        private long id = 1L;

        @Override
        public TimeEntry create(TimeEntry timeEntry) {
                TimeEntry newTimeEntry = new TimeEntry(id++, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
                timeEntryList.add(newTimeEntry);
                return newTimeEntry;
        }

        @Override
        public TimeEntry find(long id) {
                for (TimeEntry timeEntry: timeEntryList) {
                        if(timeEntry.getId()==id){
                                return timeEntry;
                        }
                }
                return null;
        }

        @Override
        public List<TimeEntry> list() {
                return timeEntryList;
        }

        @Override
        public TimeEntry update(long id, TimeEntry timeEntry) {
                for (int i = 0; i < timeEntryList.size(); i++) {
                        if(timeEntryList.get(i).getId()==id){
                                timeEntry.setId(id);
                                timeEntryList.set(i, timeEntry);
                                return timeEntryList.get(i);
                        }
                }
                return null;
        }

        @Override
        public void delete(long id) {
                for (int i = 0; i < timeEntryList.size(); i++) {
                        if(timeEntryList.get(i).getId()==id){
                                timeEntryList.remove(i);
                        }
                }
        }
}
