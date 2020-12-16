package com.cheminv.app.service;

import com.cheminv.app.domain.Report;
import com.cheminv.app.repository.ReportRepository;
import com.cheminv.app.repository.StoreRepository;
import com.cheminv.app.repository.UserRepository;
import com.cheminv.app.repository.ItemStockRepository;
import com.cheminv.app.service.dto.ItemStockDTO;
import com.cheminv.app.service.mapper.ItemStockMapper;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class JasperReportService {

    private final Logger log = LoggerFactory.getLogger(JasperReportService.class);

    @Value("classpath:templates/cmis_jasper_report.jrxml")
    private Resource jasperTemplate;

    @Value("classpath:templates/images/logo-header-full.png")
    private Resource headerImage;

    private final ItemStockRepository itemStockRepository;

    private final ReportRepository reportRepository;

    private final UserRepository userRepository;

    private final StoreRepository storeRepository;

    private final ItemStockMapper itemStockMapper;

    public JasperReportService(ItemStockRepository itemStockRepository, ReportRepository reportRepository,
                               UserRepository userRepository, StoreRepository storeRepository, ItemStockMapper itemStockMapper) {
        this.itemStockRepository = itemStockRepository;
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.itemStockMapper = itemStockMapper;
    }

    public JasperPrint generateReports(Long id,Long storeId) throws IOException, JRException {
        List<ItemStockDTO> reportItems = itemStockMapper.toDto(
            itemStockRepository.findAllByStore(storeId));
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportItems);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperTemplate.getInputStream());
        BufferedImage image = ImageIO.read(headerImage.getInputStream());
        Map<String,Object> params = new HashMap<>();
        params.put("logo",image);
        String storeName = storeRepository.getOne(storeId).getName();
        params.put("category",storeName.toUpperCase());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,params,dataSource);

        String filename = "inventory-report-"
            + DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mma").format(LocalDateTime.now()) + ".pdf";

        Report report = new Report();
        report.setInvUser(userRepository.getOne(id));
        report.setName(filename);
        report.setReport(JasperExportManager.exportReportToPdf(jasperPrint));
        report.setReportContentType("application/pdf");
        report.setCreatedOn(Instant.now());
        reportRepository.save(report);


        log.debug("Report export successfully");
        return jasperPrint;
    }
}
