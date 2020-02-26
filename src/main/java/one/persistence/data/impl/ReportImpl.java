package one.persistence.data.impl;

import one.persistence.data.IReport;
import one.persistence.entity.Report;
import one.persistence.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Optional;


@Repository
public class ReportImpl implements IReport {
    @Autowired
   private ReportRepository reportRepo;


    @Override
    public Page<Report> futureReports(Date date, Pageable pageable) {
        return reportRepo.findByDateGreaterThan(date, pageable);
    }

    @Override
    public Page<Report> pastReports(Date date, Pageable pageable) {
        return reportRepo.findByDateLessThan(date, pageable);
    }

    @Override
    public Page<Report> offeredReports(Pageable pageable) {
        return reportRepo.findByDateIsNullAndSpeakerNotNull(pageable);
    }

    @Override
    public Optional<Report> getReportBuId(Long id) {
        return reportRepo.findById(id);
    }

    @Transactional
    @Override
    public Report saveReport(Report report) {
        return reportRepo.save(report);
    }

    @Transactional
    @Override
    public void deleteReportById(Long id) {
        reportRepo.deleteById(id);
    }
}
