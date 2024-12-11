package com.sa.appexamelaboratorio.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.sa.appexamelaboratorio.model.Exame;

@Service
public class RelatorioService {
        // Método que gera o PDF
        public ByteArrayOutputStream exportarExamesParaPdf(List<Exame> exames) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                try {
                        // Criando o PdfWriter e o PdfDocument
                        PdfWriter writer = new PdfWriter(out);
                        PdfDocument pdfDoc = new PdfDocument(writer);
                        Document document = new Document(pdfDoc);

                        // Criando uma fonte personalizada
                        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

                        // Adicionando a imagem no canto superior esquerdo
                        Image logo = new Image(ImageDataFactory.create("src/main/resources/static/img/sus.png"));
                        logo.setWidth(80).setHeight(30); // Defina o tamanho da imagem
                        document.add(logo); // Adiciona a imagem ao documento

                        // Criando um título para o relatório
                        Paragraph title = new Paragraph("Relatório de Exames")
                                        .setFont(font)
                                        .setFontSize(18)
                                        .setTextAlignment(TextAlignment.CENTER)
                                        .setMarginTop(10); // Margem superior para afastar do cabeçalho
                        document.add(title);

                        // Criando a tabela com 5 colunas
                        Table table = new Table(6);

                        // Cabeçalho da tabela com fundo colorido
                        table.addHeaderCell(new Cell().add(new Paragraph("ID"))
                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                        .setTextAlignment(TextAlignment.CENTER));
                        table.addHeaderCell(new Cell().add(new Paragraph("Nome"))
                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                        .setTextAlignment(TextAlignment.CENTER));
                        table.addHeaderCell(new Cell().add(new Paragraph("Data"))
                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                        .setTextAlignment(TextAlignment.CENTER));
                        table.addHeaderCell(new Cell().add(new Paragraph("Status"))
                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                        .setTextAlignment(TextAlignment.CENTER));
                        table.addHeaderCell(new Cell().add(new Paragraph("Laboratório"))
                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                        .setTextAlignment(TextAlignment.CENTER));
                        table.addHeaderCell(new Cell().add(new Paragraph("Paciente"))
                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                        .setTextAlignment(TextAlignment.CENTER));

                        // Preenchendo a tabela com dados
                        for (Exame exame : exames) {
                                table.addCell(new Cell().add(new Paragraph(String.valueOf(exame.getId())))
                                                .setTextAlignment(TextAlignment.CENTER));
                                table.addCell(new Cell().add(new Paragraph(exame.getNome()))
                                                .setTextAlignment(TextAlignment.LEFT));
                                table.addCell(new Cell().add(new Paragraph(exame.getData().toString()))
                                                .setTextAlignment(TextAlignment.CENTER));
                                table.addCell(new Cell().add(new Paragraph(exame.getStatus()))
                                                .setTextAlignment(TextAlignment.CENTER));
                                table.addCell(new Cell().add(new Paragraph(exame.getLaboratorio().getNome()))
                                                .setTextAlignment(TextAlignment.LEFT));
                                table.addCell(new Cell().add(new Paragraph(exame.getPaciente().getNome()))
                                                .setTextAlignment(TextAlignment.LEFT));
                        }

                        document.add(new Paragraph().add(table).setTextAlignment(TextAlignment.CENTER));

                        // Fechar o documento
                        document.close();
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return out;
        }
}
