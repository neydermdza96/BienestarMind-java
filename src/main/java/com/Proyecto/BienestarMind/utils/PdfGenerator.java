package com.Proyecto.BienestarMind.utils;


import freemarker.template.Template;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import jakarta.servlet.http.HttpServletResponse;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.OutputStream;
import java.util.*;
import java.time.LocalDate;

/**
 * Utilidad para generar archivos PDF a partir de plantillas FreeMarker.
 * 
 * Esta clase se encarga de:
 * <ul>
 * <li>Cargar una plantilla HTML definida en FreeMarker.</li>
 * <li>Procesar la plantilla con un modelo de datos.</li>
 * <li>Convertir el resultado HTML en un documento PDF.</li>
 * <li>Enviar el PDF como respuesta HTTP al cliente.</li>
 * </ul>
 */
@Component
public class PdfGenerator {

    private final FreeMarkerConfigurer configurer;

    /* Constructor de la clase PdfGenerator. */
    public PdfGenerator(FreeMarkerConfigurer configurer) {
        this.configurer = configurer;
    }

    /**
     * Genera un archivo PDF a partir de una plantilla FreeMarker y una lista de
     * datos.
     *
     * @param templateName nombre de la plantilla (sin extensión) que se usará para
     *                     generar el PDF
     * @param datos        lista de objetos que se inyectarán en la plantilla
     *                     (ejemplo: lista de clientes)
     * @param response     objeto HttpServletResponse donde se escribirá el PDF
     *                     generado
     * @throws Exception si ocurre algún error durante la carga de la plantilla o la
     *                   generación del PDF
     *
     *                   <p>
     *                   Flujo del método:
     *                   </p>
     *                   <ol>
     *                   <li>Se crea un modelo de datos y se asocia con la clave
     *                   "clientes".</li>
     *                   <li>Se obtiene la plantilla HTML desde la configuración de
     *                   FreeMarker.</li>
     *                   <li>Se procesa la plantilla para generar el contenido
     *                   HTML.</li>
     *                   <li>Se configura la respuesta HTTP con tipo de contenido
     *                   PDF y encabezado de descarga.</li>
     *                   <li>Se utiliza ITextRenderer para convertir el HTML en
     *                   PDF.</li>
     *                   <li>El PDF se escribe en el OutputStream de la
     *                   respuesta.</li>
     *                   </ol>
     */
    public void generarPdfReservaElementos(String templateName, List<?> datos,String descripcion, LocalDate desde, LocalDate hasta,
            HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("listaReservas", datos);
        model.put("descripcion", descripcion);
        model.put("desde", desde);
        model.put("hasta", hasta);

        Template template = configurer.getConfiguration().getTemplate(templateName + ".html");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reserva-elementos.pdf");

        OutputStream out = response.getOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(out);
        out.close();
    }

    public void generarPdfReservaEspacios(String templateName, List<?> datos,String motivo, LocalDate desde, LocalDate hasta,
            HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("listaReservasEspacio", datos);
        model.put("motivo", motivo);
        model.put("desde", desde);
        model.put("hasta", hasta);

        Template template = configurer.getConfiguration().getTemplate(templateName + ".html");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reserva-espacios.pdf");

        OutputStream out = response.getOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(out);
        out.close();
    }

     public void generarPdfFichas(String templateName, List<?> datos,String ficha, String programa,
            HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("listaFichas", datos);
        model.put("ficha", ficha);
        model.put("programa", programa);

        Template template = configurer.getConfiguration().getTemplate(templateName + ".html");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=fichas.pdf");

        OutputStream out = response.getOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(out);
        out.close();
    }
}

