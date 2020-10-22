package com.cheminv.app.service;

import com.cheminv.app.domain.InvReport;
import com.cheminv.app.repository.InvReportRepository;
import com.cheminv.app.repository.InvUserRepository;
import com.cheminv.app.repository.ItemStockRepository;
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

    private final InvReportRepository invReportRepository;

    private final InvUserRepository invUserRepository;

    public JasperReportService(ItemStockRepository itemStockRepository, InvReportRepository invReportRepository, InvUserRepository invUserRepository) {
        this.itemStockRepository = itemStockRepository;
        this.invReportRepository = invReportRepository;
        this.invUserRepository = invUserRepository;
    }

    public JasperPrint generateReports(Long id) throws IOException, JRException {
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(itemStockRepository.findAll());
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperTemplate.getInputStream());
        BufferedImage image = ImageIO.read(headerImage.getInputStream());
        Map<String,Object> params = new HashMap<>();
        params.put("logo",image);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,params,dataSource);

        String filename = "inventory-report-" + DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mma").format(LocalDateTime.now()) + ".pdf";
        InvReport invReport = new InvReport();
        invReport.setInvUser(invUserRepository.getOne(id));
        invReport.setName(filename);
        invReport.setCreatedOn(Instant.now());
        invReportRepository.save(invReport);

        //String fileName = "inv_report" + ".pdf";
        //JasperExportManager.exportReportToPdfFile(jasperPrint,fileName);
        log.debug("Report export successfully");
        return jasperPrint;
    }
}
