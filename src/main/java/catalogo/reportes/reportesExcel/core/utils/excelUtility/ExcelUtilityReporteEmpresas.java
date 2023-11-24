package catalogo.reportes.reportesExcel.core.utils.excelUtility;

import catalogo.reportes.core.afiliados.afiliadosRepositories.IAfiliadosProductoRepository;
import catalogo.reportes.core.utils.S3FileManager;
import catalogo.reportes.reportesExcel.core.utils.resources.ExcelDocumentoColumna;
import common.rondanet.catalogo.core.entity.Empaque;
import common.rondanet.catalogo.core.entity.Producto;
import common.rondanet.catalogo.core.entity.Usuario;
import common.rondanet.clasico.core.afiliados.models.Empresa;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ExcelUtilityReporteEmpresas {

    @Autowired
    S3FileManager s3FileManager;

    @Autowired
    IAfiliadosProductoRepository afiliadosProductoRepository;

    public ExcelUtilityReporteEmpresas(S3FileManager s3FileManager) {
        this.s3FileManager = s3FileManager;
    }

    public XSSFWorkbook obtenerExcelParaReporteEmpresas() {
        XSSFWorkbook xSSFWorkbook = new XSSFWorkbook();
        try {
            File file = new File("excelParaReportes/reportes.xlsx");
            InputStream inputStream = new FileInputStream(file);

            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            XSSFSheet worksheet = wb.getSheetAt(0);

            inputStream.close();
            xSSFWorkbook = wb;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return xSSFWorkbook;
    }

    public XSSFWorkbook obtenerExcelParaReporteProductos() {
        XSSFWorkbook xSSFWorkbook = new XSSFWorkbook();
        try {

            File file = new File("excelParaReportes/productosDescargar.xlsx");
            InputStream inputStream = new FileInputStream(file);

            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            XSSFSheet worksheet = wb.getSheetAt(0);

            inputStream.close();
            xSSFWorkbook = wb;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return xSSFWorkbook;
    }

    public XSSFWorkbook agregarEmpresa(XSSFWorkbook xSSFWorkbook, Empresa empresa, String glnEmpresa, Date ultimaFechaDeActualizacion, int totalDeProductosConGTIN13, int totalDeProductosConGTIN14) {
        try {

            XSSFSheet worksheet = xSSFWorkbook.getSheetAt(0);
            List<ExcelDocumentoColumna> excelDocumentoColumnas =  ExcelDocumentoColumna.cabecerasExcelParaReporteEmpresa();
            // int row = crearCabeceras(xSSFWorkbook, worksheet, 0, excelDocumentoColumnas);
            int rowNumber = worksheet.getLastRowNum();
            rowNumber++;

            Row row = worksheet.createRow(rowNumber);

            Format formatter = new SimpleDateFormat("dd/MM/YYYY");
            String fechaDeActualizacion = formatter.format(ultimaFechaDeActualizacion);

            Cell cell = row.createCell(0);
            cell.setCellValue(empresa != null ? empresa.getRazonSocial(): "No tiene ubicación");

            cell = row.createCell(1);
            cell.setCellValue(empresa != null ? empresa.getRuc(): "No tiene ubicación");

            cell = row.createCell(2);
            cell.setCellValue(glnEmpresa);

            cell = row.createCell(3);
            cell.setCellValue(empresa.getFechaDeBaja() != null ? formatter.format(empresa.getFechaDeBaja()) : null);

            return xSSFWorkbook;
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return xSSFWorkbook;
    }

    public XSSFWorkbook agregarEmpresasEmitiendoOrdenesDeCompra(
            XSSFWorkbook xSSFWorkbook,
            common.rondanet.catalogo.core.entity.Empresa empresa
    ) {
        try {
            XSSFSheet worksheet = xSSFWorkbook.getSheetAt(0);
            int rowNumber = worksheet.getLastRowNum();
            rowNumber++;

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

            Row row = worksheet.createRow(rowNumber);

            Cell cell = row.createCell(0);
            cell.setCellValue(empresa != null ? empresa.getRut(): "No tiene RUT");

            cell = row.createCell(1);
            cell.setCellValue(empresa.getGln());

            cell = row.createCell(2);
            cell.setCellValue(empresa != null ? empresa.getRazonSocial(): "No tiene razon social");

            /*
                cell = row.createCell(3);
                cell.setCellValue(empresa != null ? empresa.getId(): "No tiene Id");
            */

            cell = row.createCell(3);
            cell.setCellValue(empresa != null ? formatter.print(empresa.getFechaCreacion()) : "No tiene fecha");

            return xSSFWorkbook;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return xSSFWorkbook;
    }

    public XSSFWorkbook agregarUsuarioEmpresas(
            XSSFWorkbook xSSFWorkbook,
            common.rondanet.catalogo.core.entity.Empresa empresa,
            Usuario usuario
    ) {
        try {
            XSSFSheet worksheet = xSSFWorkbook.getSheetAt(0);
            int rowNumber = worksheet.getLastRowNum();
            rowNumber++;

            Row row = worksheet.createRow(rowNumber);

            Cell cell = row.createCell(0);
            cell.setCellValue(empresa.getRut());

            cell = row.createCell(1);
            cell.setCellValue(empresa.getRazonSocial());

            cell = row.createCell(2);
            cell.setCellValue(usuario != null ? (usuario.getNombre() != null ? usuario.getNombre() : "") + " " + (usuario.getApellido() != null ? usuario.getApellido() : "") : "");

            cell = row.createCell(3);
            cell.setCellValue(usuario.getEmail());

            return xSSFWorkbook;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return xSSFWorkbook;
    }

    public int crearCabeceras(
            XSSFWorkbook xSSFWorkbook,
            XSSFSheet worksheet,
            int row,
            List<ExcelDocumentoColumna> excelDocumentoColumnas
    ) {
        XSSFCellStyle CellStyleBorder = xSSFWorkbook.createCellStyle();
        Font font = xSSFWorkbook.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        CellStyleBorder.setFont(font);
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setBold(false);
        font.setItalic(false);

        font.setBold(true);

        row = worksheet.getLastRowNum();
        row++;
        Row cabecera = worksheet.createRow(row);
        for (ExcelDocumentoColumna excelDocumentoColumna: excelDocumentoColumnas ) {
            Cell cell = cabecera.createCell(excelDocumentoColumna.getPosicion());
            cell.setCellStyle(CellStyleBorder);
            cell.setCellValue(excelDocumentoColumna.getLabel());
            worksheet.setColumnWidth(excelDocumentoColumna.getPosicion(), 255 * 20);
        }
        return row + 1;
    }

    public int agregarProductos(
            XSSFWorkbook xSSFWorkbook,
            List<Producto> productos,
            int rowNumber
    ) {
        try {

            XSSFSheet worksheet = xSSFWorkbook.getSheetAt(0);

            XSSFCellStyle CellStyleBorder = obtenerEstiloParaCabeceraExcelDeProductos(xSSFWorkbook);

            for (Producto producto: productos) {

                common.rondanet.clasico.core.afiliados.models.Producto productoAfiliados = afiliadosProductoRepository.findByGtin(producto.getGtin());
                if (productoAfiliados != null) {
                    String descripcion = producto.getDescripcion() + "  " + productoAfiliados.getPresentacion();
                    producto.setDescripcion(descripcion);
                }

                Row row = worksheet.createRow(rowNumber);

                agregarDatosDelProducto(producto, row, CellStyleBorder);

                agregarEmpaques(producto, row, CellStyleBorder);

                rowNumber++;

            }
            return rowNumber;
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return rowNumber;
    }

    public void agregarDatosDelProducto(
            Producto producto,
            Row row,
            XSSFCellStyle CellStyleBorder
    ) {
        Cell cell = row.createCell(ExcelMap.PRODUCT_CPP);
        agregarEstiloAlaCelda(cell, CellStyleBorder);

        String cpp = producto.getCpp() != null ? producto.getCpp() : "";
        if (producto.getEliminado()) {
            cpp = cpp + " - DADO DE BAJA";
        }
        cell.setCellValue(cpp);

        cell = row.createCell(ExcelMap.PRODUCT_GTIN);
        agregarEstiloAlaCelda(cell, CellStyleBorder);
        String gtin = producto.getGtin() != null ? producto.getGtin() : "";
        if (gtin.length() == 14) {
            gtin = gtin + " - EMPAQUE";
        }
        cell.setCellValue(gtin);

        cell = row.createCell(ExcelMap.PRODUCT_DESCRIPTION);
        agregarEstiloAlaCelda(cell, CellStyleBorder);
        cell.setCellValue(producto.getDescripcion() != null ? producto.getDescripcion() : "");

        cell = row.createCell(ExcelMap.PRODUCT_BRAND);
        agregarEstiloAlaCelda(cell, CellStyleBorder);
        cell.setCellValue(producto.getMarca() != null ? producto.getMarca() : "");

        cell = row.createCell(ExcelMap.PRODUCT_SECONDARY_CATEGORY);
        agregarEstiloAlaCelda(cell, CellStyleBorder);
        cell.setCellValue(producto.getLinea() != null ? producto.getLinea() : "");

        cell = row.createCell(ExcelMap.PRODUCT_MAIN_CATEGORY);
        agregarEstiloAlaCelda(cell, CellStyleBorder);
        cell.setCellValue(producto.getDivision() != null ? producto.getDivision() : "");

        cell = row.createCell(ExcelMap.PRODUCT_PRESENTATION);
        agregarEstiloAlaCelda(cell, CellStyleBorder);
        cell.setCellValue(producto.getPresentacion() != null ? producto.getPresentacion().getNombre() : "");

        cell = row.createCell(ExcelMap.PRODUCT_NET_CONTENT);
        agregarEstiloAlaCelda(cell, CellStyleBorder);
        if(producto.getContenidoNeto() != null)
            cell.setCellValue(producto.getContenidoNeto().doubleValue());

        cell = row.createCell(ExcelMap.PRODUCT_CONTENT_UNIT);
        agregarEstiloAlaCelda(cell, CellStyleBorder);
        cell.setCellValue(producto.getUnidadMedida() != null ? producto.getUnidadMedida() : "");
    }

    public void agregarEmpaques(Producto producto, Row row, XSSFCellStyle CellStyleBorder){
        if (producto.getEmpaques() != null) {
            Set<Empaque> empaques = producto.getEmpaques();
            for (Empaque empaque : empaques) {
                if (empaque != null) {
                    Cell cell = row.createCell(ExcelMap.FIRST_PACK_CPP);
                    cell.setCellValue(empaque.getCpp() != null ? empaque.getCpp() : "");
                    agregarEstiloAlaCelda(cell, CellStyleBorder);

                    cell = row.createCell(ExcelMap.FIRST_PACK_GTIN);
                    cell.setCellValue(empaque.getGtin() != null ? empaque.getGtin() : "");
                    agregarEstiloAlaCelda(cell, CellStyleBorder);

                    cell = row.createCell(ExcelMap.FIRST_PACK_CLASIFICATION);
                    cell.setCellValue(empaque.getClasificacion() != null ? empaque.getClasificacion() : "");
                    agregarEstiloAlaCelda(cell, CellStyleBorder);

                    cell = row.createCell(ExcelMap.FIRST_PACK_NET_WEIGHT);
                    if (empaque.getPesoBruto() != null)
                        cell.setCellValue(empaque.getPesoBruto().doubleValue());
                    agregarEstiloAlaCelda(cell, CellStyleBorder);

                    cell = row.createCell(ExcelMap.FIRST_PACK_NET_WEIGHT_UNIT);
                    cell.setCellValue(empaque.getUnidadMedida() != null ? empaque.getUnidadMedida() : "");
                    agregarEstiloAlaCelda(cell, CellStyleBorder);

                    cell = row.createCell(ExcelMap.FIRST_PACK_HEIGHT);
                    if (empaque.getAlto() != null)
                        cell.setCellValue(empaque.getAlto().doubleValue());
                    agregarEstiloAlaCelda(cell, CellStyleBorder);

                    cell = row.createCell(ExcelMap.FIRST_PACK_WIDTH);
                    if (empaque.getAncho() != null)
                        cell.setCellValue(empaque.getAncho().doubleValue());
                    agregarEstiloAlaCelda(cell, CellStyleBorder);

                    cell = row.createCell(ExcelMap.FIRST_PACK_DEPTH);
                    if (empaque.getProfundidad() != null)
                        cell.setCellValue(empaque.getProfundidad().doubleValue());
                    agregarEstiloAlaCelda(cell, CellStyleBorder);

                    cell = row.createCell(ExcelMap.FIRST_PACK_QUANTITY);
                    if (empaque.getCantidad() != null)
                        cell.setCellValue(empaque.getCantidad().doubleValue());
                    agregarEstiloAlaCelda(cell, CellStyleBorder);
                }
            }
        }
    }

    public static void agregarEstiloAlaCelda(Cell cell, XSSFCellStyle CellStyleBorder){
        cell.setCellStyle(CellStyleBorder);
    }

    public XSSFCellStyle obtenerEstiloParaCabeceraExcelDeProductos(XSSFWorkbook wb){
        XSSFCellStyle CellStyleBorder = wb.createCellStyle();
        CellStyleBorder.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        CellStyleBorder.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 204), wb.getStylesSource().getIndexedColors()));
        CellStyleBorder.setBorderBottom(BorderStyle.THIN);
        CellStyleBorder.setBorderTop(BorderStyle.THIN);
        CellStyleBorder.setBorderRight(BorderStyle.THIN);
        CellStyleBorder.setBorderLeft(BorderStyle.THIN);
        return CellStyleBorder;
    }


}
