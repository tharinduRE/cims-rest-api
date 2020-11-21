package com.cheminv.app.service;

import com.cheminv.app.domain.InvReport;
import com.cheminv.app.domain.ItemStock;
import com.cheminv.app.domain.enumeration.StockStore;
import com.cheminv.app.repository.InvReportRepository;
import com.cheminv.app.repository.InvUserRepository;
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
import java.util.stream.Collectors;

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

    private final ItemStockMapper itemStockMapper;

    public JasperReportService(ItemStockRepository itemStockRepository, InvReportRepository invReportRepository,
                               InvUserRepository invUserRepository, ItemStockMapper itemStockMapper) {
        this.itemStockRepository = itemStockRepository;
        this.invReportRepository = invReportRepository;
        this.invUserRepository = invUserRepository;
        this.itemStockMapper = itemStockMapper;
    }

    public JasperPrint generateReports(Long id,StockStore stockStore) throws IOException, JRException {
        List<ItemStockDTO> reportItems = itemStockMapper.toDto(
            itemStockRepository.findAllByStockStoreEquals(stockStore));
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportItems);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperTemplate.getInputStream());
        BufferedImage image = ImageIO.read(headerImage.getInputStream());
        Map<String,Object> params = new HashMap<>();
        params.put("logo",image);
        params.put("category",stockStore.getName().toUpperCase());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,params,dataSource);

        String filename = "inventory-report-"
            + DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mma").format(LocalDateTime.now()) + ".pdf";

        InvReport invReport = new InvReport();
        invReport.setInvUser(invUserRepository.getOne(id));
        invReport.setName(filename);
        invReport.setReport(JasperExportManager.exportReportToPdf(jasperPrint));
        invReport.setReportContentType("application/pdf");
        invReport.setCreatedOn(Instant.now());
        invReportRepository.save(invReport);


        log.debug("Report export successfully");
        return jasperPrint;
    }
}
