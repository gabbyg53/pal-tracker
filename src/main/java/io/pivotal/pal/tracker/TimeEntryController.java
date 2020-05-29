package io.pivotal.pal.tracker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;
import java.util.List;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
@RequestMapping(path = "/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntriesRepo;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(
            TimeEntryRepository timeEntriesRepo,
            MeterRegistry meterRegistry
    ) {
        this.timeEntriesRepo = timeEntriesRepo;

        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        TimeEntry createdTimeEntry = timeEntriesRepo.create(timeEntry);
        actionCounter.increment();
        timeEntrySummary.record(timeEntriesRepo.list().size());

        return new ResponseEntity<>(createdTimeEntry, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long id) {
        TimeEntry timeEntry = timeEntriesRepo.find(id);
        if (timeEntry != null) {
            actionCounter.increment();
            return new ResponseEntity<>(timeEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        return new ResponseEntity<>(timeEntriesRepo.list(), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable Long id, @RequestBody TimeEntry timeEntry) {
        TimeEntry updatedTimeEntry = timeEntriesRepo.update(id, timeEntry);
        if (updatedTimeEntry != null) {
            actionCounter.increment();
            return new ResponseEntity<>(updatedTimeEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        timeEntriesRepo.delete(id);
        actionCounter.increment();
        timeEntrySummary.record(timeEntriesRepo.list().size());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
//    private final TimeEntryRepository timeEntryRepository;
//    public TimeEntryController(@ModelAttribute TimeEntryRepository timeEntryRepository) {
//        this.timeEntryRepository = timeEntryRepository;
//    }
//    @PostMapping
//    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
//        return new ResponseEntity(timeEntryRepository.create(timeEntryToCreate), HttpStatus.CREATED);
//    }
//    @GetMapping("/{id}")
//    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
//        TimeEntry entry = timeEntryRepository.find(id);
//        if(entry==null){
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        } else {
//            return new ResponseEntity(entry, HttpStatus.OK);
//        }
//    }
//    @GetMapping
//    public ResponseEntity<List<TimeEntry>> list() {
//        List<TimeEntry> list = timeEntryRepository.list();
//        return new ResponseEntity(list, HttpStatus.OK);
//    }
//    @PutMapping("/{id}")
//    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry expected) {
//        TimeEntry entry = timeEntryRepository.update(id, expected);
//        if (entry==null){
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        } else {
//            return new ResponseEntity(entry, HttpStatus.OK);
//        }
//    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity delete(@PathVariable long id) {
//        timeEntryRepository.delete(id);
//        return new ResponseEntity(HttpStatus.NO_CONTENT);
//    }
//}